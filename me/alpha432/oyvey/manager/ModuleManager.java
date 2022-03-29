package me.alpha432.oyvey.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.client.Components;
import me.alpha432.oyvey.features.modules.client.FontMod;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.features.modules.client.Logo;
import me.alpha432.oyvey.features.modules.client.MainMenu;
import me.alpha432.oyvey.features.modules.client.WaterMarkNew;
import me.alpha432.oyvey.features.modules.combat.AutoArmor;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.modules.combat.AutoTrap;
import me.alpha432.oyvey.features.modules.combat.CEV;
import me.alpha432.oyvey.features.modules.combat.CIV;
import me.alpha432.oyvey.features.modules.combat.CevBlocker;
import me.alpha432.oyvey.features.modules.combat.HoleFiller;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.features.modules.combat.Offhand;
import me.alpha432.oyvey.features.modules.combat.SelfAnvil;
import me.alpha432.oyvey.features.modules.combat.SelfFill;
import me.alpha432.oyvey.features.modules.combat.Selftrap;
import me.alpha432.oyvey.features.modules.combat.SilentXP;
import me.alpha432.oyvey.features.modules.combat.Surround;
import me.alpha432.oyvey.features.modules.combat.TrapPhase;
import me.alpha432.oyvey.features.modules.misc.AutoRespawn;
import me.alpha432.oyvey.features.modules.misc.BuildHeight;
import me.alpha432.oyvey.features.modules.misc.Chat;
import me.alpha432.oyvey.features.modules.misc.ChatModifier;
import me.alpha432.oyvey.features.modules.misc.ExtraTab;
import me.alpha432.oyvey.features.modules.misc.Kill;
import me.alpha432.oyvey.features.modules.misc.MCF;
import me.alpha432.oyvey.features.modules.misc.NoHandShake;
import me.alpha432.oyvey.features.modules.misc.NoHitBox;
import me.alpha432.oyvey.features.modules.misc.PearlNotify;
import me.alpha432.oyvey.features.modules.misc.PopCounter;
import me.alpha432.oyvey.features.modules.misc.Spammer;
import me.alpha432.oyvey.features.modules.misc.TEST;
import me.alpha432.oyvey.features.modules.misc.ToolTips;
import me.alpha432.oyvey.features.modules.misc.Tracker;
import me.alpha432.oyvey.features.modules.movement.AirJump;
import me.alpha432.oyvey.features.modules.movement.HoleTP;
import me.alpha432.oyvey.features.modules.movement.NoSlowDown;
import me.alpha432.oyvey.features.modules.movement.ReverseStep;
import me.alpha432.oyvey.features.modules.movement.StepOld;
import me.alpha432.oyvey.features.modules.movement.VanillaSpeed;
import me.alpha432.oyvey.features.modules.movement.Velocity;
import me.alpha432.oyvey.features.modules.movement.Webbypass;
import me.alpha432.oyvey.features.modules.player.FakePlayer;
import me.alpha432.oyvey.features.modules.player.FastPlace;
import me.alpha432.oyvey.features.modules.player.LiquidInteract;
import me.alpha432.oyvey.features.modules.player.MCP;
import me.alpha432.oyvey.features.modules.player.MoonBreak;
import me.alpha432.oyvey.features.modules.player.MultiTask;
import me.alpha432.oyvey.features.modules.player.Replenish;
import me.alpha432.oyvey.features.modules.player.Speedmine;
import me.alpha432.oyvey.features.modules.player.TpsSync;
import me.alpha432.oyvey.features.modules.render.AimBug;
import me.alpha432.oyvey.features.modules.render.Animations;
import me.alpha432.oyvey.features.modules.render.ArrowESP;
import me.alpha432.oyvey.features.modules.render.BlockHighlight;
import me.alpha432.oyvey.features.modules.render.Chams;
import me.alpha432.oyvey.features.modules.render.ESP;
import me.alpha432.oyvey.features.modules.render.Fullbright;
import me.alpha432.oyvey.features.modules.render.HandChams;
import me.alpha432.oyvey.features.modules.render.HoleESP;
import me.alpha432.oyvey.features.modules.render.ItemPhysics;
import me.alpha432.oyvey.features.modules.render.NoRender;
import me.alpha432.oyvey.features.modules.render.PopChams;
import me.alpha432.oyvey.features.modules.render.Skeleton;
import me.alpha432.oyvey.features.modules.render.SmallShield;
import me.alpha432.oyvey.features.modules.render.TestNametags;
import me.alpha432.oyvey.features.modules.render.Tracer;
import me.alpha432.oyvey.features.modules.render.Trajectories;
import me.alpha432.oyvey.features.modules.render.ViewModel;
import me.alpha432.oyvey.features.modules.render.Wireframe;
import me.alpha432.oyvey.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager extends Feature {

    public ArrayList modules = new ArrayList();
    public List sortedModules = new ArrayList();
    public List sortedModulesABC = new ArrayList();
    public ModuleManager.Animation animationThread;

    public void init() {
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new ExtraTab());
        this.modules.add(new HUD());
        this.modules.add(new BlockHighlight());
        this.modules.add(new HoleESP());
        this.modules.add(new Skeleton());
        this.modules.add(new Wireframe());
        this.modules.add(new Replenish());
        this.modules.add(new SmallShield());
        this.modules.add(new HandChams());
        this.modules.add(new Trajectories());
        this.modules.add(new FakePlayer());
        this.modules.add(new TpsSync());
        this.modules.add(new MultiTask());
        this.modules.add(new MCP());
        this.modules.add(new LiquidInteract());
        this.modules.add(new Speedmine());
        this.modules.add(new ReverseStep());
        this.modules.add(new NoHandShake());
        this.modules.add(new BuildHeight());
        this.modules.add(new ChatModifier());
        this.modules.add(new MCF());
        this.modules.add(new PearlNotify());
        this.modules.add(new ToolTips());
        this.modules.add(new Tracker());
        this.modules.add(new PopCounter());
        this.modules.add(new Offhand());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoCrystal());
        this.modules.add(new Killaura());
        this.modules.add(new HoleFiller());
        this.modules.add(new AutoArmor());
        this.modules.add(new FastPlace());
        this.modules.add(new ESP());
        this.modules.add(new Selftrap());
        this.modules.add(new NoHitBox());
        this.modules.add(new SelfFill());
        this.modules.add(new ArrowESP());
        this.modules.add(new ViewModel());
        this.modules.add(new Chat());
        this.modules.add(new TrapPhase());
        this.modules.add(new StepOld());
        this.modules.add(new VanillaSpeed());
        this.modules.add(new HoleTP());
        this.modules.add(new Velocity());
        this.modules.add(new WaterMarkNew());
        this.modules.add(new Components());
        this.modules.add(new SelfAnvil());
        this.modules.add(new SilentXP());
        this.modules.add(new Fullbright());
        this.modules.add(new NoRender());
        this.modules.add(new NoSlowDown());
        this.modules.add(new MainMenu());
        this.modules.add(new Kill());
        this.modules.add(new Spammer());
        this.modules.add(new AutoRespawn());
        this.modules.add(new PopChams());
        this.modules.add(new MoonBreak());
        this.modules.add(new Tracer());
        this.modules.add(new TestNametags());
        this.modules.add(new CEV());
        this.modules.add(new CIV());
        this.modules.add(new Chams());
        this.modules.add(new CevBlocker());
        this.modules.add(new Logo());
        this.modules.add(new TEST());
        this.modules.add(new Animations());
        this.modules.add(new ItemPhysics());
        this.modules.add(new Webbypass());
        this.modules.add(new AirJump());
        this.modules.add(new AimBug());
    }

    public Module getModuleByName(String name) {
        Iterator iterator = this.modules.iterator();

        Module module;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            module = (Module) iterator.next();
        } while (!module.getName().equalsIgnoreCase(name));

        return module;
    }

    public Module getModuleByClass(Class clazz) {
        Iterator iterator = this.modules.iterator();

        Module module;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            module = (Module) iterator.next();
        } while (!clazz.isInstance(module));

        return module;
    }

    public void enableModule(Class clazz) {
        Module module = this.getModuleByClass(clazz);

        if (module != null) {
            module.enable();
        }

    }

    public void disableModule(Class clazz) {
        Module module = this.getModuleByClass(clazz);

        if (module != null) {
            module.disable();
        }

    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);

        if (module != null) {
            module.enable();
        }

    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);

        if (module != null) {
            module.disable();
        }

    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);

        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Module module = this.getModuleByClass(clazz);

        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        Iterator iterator = this.modules.iterator();

        Module module;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            module = (Module) iterator.next();
        } while (!module.getDisplayName().equalsIgnoreCase(displayName));

        return module;
    }

    public ArrayList getEnabledModules() {
        ArrayList enabledModules = new ArrayList();
        Iterator iterator = this.modules.iterator();

        while (iterator.hasNext()) {
            Module module = (Module) iterator.next();

            if (module.isEnabled()) {
                enabledModules.add(module);
            }
        }

        return enabledModules;
    }

    public ArrayList getEnabledModulesName() {
        ArrayList enabledModules = new ArrayList();
        Iterator iterator = this.modules.iterator();

        while (iterator.hasNext()) {
            Module module = (Module) iterator.next();

            if (module.isEnabled() && module.isDrawn()) {
                enabledModules.add(module.getFullArrayString());
            }
        }

        return enabledModules;
    }

    public ArrayList getModulesByCategory(Module.Category category) {
        ArrayList modulesCategory = new ArrayList();

        this.modules.forEach(accept<invokedynamic>(category, modulesCategory));
        return modulesCategory;
    }

    public List getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        Stream stream = this.modules.stream().filter(test<invokedynamic>());
        EventBus eventbus = MinecraftForge.EVENT_BUS;

        MinecraftForge.EVENT_BUS.getClass();
        stream.forEach(accept<invokedynamic>(eventbus));
        this.modules.forEach(accept<invokedynamic>());
    }

    public void onUpdate() {
        this.modules.stream().filter(test<invokedynamic>()).forEach(accept<invokedynamic>());
    }

    public void onTick() {
        this.modules.stream().filter(test<invokedynamic>()).forEach(accept<invokedynamic>());
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(test<invokedynamic>()).forEach(accept<invokedynamic>(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(test<invokedynamic>()).forEach(accept<invokedynamic>(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = (List) this.getEnabledModules().stream().filter(test<invokedynamic>()).sorted(Comparator.comparing(apply<invokedynamic>(this, reverse))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(accept<invokedynamic>());
    }

    public void onLogin() {
        this.modules.forEach(accept<invokedynamic>());
    }

    public void onUnload() {
        ArrayList arraylist = this.modules;
        EventBus eventbus = MinecraftForge.EVENT_BUS;

        MinecraftForge.EVENT_BUS.getClass();
        arraylist.forEach(accept<invokedynamic>(eventbus));
        this.modules.forEach(accept<invokedynamic>());
    }

    public void onUnloadPost() {
        Iterator iterator = this.modules.iterator();

        while (iterator.hasNext()) {
            Module module = (Module) iterator.next();

            module.enabled.setValue(Boolean.valueOf(false));
        }

    }

    public void onKeyPressed(int eventKey) {
        if (eventKey != 0 && Keyboard.getEventKeyState() && !(ModuleManager.mc.currentScreen instanceof OyVeyGui)) {
            this.modules.forEach(accept<invokedynamic>(eventKey));
        }
    }

    private static void lambda$onKeyPressed$4(int eventKey, Module module) {
        if (module.getBind().getKey() == eventKey) {
            module.toggle();
        }

    }

    private Integer lambda$sortModules$3(boolean reverse, Module module) {
        return Integer.valueOf(this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1));
    }

    private static void lambda$onRender3D$2(Render3DEvent event, Module module) {
        module.onRender3D(event);
    }

    private static void lambda$onRender2D$1(Render2DEvent event, Module module) {
        module.onRender2D(event);
    }

    private static void lambda$getModulesByCategory$0(Module.Category category, ArrayList modulesCategory, Module module) {
        if (module.getCategory() == category) {
            modulesCategory.add(module);
        }

    }

    private class Animation extends Thread {

        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        public Animation() {
            super("Animation");
        }

        public void run() {
            Iterator iterator;

            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                iterator = ModuleManager.this.sortedModules.iterator();

                while (iterator.hasNext()) {
                    Module e = (Module) iterator.next();
                    String module = e.getDisplayName() + ChatFormatting.GRAY + (e.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + e.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");

                    e.offset = (float) ModuleManager.this.renderer.getStringWidth(module) / ((Integer) HUD.getInstance().animationHorizontalTime.getValue()).floatValue();
                    e.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / ((Integer) HUD.getInstance().animationVerticalTime.getValue()).floatValue();
                    if (e.isEnabled() && ((Integer) HUD.getInstance().animationHorizontalTime.getValue()).intValue() != 1) {
                        if (e.arrayListOffset > e.offset && Util.mc.world != null) {
                            e.arrayListOffset -= e.offset;
                            e.sliding = true;
                        }
                    } else if (e.isDisabled() && ((Integer) HUD.getInstance().animationHorizontalTime.getValue()).intValue() != 1) {
                        if (e.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(module) && Util.mc.world != null) {
                            e.arrayListOffset += e.offset;
                            e.sliding = true;
                        } else {
                            e.sliding = false;
                        }
                    }
                }
            } else {
                iterator = ModuleManager.this.sortedModulesABC.iterator();

                while (iterator.hasNext()) {
                    String e1 = (String) iterator.next();
                    Module module1 = OyVey.moduleManager.getModuleByName(e1);
                    String text = module1.getDisplayName() + ChatFormatting.GRAY + (module1.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module1.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");

                    module1.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / ((Integer) HUD.getInstance().animationHorizontalTime.getValue()).floatValue();
                    module1.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / ((Integer) HUD.getInstance().animationVerticalTime.getValue()).floatValue();
                    if (module1.isEnabled() && ((Integer) HUD.getInstance().animationHorizontalTime.getValue()).intValue() != 1) {
                        if (module1.arrayListOffset > module1.offset && Util.mc.world != null) {
                            module1.arrayListOffset -= module1.offset;
                            module1.sliding = true;
                        }
                    } else if (module1.isDisabled() && ((Integer) HUD.getInstance().animationHorizontalTime.getValue()).intValue() != 1) {
                        if (module1.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                            module1.arrayListOffset += module1.offset;
                            module1.sliding = true;
                        } else {
                            module1.sliding = false;
                        }
                    }
                }
            }

        }

        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}
