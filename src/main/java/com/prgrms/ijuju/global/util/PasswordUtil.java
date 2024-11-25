package com.prgrms.ijuju.global.util;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    public static String generateTempPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder tempPassword = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHAR_SET.length());
            tempPassword.append(CHAR_SET.charAt(index));
        }
        return tempPassword.toString();
    }
}
