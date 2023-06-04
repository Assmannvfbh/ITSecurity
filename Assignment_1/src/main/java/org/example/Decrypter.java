package org.example;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Decrypter {

    public static boolean decryptContent(byte[] content, byte[] key, byte[] encryIV, byte[] streamStartBytes){
        byte[] temp;
        byte[] first32Bytes = new byte[32];
        Cipher cipherCBC;
        SecretKey secretKey;
        try {
            cipherCBC = Cipher.getInstance("AES/CBC/NoPadding");
            secretKey = new SecretKeySpec(key, "AES");
            cipherCBC.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryIV));

            temp = cipherCBC.doFinal(content,0,32);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        System.arraycopy(temp, 0, first32Bytes, 0, 32);

        return Arrays.equals(streamStartBytes,first32Bytes);


    }
}
