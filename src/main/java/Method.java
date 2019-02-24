public enum Method{
    RSA("RSA"),
    EC("EC"),
    NCRYPT("NCRYPT");

    String method;
    Method(String method) {
        this.method = method;
    }

    String getMethod(){
        return method;
    }
}