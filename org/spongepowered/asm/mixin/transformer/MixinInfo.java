package org.spongepowered.asm.mixin.transformer;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.InnerClassNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTargetAlreadyLoadedException;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.perf.Profiler;

class MixinInfo implements Comparable, IMixinInfo {

    private static final IMixinService classLoaderUtil = MixinService.getService();
    static int mixinOrder = 0;
    private final transient Logger logger = LogManager.getLogger("mixin");
    private final transient Profiler profiler = MixinEnvironment.getProfiler();
    private final transient MixinConfig parent;
    private final String name;
    private final String className;
    private final int priority;
    private final boolean virtual;
    private final List targetClasses;
    private final List targetClassNames;
    private final transient int order;
    private final transient IMixinService service;
    private final transient IMixinConfigPlugin plugin;
    private final transient MixinEnvironment.Phase phase;
    private final transient ClassInfo info;
    private final transient MixinInfo.SubType type;
    private final transient boolean strict;
    private transient MixinInfo.State pendingState;
    private transient MixinInfo.State state;

    MixinInfo(IMixinService service, MixinConfig parent, String name, boolean runTransformers, IMixinConfigPlugin plugin, boolean suppressPlugin) {
        this.order = MixinInfo.mixinOrder++;
        this.service = service;
        this.parent = parent;
        this.name = name;
        this.className = parent.getMixinPackage() + name;
        this.plugin = plugin;
        this.phase = parent.getEnvironment().getPhase();
        this.strict = parent.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_TARGETS);

        try {
            byte[] ex = this.loadMixinClass(this.className, runTransformers);

            this.pendingState = new MixinInfo.State(ex);
            this.info = this.pendingState.getClassInfo();
            this.type = MixinInfo.SubType.getTypeFor(this);
        } catch (InvalidMixinException invalidmixinexception) {
            throw invalidmixinexception;
        } catch (Exception exception) {
            throw new InvalidMixinException(this, exception);
        }

        if (!this.type.isLoadable()) {
            MixinInfo.classLoaderUtil.registerInvalidClass(this.className);
        }

