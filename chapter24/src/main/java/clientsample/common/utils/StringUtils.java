package clientsample.common.utils;

public class StringUtils {
    private StringUtils() {}

    public static void shouldNotBlank(String value, String message){
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void shouldNotLessThen(String value, int threshold, String message) {
        if (value == null || value.length() < threshold) {
            throw new IllegalArgumentException(message);
        }
    }
}
