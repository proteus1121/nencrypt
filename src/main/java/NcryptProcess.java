import net.sf.ntru.encrypt.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;

public class NcryptProcess implements Process {
    NtruEncrypt ntru = new NtruEncrypt(EncryptionParameters.EES1087EP2);

    @Override
    public Pair<String, String> getKeys() {
        EncryptionKeyPair encryptionKeyPair = ntru.generateKeyPair();
        EncryptionPrivateKey aPrivate = encryptionKeyPair.getPrivate();
        EncryptionPublicKey aPublic = encryptionKeyPair.getPublic();

        String privateA = Hex.encodeHexString(aPrivate.getEncoded());
        String publicA = Hex.encodeHexString(aPublic.getEncoded());
        return Pair.of(privateA, publicA);
    }

    @Override
    public String encrypt(String msg, String key) {
        try {
           return Hex.encodeHexString(ntru.encrypt(msg.getBytes(), new EncryptionPublicKey(Hex.decodeHex(key))));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    @Override
    public String decrypt(String msg, String key) {
        return null;
    }
}
