package com.b4t.app.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class EncryptDecryptPwd {

//    private static final String DB_USERNAME = "mic_dashboard";
//    private static final String DB_PWD = "Mic#2020";
//
//    public static void main(String[] args) {
//        encryptKey(DB_USERNAME);
//        encryptKey(DB_PWD);
//
////        decryptKey("9Y6SF/ht5/CaU7v8o1WyQQ==");
////        decryptKey("G8MWNiqA7QJc6AIcfOL0zvje17vMGaBN");
//    }
//
//    //encrypt the plan text
//    private static void encryptKey(final String plainKey) {
//        JasyptConfig app = new JasyptConfig();
//        final SimpleStringPBEConfig pbeConfig = app.simpleStringPBEConfig();
//        pbeConfig.setPassword("Mic@2020@@###");
//        pbeConfig.setPoolSize("1");
//
//        final PooledPBEStringEncryptor pbeStringEncryptor = new PooledPBEStringEncryptor();
//
//        pbeStringEncryptor.setConfig(pbeConfig);
//
//        System.out.println("Encrypted key = " +  pbeStringEncryptor.encrypt(plainKey));
//    }
//
//    //decrypt the encrypted text
//    private static void decryptKey(final String encryptedKey) {
//        JasyptConfig app = new JasyptConfig();
//        final SimpleStringPBEConfig pbeConfig = app.simpleStringPBEConfig();
//        final PooledPBEStringEncryptor pbeStringEncryptor = new PooledPBEStringEncryptor();
//        pbeStringEncryptor.setConfig(pbeConfig);
//
//        System.out.println("Decrypted key = " + pbeStringEncryptor.decrypt(encryptedKey));
//    }
}
