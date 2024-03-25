import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encrypt {

    // this method generates hashes from clear text passwords
    public String generatePasswordHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 10;
        char[] passChars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(passChars, salt, iterations, 64 * 8);
        SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = key.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    // this method generates a salt to add to hashes
    private byte[] getSalt()
            throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // this method digests hashes
    private String toHex(byte[] arr)
            throws NoSuchAlgorithmException {
        BigInteger bigInt = new BigInteger(1, arr);
        String hex = bigInt.toString(16);

        int padLength = (arr.length * 2) - hex.length();

        if (padLength > 0) {
            return String.format("%0" + padLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
