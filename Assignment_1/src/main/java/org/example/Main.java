package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\Niklas\\Desktop\\Niklas\\IT Security\\Assignments\\ITSecurity\\Assignment_1\\databases\\databases\\Assmannn.kdbx");
        byte[] data;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteParser parser = new ByteParser(data);
    }
}