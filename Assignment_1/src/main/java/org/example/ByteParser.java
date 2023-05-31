package org.example;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteParser {
    byte[] masterSeed;
    byte[] transformSeed;
    byte[] transformRounds;
    byte[] encryIV;
    byte[] streamStartBytes;

    byte[] content;
    int[] intValues;
    byte[] data;
    int pointer;

    public ByteParser(byte[] data) {
        this.pointer = 0;
        this.data = data;
        intValues = this.fillUpUsgInt(data);
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
        while (pointer < intValues.length){
            switch(intValues[pointer]){
                case 4: int length = getDataLength(data[pointer], data[pointer + 1]);
                        pointer = pointer + 2;
                        int i = 0;
                        for(int p = pointer; p < (pointer + length); p++ ){
                            masterSeed[i] = data[p];
                        }
                        pointer = pointer + length;
                        break;

            }
        }


    }

    public short getDataLength(byte b1, byte b2){
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{b1,b2});
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }
}
