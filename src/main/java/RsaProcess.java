import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaProcess implements Process
{
  @Override
  public Pair<String, String> getKeys()
  {
    final int keySize = 2048;
    KeyPairGenerator keyPairGenerator;
    try
    {
      keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(keySize);
      KeyPair keyPair = keyPairGenerator.genKeyPair();

      PublicKey pubKey = keyPair.getPublic();
      PrivateKey privateKey = keyPair.getPrivate();
      return Pair.of(Hex.encodeHexString(privateKey.getEncoded()), Hex.encodeHexString(pubKey.getEncoded()));
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String encrypt(String msg, String key)
  {
    Cipher cipher;

    try
    {
      cipher = Cipher.getInstance("RSA");
      PublicKey rsa = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Hex.decodeHex(key)));
      cipher.init(Cipher.ENCRYPT_MODE, rsa);
      return Hex.encodeHexString(cipher.doFinal(msg.getBytes()));
    }
    catch (DecoderException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String decrypt(String msg, String privateKey, String publicKey)
  {
    Cipher cipher;

    try
    {
      cipher = Cipher.getInstance("RSA");
      PrivateKey rsa = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Hex.decodeHex(privateKey)));
      cipher.init(Cipher.DECRYPT_MODE, rsa);
      return new String(cipher.doFinal(Hex.decodeHex(msg)));
    }
    catch (DecoderException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