        try {
            this.priority = this.readPriority(this.pendingState.getClassNode());
            this.virtual = this.readPseudo(this.pendingState.getClassNode());
            this.targetClasses = this.readTargetClasses(this.pendingState.getClassNode(), suppressPlugin);
            this.targetClassNames = Collections.unmodifiableList(Lists.transform(this.targetClasses, Functions.toStringFunction()));
        } catch (InvalidMixinException invalidmixinexception1) {
            throw invalidmixinexception1;
        } catch (Exception exception1) {
            throw new InvalidMixinException(this, exception1);
        }
    }

    void validate() {
        if (this.pendingState == null) {
            throw new IllegalStateException("No pending validation state for " + this);
        } else {
            try {
                this.pendingState.validate(this.type, this.targetClasses);
                this.state = this.pendingState;
            } finally {
                this.pendingState = null;
            }

        }
    }

    protected List readTargetClasses(MixinInfo.MixinClassNode classNode, boolean suppressPlugin) {
        if (classNode == null) {
            return Collections.emptyList();
        } else {
            AnnotationNode mixin = Annotations.getInvisible((ClassNode) classNode, Mixin.class);

            if (mixin == null) {
                throw new InvalidMixinException(this, String.format("The mixin \'%s\' is missing an @Mixin annotation", new Object[] { this.className}));
            } else {
                ArrayList targets = new ArrayList();
                List publicTargets = (List) Annotations.getValue(mixin, "value");
                List privateTargets = (List) Annotations.getValue(mixin, "targets");

                if (publicTargets != null) {
                    this.readTargets(targets, Lists.transform(publicTargets, new Function() {
                        public String apply(Type input) {
                            return input.getClassName();
                        }
                    }), suppressPlugin, false);
                }

                if (privateTargets != null) {
                    this.readTargets(targets, Lists.transform(privateTargets, new Function() {
                        public String apply(String input) {
                            return MixinInfo.this.getParent().remapClassName(MixinInfo.this.getClassRef(), input);
                        }
                    }), suppressPlugin, true);
                }

                return targets;
            }
        }
    }

    private void readTargets(Collection outTargets, Collection inTargets, boolean suppressPlugin, boolean checkPublic) {
        Iterator iterator = inTargets.iterator();

        while (iterator.hasNext()) {
            String targetRef = (String) iterator.next();
            String targetName = targetRef.replace('/', '.');

            if (MixinInfo.classLoaderUtil.isClassLoaded(targetName) && !this.isReloading()) {
                String targetInfo = String.format("Critical problem: %s target %s was already transformed.", new Object[] { this, targetName});

                if (this.parent.isRequired()) {
                    throw new MixinTargetAlreadyLoadedException(this, targetInfo, targetName);
                }

                this.logger.error(targetInfo);
            }

            if (this.shouldApplyMixin(suppressPlugin, targetName)) {
                ClassInfo targetInfo1 = this.getTarget(targetName, checkPublic);

                if (targetInfo1 != null && !outTargets.contains(targetInfo1)) {
                    outTargets.add(targetInfo1);
                    targetInfo1.addMixin(this);
                }
            }
        }

    }

    private boolean shouldApplyMixin(boolean suppressPlugin, String targetName) {
        Profiler.Section pluginTimer = this.profiler.begin("plugin");
        boolean result = this.plugin == null || suppressPlugin || this.plugin.shouldApplyMixin(targetName, this.className);

        pluginTimer.end();
        return result;
    }

    private ClassInfo getTarget(String targetName, boolean checkPublic) throws InvalidMixinException {
        ClassInfo targetInfo = ClassInfo.forName(targetName);

        if (targetInfo == null) {
            if (this.isVirtual()) {
                this.logger.debug("Skipping virtual target {} for {}", new Object[] { targetName, this});
            } else {
                this.handleTargetError(String.format("@Mixin target %s was not found %s", new Object[] { targetName, this}));
            }

            return null;
        } else {
            this.type.validateTarget(targetName, targetInfo);
            if (checkPublic && targetInfo.isPublic() && !this.isVirtual()) {
                this.handleTargetError(String.format("@Mixin target %s is public in %s and should be specified in value", new Object[] { targetName, this}));
            }

            return targetInfo;
        }
    }

    private void handleTargetError(String message) {
        if (this.strict) {
            this.logger.error(message);
            throw new InvalidMixinException(this, message);
        } else {
            this.logger.warn(message);
        }
    }

    protected int readPriority(ClassNode classNode) {
        if (classNode == null) {
            return this.parent.getDefaultMixinPriority();
        } else {
            AnnotationNode mixin = Annotations.getInvisible(classNode, Mixin.class);

            if (mixin == null) {
                throw new InvalidMixinException(this, String.format("The mixin \'%s\' is missing an @Mixin annotation", new Object[] { this.className}));
            } else {
                Integer priority = (Integer) Annotations.getValue(mixin, "priority");

                return priority == null ? this.parent.getDefaultMixinPriority() : priority.intValue();
            }
        }
    }

    protected boolean readPseudo(ClassNode classNode) {
        return Annotations.getInvisible(classNode, Pseudo.class) != null;
    }

    private boolean isReloading() {
        return this.pendingState instanceof MixinInfo.Reloaded;
    }

    private MixinInfo.State getState() {
        return this.state != null ? this.state : this.pendingState;
    }

    ClassInfo getClassInfo() {
        return this.info;
    }

    public IMixinConfig getConfig() {
        return this.parent;
    }

    MixinConfig getParent() {
        return this.parent;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getName() {
        return this.name;
    }

    public String getClassName() {
        return this.className;
    }

    public String getClassRef() {
        return this.getClassInfo().getName();
    }

    public byte[] getClassBytes() {
        return this.getState().getClassBytes();
    }

    public boolean isDetachedSuper() {
        return this.getState().isDetachedSuper();
    }

    public boolean isUnique() {
        return this.getState().isUnique();
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    public boolean isAccessor() {
        return this.type instanceof MixinInfo.SubType.Accessor;
    }

    public boolean isLoadable() {
        return this.type.isLoadable();
    }

    public Level getLoggingLevel() {
        return this.parent.getLoggingLevel();
    }

    public MixinEnvironment.Phase getPhase() {
        return this.phase;
    }

    public MixinInfo.MixinClassNode getClassNode(int flags) {
        return this.getState().createClassNode(flags);
    }

    public List getTargetClasses() {
        return this.targetClassNames;
    }

    List getSoftImplements() {
        return Collections.unmodifiableList(this.getState().getSoftImplements());
    }

    Set getSyntheticInnerClasses() {
        return Collections.unmodifiableSet(this.getState().getSyntheticInnerClasses());
    }

    Set getInnerClasses() {
        return Collections.unmodifiableSet(this.getState().getInnerClasses());
    }

    List getTargets() {
        return Collections.unmodifiableList(this.targetClasses);
    }

    Set getInterfaces() {
        return this.getState().getInterfaces();
    }

    MixinTargetContext createContextFor(TargetClassContext target) {
        MixinInfo.MixinClassNode classNode = this.getClassNode(8);
        Profiler.Section preTimer = this.profiler.begin("pre");
        MixinTargetContext preProcessor = this.type.createPreProcessor(classNode).prepare().createContextFor(target);

        preTimer.end();
        return preProcessor;
    }

    private byte[] loadMixinClass(String mixinClassName, boolean runTransformers) throws ClassNotFoundException {
        Object mixinBytes = null;

        try {
            if (runTransformers) {
                String ex = this.service.getClassRestrictions(mixinClassName);

                if (ex.length() > 0) {
                    this.logger.error("Classloader restrictions [{}] encountered loading {}, name: {}", new Object[] { ex, this, mixinClassName});
                }
            }

            byte[] mixinBytes1 = this.service.getBytecodeProvider().getClassBytes(mixinClassName, runTransformers);

            return mixinBytes1;
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new ClassNotFoundException(String.format("The specified mixin \'%s\' was not found", new Object[] { mixinClassName}));
        } catch (IOException ioexception) {
            this.logger.warn("Failed to load mixin {}, the specified mixin will not be applied", new Object[] { mixinClassName});
            throw new InvalidMixinException(this, "An error was encountered whilst loading the mixin class", ioexception);
        }
    }

    void reloadMixin(byte[] mixinBytes) {
        if (this.pendingState != null) {
            throw new IllegalStateException("Cannot reload mixin while it is initialising");
        } else {
            this.pendingState = new MixinInfo.Reloaded(this.state, mixinBytes);
            this.validate();
        }
    }

    public int compareTo(MixinInfo other) {
        return other == null ? 0 : (other.priority == this.priority ? this.order - other.order : this.priority - other.priority);
    }

    public void preApply(String transformedName, ClassNode targetClass) {
        if (this.plugin != null) {
            Profiler.Section pluginTimer = this.profiler.begin("plugin");

            this.plugin.preApply(transformedName, targetClass, this.className, this);
            pluginTimer.end();
        }

    }

    public void postApply(String transformedName, ClassNode targetClass) {
        if (this.plugin != null) {
            Profiler.Section pluginTimer = this.profiler.begin("plugin");

            this.plugin.postApply(transformedName, targetClass, this.className, this);
            pluginTimer.end();
        }

        this.parent.postApply(transformedName, targetClass);
    }

    public String toString() {
        return String.format("%s:%s", new Object[] { this.parent.getName(), this.name});
    }

    abstract static class SubType {

        protected final MixinInfo mixin;
        protected final String annotationType;
        protected final boolean targetMustBeInterface;
        protected boolean detached;

        SubType(MixinInfo info, String annotationType, boolean targetMustBeInterface) {
            this.mixin = info;
            this.annotationType = annotationType;
            this.targetMustBeInterface = targetMustBeInterface;
        }

        Collection getInterfaces() {
            return Collections.emptyList();
        }

        boolean isDetachedSuper() {
            return this.detached;
        }

        boolean isLoadable() {
            return false;
        }

        void validateTarget(String targetName, ClassInfo targetInfo) {
            boolean targetIsInterface = targetInfo.isInterface();

            if (targetIsInterface != this.targetMustBeInterface) {
                String not = targetIsInterface ? "" : "not ";

                throw new InvalidMixinException(this.mixin, this.annotationType + " target type mismatch: " + targetName + " is " + not + "an interface in " + this);
            }
        }

        abstract void validate(MixinInfo.State mixininfo_state, List list);

        abstract MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode mixininfo_mixinclassnode);

        static MixinInfo.SubType getTypeFor(MixinInfo mixin) {
            if (!mixin.getClassInfo().isInterface()) {
                return new MixinInfo.SubType.Standard(mixin);
            } else {
                boolean containsNonAccessorMethod = false;

                ClassInfo.Method method;

                for (Iterator iterator = mixin.getClassInfo().getMethods().iterator(); iterator.hasNext(); containsNonAccessorMethod |= !method.isAccessor()) {
                    method = (ClassInfo.Method) iterator.next();
                }

                if (containsNonAccessorMethod) {
                    return new MixinInfo.SubType.Interface(mixin);
                } else {
                    return new MixinInfo.SubType.Accessor(mixin);
                }
            }
        }

        static class Accessor extends MixinInfo.SubType {

            private final Collection interfaces = new ArrayList();

            Accessor(MixinInfo info) {
                super(info, "@Mixin", false);
                this.interfaces.add(info.getClassRef());
            }

            boolean isLoadable() {
                return true;
            }

            Collection getInterfaces() {
                return this.interfaces;
            }

            void validateTarget(String targetName, ClassInfo targetInfo) {
                boolean targetIsInterface = targetInfo.isInterface();

                if (targetIsInterface && !MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
                    throw new InvalidMixinException(this.mixin, "Accessor mixin targetting an interface is not supported in current enviromnment");
                }
            }

            void validate(MixinInfo.State state, List targetClasses) {
                MixinInfo.MixinClassNode classNode = state.getClassNode();

                if (!"java/lang/Object".equals(classNode.superName)) {
                    throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + classNode.superName.replace('/', '.'));
                }
            }

            MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode classNode) {
                return new MixinPreProcessorAccessor(this.mixin, classNode);
            }
        }

        static class Interface extends MixinInfo.SubType {

            Interface(MixinInfo info) {
                super(info, "@Mixin", true);
            }

            void validate(MixinInfo.State state, List targetClasses) {
                if (!MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
                    throw new InvalidMixinException(this.mixin, "Interface mixin not supported in current enviromnment");
                } else {
                    MixinInfo.MixinClassNode classNode = state.getClassNode();

                    if (!"java/lang/Object".equals(classNode.superName)) {
                        throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + classNode.superName.replace('/', '.'));
                    }
                }
            }

            MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode classNode) {
                return new MixinPreProcessorInterface(this.mixin, classNode);
            }
        }

        static class Standard extends MixinInfo.SubType {

            Standard(MixinInfo info) {
                super(info, "@Mixin", false);
            }

            void validate(MixinInfo.State state, List targetClasses) {
                MixinInfo.MixinClassNode classNode = state.getClassNode();
                Iterator iterator = targetClasses.iterator();

                while (iterator.hasNext()) {
                    ClassInfo targetClass = (ClassInfo) iterator.next();

                    if (!classNode.superName.equals(targetClass.getSuperName())) {
                        if (!targetClass.hasSuperClass(classNode.superName, ClassInfo.Traversal.SUPER)) {
                            ClassInfo superClass = ClassInfo.forName(classNode.superName);

                            if (superClass.isMixin()) {
                                Iterator iterator1 = superClass.getTargets().iterator();

                                while (iterator1.hasNext()) {
                                    ClassInfo superTarget = (ClassInfo) iterator1.next();

                                    if (targetClasses.contains(superTarget)) {
                                        throw new InvalidMixinException(this.mixin, "Illegal hierarchy detected. Derived mixin " + this + " targets the same class " + superTarget.getClassName() + " as its superclass " + superClass.getClassName());
                                    }
                                }
                            }

                            throw new InvalidMixinException(this.mixin, "Super class \'" + classNode.superName.replace('/', '.') + "\' of " + this.mixin.getName() + " was not found in the hierarchy of target class \'" + targetClass + "\'");
                        }

                        this.detached = true;
                    }
                }

            }

            MixinPreProcessorStandard createPreProcessor(MixinInfo.MixinClassNode classNode) {
                return new MixinPreProcessorStandard(this.mixin, classNode);
            }
        }
    }

    class Reloaded extends MixinInfo.State {

        private final MixinInfo.State previous;

        Reloaded(MixinInfo.State previous, byte[] mixinBytes) {
            super(mixinBytes, previous.getClassInfo());
            this.previous = previous;
        }

        protected void validateChanges(MixinInfo.SubType type, List targetClasses) {
            if (!this.syntheticInnerClasses.equals(this.previous.syntheticInnerClasses)) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change inner classes");
            } else if (!this.interfaces.equals(this.previous.interfaces)) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change interfaces");
            } else if (!(new HashSet(this.softImplements)).equals(new HashSet(this.previous.softImplements))) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change soft interfaces");
            } else {
                List targets = MixinInfo.this.readTargetClasses(this.classNode, true);

                if (!(new HashSet(targets)).equals(new HashSet(targetClasses))) {
                    throw new MixinReloadException(MixinInfo.this, "Cannot change target classes");
                } else {
                    int priority = MixinInfo.this.readPriority(this.classNode);

                    if (priority != MixinInfo.this.getPriority()) {
                        throw new MixinReloadException(MixinInfo.this, "Cannot change mixin priority");
                    }
                }
            }
        }
    }

    class State {

        private byte[] mixinBytes;
        private final ClassInfo classInfo;
        private boolean detachedSuper;
        private boolean unique;
        protected final Set interfaces;
        protected final List softImplements;
        protected final Set syntheticInnerClasses;
        protected final Set innerClasses;
        protected MixinInfo.MixinClassNode classNode;

        State(byte[] mixinBytes) {
            this(mixinBytes, (ClassInfo) null);
        }

        State(byte[] mixinBytes, ClassInfo classInfo) {
            this.interfaces = new HashSet();
            this.softImplements = new ArrayList();
            this.syntheticInnerClasses = new HashSet();
            this.innerClasses = new HashSet();
            this.mixinBytes = mixinBytes;
            this.connect();
            this.classInfo = classInfo != null ? classInfo : ClassInfo.fromClassNode(this.getClassNode());
        }

        private void connect() {
            this.classNode = this.createClassNode(0);
        }

        private void complete() {
            this.classNode = null;
        }

        ClassInfo getClassInfo() {
            return this.classInfo;
        }

        byte[] getClassBytes() {
            return this.mixinBytes;
        }

        MixinInfo.MixinClassNode getClassNode() {
            return this.classNode;
        }

        boolean isDetachedSuper() {
            return this.detachedSuper;
        }

        boolean isUnique() {
            return this.unique;
        }

        List getSoftImplements() {
            return this.softImplements;
        }

        Set getSyntheticInnerClasses() {
            return this.syntheticInnerClasses;
        }

        Set getInnerClasses() {
            return this.innerClasses;
        }

        Set getInterfaces() {
            return this.interfaces;
        }

        MixinInfo.MixinClassNode createClassNode(int flags) {
            MixinInfo.MixinClassNode classNode = MixinInfo.this.new MixinClassNode(MixinInfo.this);
            ClassReader classReader = new ClassReader(this.mixinBytes);

            classReader.accept(classNode, flags);
            return classNode;
        }

        void validate(MixinInfo.SubType type, List targetClasses) {
            MixinPreProcessorStandard preProcessor = type.createPreProcessor(this.getClassNode()).prepare();
            Iterator iterator = targetClasses.iterator();

            while (iterator.hasNext()) {
                ClassInfo target = (ClassInfo) iterator.next();

                preProcessor.conform(target);
            }

            type.validate(this, targetClasses);
            this.detachedSuper = type.isDetachedSuper();
            this.unique = Annotations.getVisible((ClassNode) this.getClassNode(), Unique.class) != null;
            this.validateInner();
            this.validateClassVersion();
            this.validateRemappables(targetClasses);
            this.readImplementations(type);
            this.readInnerClasses();
            this.validateChanges(type, targetClasses);
            this.complete();
        }

        private void validateInner() {
            if (!this.classInfo.isProbablyStatic()) {
                throw new InvalidMixinException(MixinInfo.this, "Inner class mixin must be declared static");
            }
        }

        private void validateClassVersion() {
            if (this.classNode.version > MixinEnvironment.getCompatibilityLevel().classVersion()) {
                String helpText = ".";
                MixinEnvironment.CompatibilityLevel[] amixinenvironment_compatibilitylevel = MixinEnvironment.CompatibilityLevel.values();
                int i = amixinenvironment_compatibilitylevel.length;

                for (int j = 0; j < i; ++j) {
                    MixinEnvironment.CompatibilityLevel level = amixinenvironment_compatibilitylevel[j];

                    if (level.classVersion() >= this.classNode.version) {
                        helpText = String.format(". Mixin requires compatibility level %s or above.", new Object[] { level.name()});
                    }
                }

                throw new InvalidMixinException(MixinInfo.this, "Unsupported mixin class version " + this.classNode.version + helpText);
            }
        }

        private void validateRemappables(List targetClasses) {
            if (targetClasses.size() > 1) {
                Iterator iterator = this.classNode.fields.iterator();

                while (iterator.hasNext()) {
                    FieldNode method = (FieldNode) iterator.next();

                    this.validateRemappable(Shadow.class, method.name, Annotations.getVisible(method, Shadow.class));
                }

                iterator = this.classNode.methods.iterator();

                while (iterator.hasNext()) {
                    MethodNode method1 = (MethodNode) iterator.next();

                    this.validateRemappable(Shadow.class, method1.name, Annotations.getVisible(method1, Shadow.class));
                    AnnotationNode overwrite = Annotations.getVisible(method1, Overwrite.class);

                    if (overwrite != null && ((method1.access & 8) == 0 || (method1.access & 1) == 0)) {
                        throw new InvalidMixinException(MixinInfo.this, "Found @Overwrite annotation on " + method1.name + " in " + MixinInfo.this);
                    }
                }
            }

        }

        private void validateRemappable(Class annotationClass, String name, AnnotationNode annotation) {
            if (annotation != null && ((Boolean) Annotations.getValue(annotation, "remap", (Object) Boolean.TRUE)).booleanValue()) {
                throw new InvalidMixinException(MixinInfo.this, "Found a remappable @" + annotationClass.getSimpleName() + " annotation on " + name + " in " + this);
            }
        }

        void readImplementations(MixinInfo.SubType type) {
            this.interfaces.addAll(this.classNode.interfaces);
            this.interfaces.addAll(type.getInterfaces());
            AnnotationNode implementsAnnotation = Annotations.getInvisible((ClassNode) this.classNode, Implements.class);

            if (implementsAnnotation != null) {
                List interfaces = (List) Annotations.getValue(implementsAnnotation);

                if (interfaces != null) {
                    Iterator iterator = interfaces.iterator();

                    while (iterator.hasNext()) {
                        AnnotationNode interfaceNode = (AnnotationNode) iterator.next();
                        InterfaceInfo interfaceInfo = InterfaceInfo.fromAnnotation(MixinInfo.this, interfaceNode);

                        this.softImplements.add(interfaceInfo);
                        this.interfaces.add(interfaceInfo.getInternalName());
                        if (!(this instanceof MixinInfo.Reloaded)) {
                            this.classInfo.addInterface(interfaceInfo.getInternalName());
                        }
                    }

                }
            }
        }

        void readInnerClasses() {
            Iterator iterator = this.classNode.innerClasses.iterator();

            while (iterator.hasNext()) {
                InnerClassNode inner = (InnerClassNode) iterator.next();
                ClassInfo innerClass = ClassInfo.forName(inner.name);

                if (inner.outerName != null && inner.outerName.equals(this.classInfo.getName()) || inner.name.startsWith(this.classNode.name + "$")) {
                    if (innerClass.isProbablyStatic() && innerClass.isSynthetic()) {
                        this.syntheticInnerClasses.add(inner.name);
                    } else {
                        this.innerClasses.add(inner.name);
                    }
                }
            }

        }

        protected void validateChanges(MixinInfo.SubType type, List targetClasses) {
            type.createPreProcessor(this.classNode).prepare();
        }
    }

    class MixinClassNode extends ClassNode {

        public final List mixinMethods;

        public MixinClassNode(MixinInfo mixin) {
            this(327680);
        }

        public MixinClassNode(int api) {
            super(api);
            this.mixinMethods = (List) this.methods;
        }

        public MixinInfo getMixin() {
            return MixinInfo.this;
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MixinInfo.MixinMethodNode method = MixinInfo.this.new MixinMethodNode(access, name, desc, signature, exceptions);

            this.methods.add(method);
            return method;
        }
    }

    class MixinMethodNode extends MethodNode {

        private final String originalName;

        public MixinMethodNode(int access, String name, String desc, String signature, String[] exceptions) {
            super(327680, access, name, desc, signature, exceptions);
            this.originalName = name;
        }

        public String toString() {
            return String.format("%s%s", new Object[] { this.originalName, this.desc});
        }

        public String getOriginalName() {
            return this.originalName;
        }

        public boolean isInjector() {
            return this.getInjectorAnnotation() != null || this.isSurrogate();
        }

        public boolean isSurrogate() {
            return this.getVisibleAnnotation(Surrogate.class) != null;
        }

        public boolean isSynthetic() {
            return Bytecode.hasFlag((MethodNode) this, 4096);
        }

        public AnnotationNode getVisibleAnnotation(Class annotationClass) {
            return Annotations.getVisible((MethodNode) this, annotationClass);
        }

        public AnnotationNode getInjectorAnnotation() {
            return InjectionInfo.getInjectorAnnotation(MixinInfo.this, this);
        }

        public IMixinInfo getOwner() {
            return MixinInfo.this;
        }
    }
}
