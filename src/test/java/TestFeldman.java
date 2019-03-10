import com.codahale.shamir.Scheme;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Map;

public class TestFeldman {

    @Test
    public void test(){
        Map<Integer, byte[]> integerMap = doShamirMain(2);
//        Map<Long, byte[]> longMap = doShamirCustom(2);
        System.out.println("x");
    }

    private Map<Integer, byte[]> doShamirMain(long msg) {
        final Scheme scheme = new Scheme(new SecureRandom(), 5, 3);
        final byte[] secret = ByteUtils.longToBytes(msg);
        final Map<Integer, byte[]> parts = scheme.split(secret);
        byte[] join = scheme.join(parts);
        System.out.println("hasil verifikasi: " + join);

        FeldmanVSS F = new FeldmanVSS(11, 3);
        F.setCommitments(msg, scheme.polCoeff);
        String result = F.verifyShare(shares.get(0).getKey(), shares.get(0).getValue());
        System.out.println("hasil verifikasi: " + result);
        return collect;

        return parts;
//        final byte[] recovered = scheme.join(parts);
//        System.out.println(new String(recovered, StandardCharsets.UTF_8));
    }
}
