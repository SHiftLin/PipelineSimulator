/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver.stage;

import static pipelineserver.stage.Var.*;

/**
 *
 * @author lsh
 */
public class Decode extends Thread {

    @Override
    public void run() {
        d_code = D_code;
        d_stat = D_stat;
        d_icode = D_icode;
        d_ifun = D_ifun;
        d_rA = D_rA;
        d_rB = D_rB;
        d_valC = D_valC;
        d_valP = D_valP;

        if (d_stat != Stats.SAOK || d_icode == Instrs.INOP) {
            d_valA = 0;
            d_valB = 0;
            d_srcA = 0xf;
            d_srcB = 0xf;
            d_dstE = 0xf;
            d_dstM = 0xf;
            return;
        }

        switch (d_icode) {
            case IRRMOVL:
            case IRMMOVL:
            case IOPL:
            case IPUSHL:
                d_srcA = d_rA;
                break;
            case IRET:
            case IPOPL:
                d_srcA = 4;
                break;
            default:
                d_srcA = 0xf;
                break;
        }
        switch (d_icode) {
            case IRMMOVL:
            case IMRMOVL:
            case IOPL:
                d_srcB = d_rB;
                break;
            case ICALL:
            case IRET:
            case IPUSHL:
            case IPOPL:
                d_srcB = 4;
                break;
            default:
                d_srcB = 0xf;
                break;
        }

        switch (d_icode) {
            case IRRMOVL:
            case IIRMOVL:
            case IOPL:
                d_dstE = d_rB;
                break;
            case ICALL:
            case IRET:
            case IPUSHL:
            case IPOPL:
                d_dstE = 4;
                break;
            default:
                d_dstE = 0xf;
                break;
        }
        switch (d_icode) {
            case IMRMOVL:
            case IPOPL:
                d_dstM = d_rA;
                break;
            default:
                d_dstM = 0xf;
                break;
        }

        int d_rvalA = (d_srcA != 0xf) ? register[d_srcA] : 0;
        int d_rvalB = (d_srcB != 0xf) ? register[d_srcB] : 0;

        if (d_icode == Instrs.ICALL || d_icode == Instrs.IJXX) {
            d_valA = d_valP;
        } else if (d_srcA == e_dstE) {
            d_valA = e_valE;
        } else if (d_srcA == M_dstM) {
            d_valA = m_valM;
        } else if (d_srcA == M_dstE) {
            d_valA = M_valE;
        } else if (d_srcA == W_dstM) {
            d_valA = W_valM;
        } else if (d_srcA == W_dstE) {
            d_valA = W_valE;
        } else {
            d_valA = d_rvalA;
        }

        if (d_srcB == e_dstE) {
            d_valB = e_valE;
        } else if (d_srcB == M_dstM) {
            d_valB = m_valM;
        } else if (d_srcB == M_dstE) {
            d_valB = M_valE;
        } else if (d_srcB == W_dstM) {
            d_valB = W_valM;
        } else if (d_srcB == W_dstE) {
            d_valB = W_valE;
        } else {
            d_valB = d_rvalB;
        }
    }
}
