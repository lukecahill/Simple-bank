package com.lukecahill;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class EncryptPasswords {

    public static String encryptPassword(String customerPassword) {
        MessageDigest md = null;
        try {
            // not secure, but this isn't a real bank
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not verify password.");
        }

        try {
            md.update(customerPassword.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Could not verify password.");
        }

        byte raw[] = md.digest();
        customerPassword = (new BASE64Encoder()).encode(raw);

        //System.out.println(customerPassword);
        return customerPassword;
    }
}
