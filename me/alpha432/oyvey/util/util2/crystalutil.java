package me.alpha432.oyvey.util.util2;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class crystalutil {

    public static byte[] rawHWID() throws NoSuchAlgorithmException {
        String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS") + System.getenv("COMPUTERNAME");
        byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        return messageDigest.digest(bytes);
    }

    public static String Encrypt(String strToEncrypt, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(1, getKey(secret));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            System.out.println("Error while encrypting: " + exception.toString());
            return null;
        }
    }

    public static SecretKeySpec getKey(String myKey) {
        try {
            byte[] abyte = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");

            abyte = sha.digest(abyte);
            abyte = Arrays.copyOf(abyte, 16);
            return new SecretKeySpec(abyte, "AES");
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
    }

    public static String getEncryptedHWID(String key) {
        try {
            String s = Hashing.sha1().hashString(new String(rawHWID(), StandardCharsets.UTF_8), StandardCharsets.UTF_8).toString();
            String b = Hashing.sha256().hashString(s, StandardCharsets.UTF_8).toString();
            String c = Hashing.sha512().hashString(b, StandardCharsets.UTF_8).toString();
            String d = Hashing.sha1().hashString(c, StandardCharsets.UTF_8).toString();

            return Encrypt(d, "spartanB312" + key);
        } catch (Exception exception) {
            exception.printStackTrace();
            return "null";
        }
    }
}
