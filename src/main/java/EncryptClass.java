public class EncryptClass {

    public EncryptClass() {
    }

    public Process getProcess(Method method) {
        switch (method){
            case EC: return new EcProcess();
            case NCRYPT: return new NcryptProcess();
                default: throw new IllegalArgumentException("Not Supported method: " + method);
        }
    }
}
