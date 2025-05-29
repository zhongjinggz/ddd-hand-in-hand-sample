package clientsample.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TypedMap {
    private final Map<String, Object> internal;

    public static TypedMap of(String key, Object value) {
        Map<String, Object> internal = new HashMap<>();
        internal.put(key, value);
        return new TypedMap(internal);
    }

    public TypedMap(Map<String, Object> internal) {
        this.internal = internal;
    }

    public TypedMap and(String key, Object value ) {
        internal.put(key, value);
        return this;
    }

    public Long getLong(String key) {
        return internal.get(key) instanceof Number number
                ? number.longValue()
                : null;
    }

    public Integer getInteger(String key) {
        return internal.get(key) instanceof Number number
                ? number.intValue()
                : null;
    }

    public String getString(String key) {
        Object value = internal.get(key);
        return value != null
                ? value.toString()
                : null;
    }

    public LocalDateTime getLocalDateTime(String key) {
        return switch (internal.get(key)) {
            case LocalDateTime dateTime -> dateTime;
            case java.sql.Timestamp timestamp -> timestamp.toLocalDateTime();
            default -> null;
        };
    }

    public LocalDate getLocalDate(String key) {
        return switch (internal.get(key)) {
            case LocalDate date -> date;
            case java.sql.Date date -> date.toLocalDate();
            default -> null;
        };
    }
}