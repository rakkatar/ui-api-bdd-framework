package pl.recruitment.qa.core;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public final class ConfigProvider {
    private static final Map<String, Object> YAML_MAP;

    static {
        Map<String, Object> map = Collections.emptyMap();
        try (InputStream in = ConfigProvider.class.getClassLoader().getResourceAsStream("config.yml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                Object loaded = yaml.load(in);
                if (loaded instanceof Map) {
                    //noinspection unchecked
                    map = (Map<String, Object>) loaded;
                }
            }
        } catch (Exception e) {
            // ignore, will fall back to env/props/defaults
        }
        YAML_MAP = map;
    }

    private ConfigProvider() {
    }

    @SuppressWarnings("unchecked")
    private static Object lookupYaml(String key) {
        // support dotted keys like 'ui.baseUrl' and nested maps
        String[] parts = key.split("\\.");
        Object current = YAML_MAP;
        for (String p : parts) {
            if (!(current instanceof Map)) return null;
            Map<String, Object> m = (Map<String, Object>) current;
            current = m.get(p);
            if (current == null) return null;
        }
        return current;
    }

    public static String get(String key) {
        // 1) JVM property
        String val = System.getProperty(key);
        if (val != null && !val.isEmpty()) return val;

        // 2) env var (support uppercase with dots replaced by underscores)
        String envKey = key.replace('.', '_').toUpperCase();
        val = System.getenv(envKey);
        if (val != null && !val.isEmpty()) return val;

        // 3) yaml
        Object yamlVal = lookupYaml(key);
        if (yamlVal != null) return yamlVal.toString();

        // 4) not found
        return null;
    }

    public static Boolean getBoolean(String key) {
        String v = get(key);
        if (v == null) return null;
        return Boolean.parseBoolean(v);
    }
}
