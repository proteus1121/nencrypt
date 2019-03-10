
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import com.codahale.shamir.Scheme;

public class ShamirScheme {
    private final SecureRandom random;
    private final int n;
    private final int k;

    public ShamirScheme(SecureRandom random, int n, int k) {
        this.random = random;
        checkArgument(k > 1, "K must be > 1");
        checkArgument(n >= k, "N must be >= K");
        checkArgument(n <= 255, "N must be <= 255");
        this.n = n;
        this.k = k;
    }

    public Map<Integer, byte[]> split(byte[] secret) {
        byte[][] values = new byte[this.n][secret.length];

        for(int i = 0; i < secret.length; ++i) {
            byte[] p = GF256.generate(this.random, this.k - 1, secret[i]);

            for(int x = 1; x <= this.n; ++x) {
                values[x - 1][i] = GF256.eval(p, (byte)x);
            }
        }

        Map<Integer, byte[]> parts = new HashMap(this.n());

        for(int i = 0; i < values.length; ++i) {
            parts.put(i + 1, values[i]);
        }

        return Collections.unmodifiableMap(parts);
    }

    public byte[] join(Map<Integer, byte[]> parts) {
        checkArgument(parts.size() > 0, "No parts provided");
        int[] lengths = parts.values().stream().mapToInt((v) -> {
            return v.length;
        }).distinct().toArray();
        checkArgument(lengths.length == 1, "Varying lengths of part values");
        byte[] secret = new byte[lengths[0]];

        for(int i = 0; i < secret.length; ++i) {
            byte[][] points = new byte[parts.size()][2];
            int j = 0;

            for(Iterator var7 = parts.entrySet().iterator(); var7.hasNext(); ++j) {
                Map.Entry<Integer, byte[]> part = (Map.Entry)var7.next();
                points[j][0] = ((Integer)part.getKey()).byteValue();
                points[j][1] = ((byte[])part.getValue())[i];
            }

            secret[i] = GF256.interpolate(points);
        }

        return secret;
    }

    public SecureRandom random() {
        return this.random;
    }

    public int n() {
        return this.n;
    }

    public int k() {
        return this.k;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Scheme scheme = (Scheme)o;
            return this.n == scheme.n() && this.k == scheme.k();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.random, this.n, this.k});
    }

    public String toString() {
        return (new StringJoiner(", ", Scheme.class.getSimpleName() + "[", "]")).add("random=" + this.random).add("n=" + this.n).add("k=" + this.k).toString();
    }

    private static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}