/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver.utility;

import static pipelineserver.stage.Var.*;

/**
 *
 * @author lsh
 */
public class Convert {

    public static byte getLow(byte x) {
        return (byte) (x & 0xf);
    }

    public static byte getHigh(byte x) {
        return (byte) ((x >> 4) & 0xf);
    }

    public static String Hex2String(byte x) {
        x &= 0xf;
        switch (x) {
            case 0:
                return "0";
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "a";
            case 11:
                return "b";
            case 12:
                return "c";
            case 13:
                return "d";
            case 14:
                return "e";
            case 15:
                return "f";
        }
        return "error";
    }

    public static String Byte2String(byte x) {
        String low = Hex2String(getLow(x));
        String high = Hex2String(getHigh(x));
        return high + low;
    }

    public static int ByteArray2Int(byte[] array, int addr) {
        int res = 0;
        for (int i = 0; i < 4 && addr + i < MEMORY_SIZE; i++) {
            res += array[addr + i] << (i * 8);
        }
        return res;
    }

    public static String ByteArray2String(int addr, int len) {
        String res = "";
        for (int i = 0; i < len && addr + i < MEMORY_SIZE; i++) {
            res += Byte2String(memory[addr + i]);
        }
        return res;
    }
}
