/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver;

import static pipelineserver.stage.Var.*;

/**
 *
 * @author lsh
 */
public class Control {

    public static void stall_bubble() {
        if ((E_icode == Instrs.IMRMOVL || E_icode == Instrs.IPOPL) && (E_dstM == d_srcA || E_dstM == d_srcB)) {
            D_stall = 1;
        } else {
            D_stall = 0;
        }

        if ((E_icode == Instrs.IMRMOVL || E_icode == Instrs.IPOPL) && (E_dstM == d_srcA || E_dstM == d_srcB)
                || (D_icode == Instrs.IRET || E_icode == Instrs.IRET || M_icode == Instrs.IRET)) {
            F_stall = 1;
        } else {
            F_stall = 0;
        }

        if (m_stat != Stats.SAOK || W_stat != Stats.SAOK) {
            M_bubble = 1;
        } else {
            M_bubble = 0;
        }

        if ((E_icode == Instrs.IJXX && e_Cnd == 0)
                || (E_icode == Instrs.IMRMOVL || E_icode == Instrs.IPOPL) && (E_dstM == d_srcA || E_dstM == d_srcB)) {
            E_bubble = 1;
        } else {
            E_bubble = 0;
        }

        if ((E_icode == Instrs.IJXX && e_Cnd == 0)
                || !((E_icode == Instrs.IMRMOVL || E_icode == Instrs.IPOPL) && (E_dstM == d_srcA || E_dstM == d_srcB))
                && (D_icode == Instrs.IRET || E_icode == Instrs.IRET || M_icode == Instrs.IRET)) {
            D_bubble = 1;
        } else {
            D_bubble = 0;
        }
    }

    public static void updateRegisters() {
        if (F_stall == 0) {
            F_predPC = f_predPC;
        }

        if (D_bubble == 1) {
            D_code = "bubble";
            D_stat = Stats.SAOK;
            D_icode = Instrs.INOP;
            D_ifun = 0;
            D_rA = 0xf;
            D_rB = 0xf;
            D_valC = 0;
            D_valP = 0;
        } else if (D_stall == 0) {
            D_code = f_code;
            D_stat = f_stat;
            D_icode = f_icode;
            D_ifun = f_ifun;
            D_rA = f_rA;
            D_rB = f_rB;
            D_valC = f_valC;
            D_valP = f_valP;
        }

        if (E_bubble == 1) {
            E_code = "bubble";
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
        } else {
            E_code = d_code;
            E_stat = d_stat;
            E_icode = d_icode;
            E_ifun = d_ifun;
            E_valC = d_valC;
            E_valA = d_valA;
            E_valB = d_valB;
            E_dstE = d_dstE;
            E_dstM = d_dstM;
            E_srcA = d_srcA;
            E_srcB = d_srcB;
        }

        if (M_bubble == 1) {
            M_code = "bubble";
            M_stat = Stats.SAOK;
            M_icode = Instrs.INOP;
            M_Cnd = 0;
            M_valE = 0;
            M_valA = 0;
            M_dstE = 0xf;
            M_dstM = 0xf;
        } else {
            M_code = e_code;
            M_stat = e_stat;
            M_icode = e_icode;
            M_Cnd = e_Cnd;
            M_valE = e_valE;
            M_valA = e_valA;
            M_dstE = e_dstE;
            M_dstM = e_dstM;
        }

        W_code = m_code;
        W_stat = m_stat;
        W_icode = m_icode;
        W_valE = m_valE;
        W_valM = m_valM;
        W_dstE = m_dstE;
        W_dstM = m_dstM;
    }
}
