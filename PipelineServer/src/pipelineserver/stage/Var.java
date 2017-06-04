/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver.stage;

/**
 *
 * @author lsh
 */
public class Var {

    public static enum Instrs {
        IHALT, INOP,
        IRRMOVL, IIRMOVL, IRMMOVL, IMRMOVL,
        IOPL, IJXX,
        ICALL, IRET, IPUSHL, IPOPL
    };

    public static enum Stats {
        SAOK, SHLT, SADR, SINS
    };

    public static int Cycle;
    public static int F_stall, F_predPC,
            f_ifun, f_rA, f_rB, f_predPC, f_valC, f_valP, f_needr, f_needvc,
            D_stall, D_bubble, D_ifun, D_rA, D_rB, D_valC, D_valP,
            d_ifun, d_rA, d_rB, d_valC, d_valP, d_valA, d_valB, d_srcA, d_srcB, d_dstE, d_dstM,
            E_bubble, E_ifun, E_valC, E_valA, E_valB, E_dstE, E_dstM, E_srcA, E_srcB,
            e_ifun, e_valC, e_valA, e_valB, e_dstE, e_dstM, e_Cnd, e_valE,
            M_bubble, M_Cnd, M_valE, M_valA, M_dstE, M_dstM,
            m_valE, m_valA, m_dstE, m_dstM, m_valM,
            W_valE, W_valM, W_dstE, W_dstM;
    public static int zf, sf, of;
    public static Stats f_stat, D_stat, d_stat, E_stat, e_stat, M_stat, m_stat, W_stat;
    public static Instrs f_icode, D_icode, d_icode, E_icode, e_icode, M_icode, m_icode, W_icode;
    public static String f_code, D_code, d_code, E_code, e_code, M_code, m_code, W_code;

    public static final int MEMORY_SIZE = 65536;
    public static final int MAX_CODELEN = 65536;
    public static byte[] memory = new byte[MEMORY_SIZE];
    public static int[] register = new int[8];

    public static int codeLen;
    public static byte[] code;
    public static int offset[] = {1, 1, 2, 6, 6, 6, 2, 5, 5, 1, 2, 2, 1, 1, 1, 1};

    public static void reset() {
        Cycle = 0;
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
        }
        for (int i = 0; i < 8; i++) {
            register[i] = 0;
        }

        F_predPC = 0;

        D_code = "";
        D_stat = Stats.SAOK;
        D_icode = Instrs.INOP;
        D_ifun = 0;
        D_rA = 0xf;
        D_rB = 0xf;
        D_valC = 0;
        D_valP = 0;

        E_code = "";
        E_stat = Stats.SAOK;
        E_icode = Instrs.INOP;
        E_ifun = 0;
        E_valC = 0;
        E_valA = 0;
        E_valB = 0;
        E_dstE = 0xf;
        E_dstM = 0xf;
        E_srcA = 0xf;
        E_srcB = 0xf;

        M_code = "";
        M_stat = Stats.SAOK;
        M_icode = Instrs.INOP;
        M_Cnd = 0;
        zf = sf = of = 0;
        M_valE = 0;
        M_valA = 0;
        M_dstE = 0xf;
        M_dstM = 0xf;

        W_code = "";
        W_stat = Stats.SAOK;
        W_icode = Instrs.INOP;
        W_valE = 0;
        W_valM = 0;
        W_dstE = 0xf;
        W_dstM = 0xf;
    }
}
