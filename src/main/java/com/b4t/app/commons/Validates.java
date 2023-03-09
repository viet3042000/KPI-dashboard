package com.b4t.app.commons;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Validates {

    private Validates() {
        throw new IllegalStateException( "Validates class" );
    }

    public static boolean isEmpty(String obj) {
        return StringUtils.isEmpty( obj );
    }

    public static boolean checkLength(String obj, int min, int max) {
        return obj.length() > min && obj.length() < max;
    }

    public static boolean checkLengthMax(String obj, int min) {
        return obj.length() > min;
    }

    public static boolean checkResetPass(String oldPass, String newPass) {
        return oldPass.equals( newPass );
    }

    public static Boolean changePassword(String encryptedPassword, String unencryptedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        CharSequence s = unencryptedPassword;
        return passwordEncoder.matches( s, encryptedPassword );
    }

    public static boolean checkFormatEmail(String email) {
        return email.matches( "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$" );
    }
}
