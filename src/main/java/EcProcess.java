import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.security.SecureRandom;

public class EcProcess implements Process {
    @Override
    public Pair<String, String> getKeys() {
        X9ECParameters ecp = SECNamedCurves.getByOID(SECObjectIdentifiers.secp112r1);
        ECDomainParameters domainParams = new ECDomainParameters(ecp.getCurve(),
                ecp.getG(), ecp.getN(), ecp.getH(),
                ecp.getSeed());

        // Generate a private key and a public key
        AsymmetricCipherKeyPair keyPair;
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParams, new SecureRandom());
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(keyGenParams);
        keyPair = generator.generateKeyPair();

        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        String privateKeyHex = Hex.encodeHexString(privateKey.getD().toByteArray());
        String publicKeyHex = Hex.encodeHexString(publicKey.getQ().getEncoded());
//         --- skip check
//        // Then calculate the public key only using domainParams.getG() and private key
//        ECPoint Q = domainParams.getG().multiply(new BigInteger(privateKeyBytes));
//        System.out.println("Calculated public key: " + toHex(Q.getEncoded()));
//
//        // The calculated public key and generated public key should always match
//        if (!toHex(publicKey.getQ().getEncoded()).equals(toHex(Q.getEncoded()))) {
//            System.out.println("ERROR: Public keys do not match!");
//        } else {
//            System.out.println("Congratulations, public keys match!");
//        }
        return Pair.of(privateKeyHex, publicKeyHex);
    }

    @Override
    public String encrypt(String msg, String key) {
        return null;
    }

    @Override
    public String decrypt(String msg, String key) {
        return null;
    }
}
