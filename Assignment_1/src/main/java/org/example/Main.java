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
        ByteParser parser = new ByteParser("C:\\Users\\Niklas\\Desktop\\Niklas\\IT Security\\Homework1\\Assignments\\ITSecurity\\Aufgabe 2-e) Assmannn.kdbx");
        //ByteParser parser = new ByteParser("C:\\Users\\Faraz\\Documents\\Uniwork\\Sem06\\IT\\Koohiana.kdbx");
        final long timeStart = System.currentTimeMillis();
        long timeEnd;
        for (int password = 0; password < 10000; password++) {
            byte[] psw = String.format("%04d", password).getBytes();
            byte[] key = KeyGenerator.generateKey(parser.getMasterSeed(), parser.getTransformSeed(), parser.getTransformRounds(), parser.encryIV, psw);
            boolean result1 = Decrypter.decryptContent(parser.getContent(), key, parser.getEncryIV(), parser.getStreamStartBytes());
            if (result1){
                System.out.println(password);
            }

    	    /*
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(password);
            psw = buffer.array();
            key = KeyGenerator.generateKey(parser.getMasterSeed(), parser.getTransformSeed(), parser.getTransformRounds(), parser.encryIV, psw);
            boolean result2 = Decrypter.decryptContent(parser.getContent(), key, parser.getEncryIV(), parser.getStreamStartBytes());
            if (result2){
                System.out.println(password + " as Integer via ByteBuffer");
            }

            BigInteger bigInt = BigInteger.valueOf(password);
            psw = bigInt.toByteArray();
            key = KeyGenerator.generateKey(parser.getMasterSeed(), parser.getTransformSeed(), parser.getTransformRounds(), parser.encryIV, psw);
            boolean result3 = Decrypter.decryptContent(parser.getContent(), key, parser.getEncryIV(), parser.getStreamStartBytes());
            if (result3){
                System.out.println(password + " as Integer via BigInteger");
            }
            */
        }
        timeEnd = System.currentTimeMillis();
        System.out.println( " Time:" + (timeEnd - timeStart) + " Millisek.");
        
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