public class DecryptClass {

    public DecryptClass(){

    }

    public Process getProcess(Method method) {
        switch (method){
            case EC: return new EcProcess();
            case RSA: return new RsaProcess();
            case NCRYPT: return new NcryptProcess();
            default: throw new IllegalArgumentException("Not Supported method: " + method);
        }
    }
}
