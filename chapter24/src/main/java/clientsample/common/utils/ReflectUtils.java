package clientsample.common.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

public final class ReflectUtils {
    private ReflectUtils() {
    }

    public static void forceSet(Object theObj, String fieldName, Object fieldValue) {
        try {
            FieldUtils.writeField(theObj, fieldName, fieldValue, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("编程错误！");
        }
    }
}
