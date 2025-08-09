package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class Validations {

    public boolean isEmpty(String string) {
        return string != null && string.isEmpty();
    }

    public static boolean isDouble(String value) {
        return value.matches("^\\d+(\\.\\d{2})?$");
    }

    public boolean charLen(String input, int value) {
        return input.length() <= value;
    }

    public boolean confirmPassword(String pass, String confPass) {
//        return pass == confPass;
        return pass.equals(confPass);
    }

    public boolean validateMobileLen(String mobile) {
        return mobile.length() != 10;
    }

    public boolean validateMobile(String mobile) {
        return !mobile.matches("^07[01245678]{1}[0-9]{7}$");
    }

    public boolean validateEmail(String email) {
        return !email.matches("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-]" //email regex verification for a valid format
                + "[A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$");
    }

    public boolean validatePassword(String password) {
        return !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    public boolean validateComboBox(String selection) {
        return selection.equals("Select");
    }

    public String calculatePasswordStrength(String password) {
        int score = 0;

        if (password.length() >= 8) {
            score++;
        }
        if (password.matches(".*[A-Z].*")) {
            score++;
        }
        if (password.matches(".*[a-z].*")) {
            score++;
        }
        if (password.matches(".*[0-9].*")) {
            score++;
        }
        if (password.matches(".*[!@#$%^&*()].*")) {
            score++;
        }

        switch (score) {
            case 1:
            case 2:
                return "Weak";
            case 3:
                return "Moderate";
            case 4:
                return "Strong";
            case 5:
                return "Very Strong";
            default:
                return "Very Weak";
        }
    }

    public static String checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return "Very weak"; // Empty password is very weak
        }

        int length = password.length();
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

//        String specialChars = "!@#$%^&*()_+=-`~[]\{}|;':\",./<>?"; // Define special characters
        String specialChars = "!@#$%^&*()_+=-`~[]{}|;':\",./<>?"; // No backslash before the curly brace

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.contains(String.valueOf(c))) { // Check if it's a defined special character
                hasSpecialChar = true;
            }
        }

        int strengthScore = 0;

        if (length >= 8) {
            strengthScore++; // Length bonus for 8+ characters
        }
        if (length >= 12) {
            strengthScore++; // Length bonus for 12+ characters
        }
        if (hasUpperCase) {
            strengthScore++;
        }
        if (hasLowerCase) {
            strengthScore++;
        }
        if (hasDigit) {
            strengthScore++;
        }
        if (hasSpecialChar) {
            strengthScore++;
        }

        if (length < 6) {
            return "Very Weak";
        } else if (strengthScore <= 2) {
            return "Weak";
        } else if (strengthScore <= 4) {
            return "Moderate";
        } else if (strengthScore <= 5) {
            return "Strong";
        } else {
            return "Very Strong";
        }
    }

    public static boolean isInteger(String value) {
        return value.matches("^\\d+$");
    }
}
