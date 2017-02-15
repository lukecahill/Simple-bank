package com.lukecahill;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static Log log = LogFactory.getLog(Bank.class);
    private static final String FAILED_TO_ENCRYPT = "Failed to encrypt user password.";
    private static final String COULD_NOT_VERIFY = "Could not verify password.";

    public static String encryptPassword(String customerPassword) {
        MessageDigest md = null;
        try {
            // not secure, but this isn't a real bank
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(COULD_NOT_VERIFY);
            log.error(e.getMessage() + COULD_NOT_VERIFY);
        }

        try {
            md.update(customerPassword.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println(FAILED_TO_ENCRYPT);
            log.error(e.getMessage() + FAILED_TO_ENCRYPT);
        } catch(NullPointerException e) {
            System.err.println(FAILED_TO_ENCRYPT);
            log.info(e.getMessage() + FAILED_TO_ENCRYPT);
        }

        try {
            byte raw[] = md.digest();
            customerPassword = (new BASE64Encoder()).encode(raw);
        } catch(NullPointerException e) {
            System.err.println(FAILED_TO_ENCRYPT);
            log.info(e.getMessage() + FAILED_TO_ENCRYPT);
        }

        return customerPassword;
    }
}
