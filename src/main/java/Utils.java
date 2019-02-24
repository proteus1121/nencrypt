public class Utils
{
    private Utils(){
        //util class, creating instance is forbidden
    }

    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b: data) {
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }
}
