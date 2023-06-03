package org.example;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class KeyGenerator {



    public static byte[] generateKey(byte[] masterSeed, byte[] transformSeed, byte[]transformRounds, byte[] encryIV, String password){

        byte[] credentials;
        byte[] transformedCredentials;
        byte[] key;

        //ByteBuffer b = ByteBuffer.allocate(4);
        //ByteBuffer b = ByteBuffer.wrap(password.getBytes());
        //b.putInt(password);

//        BigInteger bigInt = BigInteger.valueOf(password);
//        byte[] c = bigInt.toByteArray();
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

        credentials = getSHA256Credentials(passwordBytes);


        transformedCredentials = getTransformedCredentials(transformSeed,transformRounds,credentials);

        key = getFinalKey(masterSeed, transformedCredentials);


        return key;

    }

    private static byte[] getSHA256Credentials(byte[] password){
        byte[] temp;
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        temp = instance.digest(password);
        //System.out.println(Arrays.toString(temp));
        //instance.reset();
        byte[] temp2 = instance.digest(temp);
        //System.out.println(Arrays.toString(temp2));
        return temp2;
    }

    private static byte[] getTransformedCredentials(byte[] transSeed, byte[] transRounds, byte[] data) {
        Cipher cipherECB;
        SecretKey secretKey;
        try {
            cipherECB = Cipher.getInstance("AES/ECB/NoPadding");
            secretKey = new SecretKeySpec(transSeed, "AES");
            cipherECB.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        //get number of transformation rounds
        ByteBuffer buffer = ByteBuffer.wrap(transRounds);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long rounds = buffer.getLong();

        //encrypt n = rounds times
        for(long i = 0; i < rounds; i++){
            try {
                data = cipherECB.doFinal(data);
                //System.out.println(Arrays.toString(data));
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    private static byte[] getFinalKey(byte[] masterSeed, byte[] transCredentials){
        byte[] temp;
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        String masterSeedString = new String(masterSeed, StandardCharsets.UTF_8);
//        String transCredentialsString = new String(transCredentials, StandardCharsets.UTF_8);
//        String finalString = masterSeedString.concat(transCredentialsString);
        //System.out.println(Arrays.toString(finalString.getBytes(StandardCharsets.UTF_8)));

        //concat byte arrays
        byte[] concatenation = new byte[masterSeed.length + transCredentials.length];
        System.arraycopy(masterSeed, 0, concatenation, 0, masterSeed.length);
        System.arraycopy(transCredentials, 0, concatenation, masterSeed.length, transCredentials.length);

        temp = instance.digest(concatenation);

        return temp;
    }

}
