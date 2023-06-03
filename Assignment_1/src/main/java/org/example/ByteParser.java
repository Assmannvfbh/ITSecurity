package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ByteParser {
    byte[] masterSeed;
    byte[] transformSeed;
    byte[] transformRounds;
    byte[] encryIV;
    byte[] streamStartBytes;
    byte[] endOfHeader;
    byte[] content;
    int[] intValues;
    byte[] data;
    int pointer;

    public ByteParser(String path) {
        try {
            File file = new File(path);
            FileInputStream fl = new FileInputStream(file);
            data = new byte[(int)file.length()];
            fl.read(data);
            fl.close();
            //this.data = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.pointer = 0;
        intValues = this.fillUpUsgInt(data);
        findKeyCredentials();
    }

    public int[] fillUpUsgInt(byte[] data){
        int[] intArray = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            intArray[i] = Byte.toUnsignedInt(data[i]);
        }
        return intArray;
    }

    public void findKeyCredentials(){
        //skip first 12 bytes
        pointer = pointer + 12;
        int length;
        int i;
        //find credentials from headers
        boolean b = true;
        while (b){
            switch(intValues[pointer]){
                case 4: length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        i = 0;
                    this.masterSeed = new byte[length];
                        for(int p = pointer; p < (pointer + length); p++ ){
                            masterSeed[i] = data[p];
                            i++;
                        }
                        pointer = pointer + length;
                        break;
                case 5: length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        i = 0;
                        this.transformSeed = new byte[length];
                        for(int p = pointer; p < (pointer + length); p++ ){
                            transformSeed[i] = data[p];
                            i++;
                        }
                        pointer = pointer + length;
                        break;
                case 6: length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        i = 0;
                        this.transformRounds = new byte[length];
                        for(int p = pointer; p < (pointer + length); p++ ){
                            transformRounds[i] = data[p];
                            i++;
                        }
                        pointer = pointer + length;
                        break;
                case 7: length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        i = 0;
                        this.encryIV = new byte[length];
                        for(int p = pointer; p < (pointer + length); p++ ){
                            encryIV[i] = data[p];
                            i++;
                        }
                        pointer = pointer + length;
                        break;
                case 9: length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        i = 0;
                        this.streamStartBytes = new byte[length];
                        for(int p = pointer; p < (pointer + length); p++ ){
                            streamStartBytes[i] = data[p];
                            i++;
                        }
                        pointer = pointer + length;
                        break;
                case 0: length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        i = 0;
                        this.endOfHeader = new byte[length];
                        for(int p = pointer; p < (pointer + length); p++ ){
                            endOfHeader[i] = data[p];
                            i++;
                        }
                        pointer = pointer + length;
                        b = false;
                        break;
                default:length = getDataLength(data[pointer + 1], data[pointer + 2]);
                        pointer = pointer + 3;
                        pointer = pointer + length;
                        break;
            }

        }
        //get encrypted content
        int k = 0;
        this.content = new byte[intValues.length - pointer];
        for(int j = pointer; j < intValues.length; j++) {
            content[k] = data[j];
            System.out.println(intValues[j]);
            k++;
        }
        //get corrct byte order, because of little endian
        this.masterSeed = switchByteOrder(this.masterSeed);
        this.transformSeed = switchByteOrder(this.transformSeed);
        ////this.transformRounds = switchByteOrder(this.transformRounds);
        this.encryIV = switchByteOrder(this.encryIV);
        this.streamStartBytes = switchByteOrder(this.streamStartBytes);
        this.content = switchByteOrder(this.content);
    }

    public short getDataLength(byte b1, byte b2){
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{b1,b2});
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }

    public byte[] switchByteOrder(byte[] array){
        byte[] temp = new byte[array.length];
        for(int i = 0; i < array.length; i++){
            temp[i] = array[(array.length - 1) - i];
        }
        return temp;
    }

    public byte[] getMasterSeed() {
        return masterSeed;
    }

    public byte[] getTransformSeed() {
        return transformSeed;
    }

    public byte[] getTransformRounds() {
        return transformRounds;
    }

    public byte[] getEncryIV() {
        return encryIV;
    }

    public byte[] getStreamStartBytes() {
        return streamStartBytes;
    }

    public byte[] getEndOfHeader() {
        return endOfHeader;
    }

    public byte[] getContent() {
        return content;
    }
}
