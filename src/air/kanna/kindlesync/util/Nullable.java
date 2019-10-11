package air.kanna.kindlesync.util;

public class Nullable {

    public static boolean isNull(String str) {
        return str == null || str.length() <= 0;
    }
    
    public static boolean isNull(byte[] bytes) {
        return bytes == null || bytes.length <= 0;
    }
}
