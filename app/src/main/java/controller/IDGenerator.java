package controller;

import java.security.SecureRandom;

/**
 * Generate random Strings for ID
 *
 * Created by Ka Kyen Lam on 3/09/2017.
 */

public class IDGenerator {

    private static final int ID_LENGTH = 6;

    /**
     * Generates random Strings with set length as ID
     *
     * @return random String
     */
    public static String idGen() {
        String charsUsed = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rand = new SecureRandom();
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++)
            sb.append(charsUsed.charAt(rand.nextInt(charsUsed.length())));
        return sb.toString();
    }
}
