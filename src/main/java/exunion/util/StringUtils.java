package exunion.util;

/**
 * String Utils
 */
public class StringUtils {

    public static boolean isEmpty(String value) {
        return isEmpty((CharSequence) value);
    }

    public static boolean isEmpty(CharSequence value) {
        if (value == null || value.length() == 0) {
            return true;
        }

        return false;
    }

}
