package org.example;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        ByteParser parser = new ByteParser("C:\\Users\\Niklas\\Desktop\\Niklas\\IT Security\\Assignments\\ITSecurity\\Assignment_1\\databases\\databases\\Assmannn.kdbx");
        for (int password = 0; password < 10000; password++) {
            byte[] key = KeyGenerator.generateKey(parser.getMasterSeed(), parser.getTransformSeed(), parser.getTransformRounds(), parser.encryIV, String.valueOf(password));
            boolean result = Decrypter.decryptContent(parser.getContent(), key, parser.getEncryIV(), parser.getStreamStartBytes());

            if (result){
                System.out.println(result);
            }

        }
//        byte[] test;
//        MessageDigest dig = MessageDigest.getInstance("SHA256");
//        ByteBuffer buffer = ByteBuffer.allocate(4);
//        String s = "5441";
//        buffer.putInt(5441);
//        BigInteger bigInt = BigInteger.valueOf(5441);
//        byte[] c = bigInt.toByteArray();
//        System.out.print(Arrays.toString(dig.digest(c)));
    }
}