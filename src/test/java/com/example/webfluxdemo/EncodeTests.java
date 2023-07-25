package com.example.webfluxdemo;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Logger;

class EncodeTests {

    private final String message = "Hello World!";
    private final Logger logger = Logger.getLogger(EncodeTests.class.getName());
    private final Base64.Encoder encoder = Base64.getEncoder();

    @Test
    void urlEncodeTest() {
        String encode = URLEncoder.encode(message, StandardCharsets.UTF_8);
        logger.info(encode);
    }

    @Test
    void hashTest() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        String salt = String.valueOf(new Random().nextInt(1000));
        logger.info(salt);

        md5.update(salt.getBytes());
        md5.update(message.getBytes());
        logger.info(encoder.encodeToString(md5.digest()));
    }

    @Test
    void hmacTest() throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");

        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        logger.info(encoder.encodeToString(key));

        Mac hmacMD5 = Mac.getInstance("HmacMD5");
        hmacMD5.init(secretKey);
        hmacMD5.update(message.getBytes());
        byte[] encode = hmacMD5.doFinal();
        logger.info(encoder.encodeToString(encode));
    }
}
