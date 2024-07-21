package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.exception.AppException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryption {

    private final SecretKey secretKey;
    private final GCMParameterSpec gcmParameterSpec;

    public Encryption(String secret) throws AppException {
        secretKey = generateKey(secret);
        gcmParameterSpec = new GCMParameterSpec(128, secret.getBytes());
    }

    public String encrypt(String data) throws AppException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public String decrypt(String encryptedData) throws AppException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private SecretKey generateKey(String secret) throws AppException {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(secret.toCharArray(), secret.getBytes(), 10000, 128);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey intermediateKey = keyFactory.generateSecret(keySpec);
            return new SecretKeySpec(intermediateKey.getEncoded(), "AES");
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

}
