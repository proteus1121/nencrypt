import org.apache.commons.lang3.tuple.Pair;

import java.security.PublicKey;

public interface Process {
    Pair<String, String> getKeys();
    String encrypt(String msg, String key);
    String decrypt(String msg, String key);

}
