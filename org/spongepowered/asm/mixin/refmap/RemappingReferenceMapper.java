package org.spongepowered.asm.mixin.refmap;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

public final class RemappingReferenceMapper implements IReferenceMapper {

    private static final String DEFAULT_RESOURCE_PATH_PROPERTY = "net.minecraftforge.gradle.GradleStart.srg.srg-mcp";
    private static final String DEFAULT_MAPPING_ENV = "name";
    private static final Logger logger = LogManager.getLogger("mixin");
    private static final Map srgs = new HashMap();
    private final IReferenceMapper refMap;
    private final Map mappings;
    private final Map cache = new HashMap();

    private RemappingReferenceMapper(MixinEnvironment env, IReferenceMapper refMap) {
        this.refMap = refMap;
        this.refMap.setContext(getMappingEnv(env));
        String resource = getResource(env);

        this.mappings = loadSrgs(resource);
        RemappingReferenceMapper.logger.info("Remapping refMap {} using {}", new Object[] { refMap.getResourceName(), resource});
    }

    public boolean isDefault() {
        return this.refMap.isDefault();
    }

    public String getResourceName() {
        return this.refMap.getResourceName();
    }

    public String getStatus() {
        return this.refMap.getStatus();
    }

    public String getContext() {
        return this.refMap.getContext();
    }

    public void setContext(String context) {}

    public String remap(String className, String reference) {
        Map classCache = this.getCache(className);
        String remapped = (String) classCache.get(reference);

        if (remapped == null) {
            remapped = this.refMap.remap(className, reference);

            Entry entry;

            for (Iterator iterator = this.mappings.entrySet().iterator(); iterator.hasNext(); remapped = remapped.replace((CharSequence) entry.getKey(), (CharSequence) entry.getValue())) {
                entry = (Entry) iterator.next();
            }

            classCache.put(reference, remapped);
        }

        return remapped;
    }

    private Map getCache(String className) {
        Object classCache = (Map) this.cache.get(className);

        if (classCache == null) {
            classCache = new HashMap();
            this.cache.put(className, classCache);
        }

        return (Map) classCache;
    }

    public String remapWithContext(String context, String className, String reference) {
        return this.refMap.remapWithContext(context, className, reference);
    }

    private static Map loadSrgs(String fileName) {
        if (RemappingReferenceMapper.srgs.containsKey(fileName)) {
            return (Map) RemappingReferenceMapper.srgs.get(fileName);
        } else {
            final HashMap map = new HashMap();

            RemappingReferenceMapper.srgs.put(fileName, map);
            File file = new File(fileName);

            if (!file.isFile()) {
                return map;
            } else {
                try {
                    Files.readLines(file, Charsets.UTF_8, new LineProcessor() {
                        public Object getResult() {
                            return null;
                        }

                        public boolean processLine(String line) throws IOException {
                            if (!Strings.isNullOrEmpty(line) && !line.startsWith("#")) {
                                byte fromPos = 0;
                                boolean toPos = false;
                                int toPos1;

                                if ((toPos1 = line.startsWith("MD: ") ? 2 : (line.startsWith("FD: ") ? 1 : 0)) > 0) {
                                    String[] entries = line.substring(4).split(" ", 4);

                                    map.put(entries[fromPos].substring(entries[fromPos].lastIndexOf(47) + 1), entries[toPos1].substring(entries[toPos1].lastIndexOf(47) + 1));
                                }

                                return true;
                            } else {
                                return true;
                            }
                        }
                    });
                } catch (IOException ioexception) {
                    RemappingReferenceMapper.logger.warn("Could not read input SRG file: {}", new Object[] { fileName});
                    RemappingReferenceMapper.logger.catching(ioexception);
                }

                return map;
            }
        }
    }

    public static IReferenceMapper of(MixinEnvironment env, IReferenceMapper refMap) {
        return (IReferenceMapper) (!refMap.isDefault() && hasData(env) ? new RemappingReferenceMapper(env, refMap) : refMap);
    }

    private static boolean hasData(MixinEnvironment env) {
        String fileName = getResource(env);

        return fileName != null && (new File(fileName)).exists();
    }

    private static String getResource(MixinEnvironment env) {
        String resource = env.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_RESOURCE);

        return Strings.isNullOrEmpty(resource) ? System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") : resource;
    }

    private static String getMappingEnv(MixinEnvironment env) {
        String resource = env.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_SOURCE_ENV);

        return Strings.isNullOrEmpty(resource) ? "name" : resource;
    }
}
