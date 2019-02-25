import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class EcProcess implements Process
{
  @Override
  public Pair<String, String> getKeys()
  {
    Security.addProvider(new BouncyCastleProvider());

    java.security.KeyPairGenerator ecKeyGen;
    try
    {
      ecKeyGen = KeyPairGenerator.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
      ecKeyGen.initialize(new ECGenParameterSpec("secp256r1"));
      KeyPair ecKeyPair = ecKeyGen.generateKeyPair();
      String aPrivate = Hex.encodeHexString(ecKeyPair.getPrivate().getEncoded());
      String aPublic = Hex.encodeHexString(ecKeyPair.getPublic().getEncoded());
      return Pair.of(aPrivate, aPublic);
    }
    catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchProviderException e)
    {
      e.printStackTrace();
    }
    return null;
  }

  // doesn't work, which means we are dancing on the leading edge :)
  //        // KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC");
  //        // ecKeyGen.initialize(new ECGenParameterSpec("secp384r1"));
  //
  //        System.out.println("What is slow?");
  //
  //        Cipher iesCipher = Cipher.getInstance("ECIESwithAES");
  //        iesCipher.init(Cipher.ENCRYPT_MODE, ecKeyPair.getPublic());
  //
  //        byte[] ciphertext = iesCipher.doFinal(com.google.common.base.Strings.repeat("owlstead", 1000).getBytes());
  //
  //        iesCipher.init(Cipher.DECRYPT_MODE, ecKeyPair.getPrivate());
  //        byte[] plaintext = iesCipher.doFinal(ciphertext);
  //
  //        System.out.println(Hex.toHexString(ciphertext));
  //        System.out.println(new String(plaintext));

  @Override
  public String encrypt(String msg, String key)
  {
    try{
    // add instance of provider class
    Security.addProvider(new BouncyCastleProvider());

    String name = "secp256r1";

    Cipher iesCipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
    PublicKey rsa = KeyFactory.getInstance("ECIES").generatePublic(new X509EncodedKeySpec(Hex.decodeHex(key)));

    iesCipher.init(Cipher.ENCRYPT_MODE, rsa);
      return Hex.encodeHexString(iesCipher.doFinal(msg.getBytes()));
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchProviderException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchPaddingException e)
    {
      e.printStackTrace();
    }
    catch (InvalidKeyException e)
    {
      e.printStackTrace();
    }
    catch (BadPaddingException e)
    {
      e.printStackTrace();
    }
    catch (IllegalBlockSizeException e)
    {
      e.printStackTrace();
    }
    catch (InvalidKeySpecException e)
    {
      e.printStackTrace();
    }
    catch (DecoderException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public String decrypt(String msg, String privateKey, String publicKey)
  {
    return null;
  }
}
