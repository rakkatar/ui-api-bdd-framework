package pl.recruitment.qa.ui.context;

import java.util.HashMap;
import java.util.Map;

public final class ScenarioContext {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = ThreadLocal.withInitial(HashMap::new);

    private ScenarioContext() {
    }

    public static void put(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    public static String getString(String key) {
        Object value = CONTEXT.get().get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
