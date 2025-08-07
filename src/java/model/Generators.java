package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class Generators {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
//    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:\",.<>?/";    

    private static String generateRandomAlphanumeric(int length) {
        String alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphanumericChars.charAt(random.nextInt(alphanumericChars.length())));
        }
        return sb.toString();
    }

    public static String generateRandomCharachers(int length, boolean useUppercase, boolean useNumbers, boolean useSymbols) {
        StringBuilder characterSet = new StringBuilder(LOWERCASE);
        if (useUppercase) {
            characterSet.append(UPPERCASE);
        }
        if (useNumbers) {
            characterSet.append(NUMBERS);
        }
        if (useSymbols) {
            characterSet.append(SYMBOLS);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(index));
        }
        return password.toString();
    }
}