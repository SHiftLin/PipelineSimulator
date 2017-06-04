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
public class getString {

    public static String showCode() {
        String res = "[";
        for (int i = 0, k = 0; i < codeLen;) {
            byte icode = Convert.getHigh(memory[i]);
            byte ifun = Convert.getLow(memory[i]);
            String instr = getString.Instr(icode, ifun);
            String operand = getString.Operand(icode, i);
            k = offset[icode];
            res += "[" + Convert.ByteArray2String(i, k) + ":" + instr + operand + "]";
            i += k;
            if (i < codeLen) {
                res += "#";
            }
        }
        res += "]";
        return res;
    }

    public static String Register(byte r) {
        switch (r) {
            case 0:
                return "%eax";
            case 1:
                return "%ecx";
            case 2:
                return "%edx";
            case 3:
                return "%ebx";
            case 4:
                return "%esp";
            case 5:
                return "%ebp";
            case 6:
                return "%esi";
            case 7:
                return "%edi";
        }
        return "error";
    }

    public static String InstrMove(byte ifun) {
        switch (ifun) {
            case 0:
                return "rrmovl ";
            case 1:
                return "cmovle ";
            case 2:
                return "cmovl ";
            case 3:
                return "cmove ";
            case 4:
                return "cmovne ";
            case 5:
                return "cmovge ";
            case 6:
                return "cmovg ";
        }
        return "error";
    }

    public static String InstrOpl(byte ifun) {
        switch (ifun) {
            case 0:
                return "addl ";
            case 1:
                return "subl ";
            case 2:
                return "andl ";
            case 3:
                return "xorl ";
        }
        return "error";
    }

    public static String InstrJmp(byte ifun) {
        switch (ifun) {
            case 0:
                return "jmp ";
            case 1:
                return "jle ";
            case 2:
                return "jl ";
            case 3:
                return "je ";
            case 4:
                return "jne ";
            case 5:
                return "jge ";
            case 6:
                return "jg ";
        }
        return "error";
    }

    public static String Instr(byte icode, byte ifun) {
        switch (icode) {
            case 0:
                return "halt";
            case 1:
                return "nop";
            case 2:
                return InstrMove(ifun);
            case 3:
                return "irmovl ";
            case 4:
                return "rmmovl ";
            case 5:
                return "mrmovl ";
            case 6:
                return InstrOpl(ifun);
            case 7:
                return InstrJmp(ifun);
            case 8:
                return "call ";
            case 9:
                return "ret";
            case 0xa:
                return "pushl ";
            case 0xb:
                return "popl ";
        }
        return "error";
    }

    public static String Operand(byte icode, int addr) // return instrucion operand as string
    {
        String rA = Register(Convert.getHigh(memory[addr + 1]));
        String rB = Register(Convert.getLow(memory[addr + 1]));

        String valc = Integer.toString(Convert.ByteArray2Int(memory, addr + 2));
        String vald = Integer.toString(Convert.ByteArray2Int(memory, addr + 1));

        switch (icode) {
            case 0:
                return "";
            case 1:
                return "";
            case 2:
                return rA + ", " + rB;
            case 3:
                return "$" + valc + ", " + rB;
            case 4:
                return rA + ", " + "$" + valc + "(" + rB + ")";
            case 5:
                return "$" + valc + "(" + rB + "), " + rA;
            case 6:
                return rA + ", " + rB;
            case 7:
                return vald;
            case 8:
                return vald;
            case 9:
                return "";
            case 10:
                return rA;
            case 11:
                return rA;
        }
        return "error";
    }
}
