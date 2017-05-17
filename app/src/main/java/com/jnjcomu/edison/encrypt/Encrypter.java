package com.jnjcomu.edison.encrypt;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author kimwoojae <wj1187@naver.com>
 * @author coderi13 <ruto1924@gmail.com>
 * @since 2017-04-05
 *
 * TODO 코드 완벽하게 이해하기
 */

public class Encrypter {
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String ENCRYPT_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ENCODING = "UTF-8";

    private Context context;
    private String keyIV;

    public Encrypter(Context context) {
        this.context = context;
    }

    /**
     *
     * @return secretKey
     */
    @Nullable
    public SecretKey fetchSignature() {
        SecretKey secretKey = null;

        try {
            Signature signature = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES
            ).signatures[0];

            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(signature.toByteArray());

            keyIV = signature.toCharsString().substring(0, 16);
            secretKey = new SecretKeySpec(md.digest(), "AES");
        } catch (Exception ignored) {
        }

        return secretKey;
    }

    /**
     *
     * @param plainText String
     * @return encrypted
     */
    @NonNull
    public String encrypt(String plainText) {
        Cipher cipher;
        IvParameterSpec ivParameterSpec;
        byte[] encryptedData;
        String encrypted;
        SecretKey secretKey;

        try {
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            secretKey = fetchSignature();
            ivParameterSpec = new IvParameterSpec(keyIV.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            encryptedData = cipher.doFinal(plainText.getBytes(ENCODING));
            encrypted = Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception ignored) {
            encrypted = "";
        }

        return encrypted;
    }

    /**
     *
     * @param encrypted String
     * @return decrypted
     */
    @NonNull
    public String decrypt(String encrypted) {
        Cipher cipher;
        IvParameterSpec ivParameterSpec;
        String decrypted;
        SecretKey secretKey;

        try {
            byte[] data = Base64.decode(encrypted, Base64.DEFAULT);

            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            secretKey = fetchSignature();
            ivParameterSpec = new IvParameterSpec(keyIV.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            decrypted = new String(cipher.doFinal(data), ENCODING);
        } catch (Exception ignored) {
            decrypted = "";
        }

        return decrypted;
    }
}