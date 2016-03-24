package by.bsuir.hockeyshop.util;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    final static Logger LOG = Logger.getLogger(Hasher.class);
    public static String md5Hash(String value) {
        String valueHash = null;
        if (value != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(value.getBytes());
                byte[] bytes = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : bytes) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                valueHash = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                LOG.error("Error in hashing value " + value);
            }
        }
        return valueHash;
    }
}
