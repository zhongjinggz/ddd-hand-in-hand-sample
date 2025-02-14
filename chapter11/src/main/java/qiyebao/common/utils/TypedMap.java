package qiyebao.common.utils;

import java.time.LocalDateTime;
import java.util.Map;

public class TypedMap {
    private final Map<String, Object> original;

    public TypedMap(Map<String, Object> resultMap) {
        this.original = resultMap;
    }

    public Long getLong(String key) {
        return original.get(key) instanceof Number number
                ? number.longValue()
                : null;
    }

    public String getString(String key) {
        Object value = original.get(key);
        return value != null
                ? value.toString()
                : null;
    }

    public LocalDateTime getLocalDateTime(String key) {
        return switch (original.get(key)) {
            case LocalDateTime dateTime -> dateTime;
            case java.sql.Timestamp timestamp -> timestamp.toLocalDateTime();
            default -> null;
        };
    }
}