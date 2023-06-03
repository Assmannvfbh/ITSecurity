package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        ByteParser parser = new ByteParser("C:\\Users\\Niklas\\Desktop\\Niklas\\IT Security\\Assignments\\ITSecurity\\Assignment_1\\databases\\databases\\Assmannn.kdbx");
        for (int password = 0; password < 1; password++) {
            byte[] key = KeyGenerator.generateKey(parser.getMasterSeed(), parser.getTransformSeed(), parser.getTransformRounds(), parser.encryIV, password);
            boolean result = Decrypter.decryptContent(parser.getContent(), key, parser.getEncryIV(), parser.getStreamStartBytes());

            if (result){
                System.out.println(result);
            }

        }

    }
}