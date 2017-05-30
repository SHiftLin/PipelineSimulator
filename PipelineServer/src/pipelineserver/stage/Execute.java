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
public class Execute extends Thread {

    @Override
    public void run() {
        e_code = E_code;
        e_stat = E_stat;
        e_icode = E_icode;
        e_ifun = E_ifun;
        e_valC = E_valC;
        e_valA = E_valA;
        e_valB = E_valB;

        int e_alufun = 0, e_aluA = 0, e_aluB = 0, e_setcc = 0;
        if (e_stat != Stats.SAOK || e_icode == Instrs.INOP) {
            e_dstE = 0xf;
            e_dstM = 0xf;
            e_Cnd = 0;
            e_valE = 0;
            e_alufun = 0;
            e_aluA = 0;
            e_aluB = 0;
            e_setcc = 0;
            return;
        }

        e_alufun = (e_icode == Instrs.IOPL) ? e_ifun : 0;

        switch (e_icode) {
            case IRRMOVL:
            case IOPL:
                e_aluA = e_valA;
                break;
            case IIRMOVL:
            case IRMMOVL:
            case IMRMOVL:
                e_aluA = e_valC;
                break;
            case ICALL:
            case IPUSHL:
                e_aluA = -4;
                break;
            case IRET:
            case IPOPL:
                e_aluA = 4;
                break;
        }
        switch (e_icode) {
            case IRMMOVL:
            case IMRMOVL:
            case IOPL:
            case ICALL:
            case IRET:
            case IPUSHL:
            case IPOPL:
                e_aluB = e_valB;
                break;
            case IRRMOVL:
            case IIRMOVL:
                e_aluB = 0;
                break;
        }
        switch (e_alufun) {
            case 0:
                e_valE = e_aluA + e_aluB;
                break;
            case 1:
                e_valE = e_aluB - e_aluA;
                break;
            case 2:
                e_valE = e_aluA & e_aluB;
                break;
            case 3:
                e_valE = e_aluA ^ e_aluB;
                break;
        }

        if (W_stat == Stats.SAOK && m_stat == Stats.SAOK && e_icode == Instrs.IOPL) {
            e_setcc = 1;
        }
        if (e_setcc == 1) {
            zf = sf = of = 0;
            if (e_valE == 0) {
                zf = 1;
            }
            if (e_valE < 0) {
                sf = 1;
            }
            switch (e_alufun) {
                case 0:
                    if (!((e_aluA < 0) ^ (e_aluB < 0)) && (e_valE < 0) ^ (e_valA < 0)) {
                        of = 1;
                    }
                    break;
                case 1:
                    if ((e_aluA < 0) ^ (e_aluB > 0) && ((e_aluB < e_aluA && e_valE > 0) || (e_aluB > e_aluA && e_valE < 0))) {
                        of = 1;
                    }
                    break;
            }
        }

        if (E_icode == Instrs.IJXX || E_icode == Instrs.IRRMOVL) {
            switch (E_ifun) {
                case 0:
                    e_Cnd = 1;
                    break;
                case 1:
                    e_Cnd = (sf ^ of) | zf;
                    break;
                case 2:
                    e_Cnd = sf ^ of;
                    break;
                case 3:
                    e_Cnd = zf;
                    break;
                case 4:
                    e_Cnd = zf ^ 1;
                    break;
                case 5:
                    e_Cnd = sf ^ of ^ 1;
                    break;
                case 6:
                    e_Cnd = (sf ^ of ^ 1) & (zf ^ 1);
                    break;
            }
        }
        e_dstE = (E_icode == Instrs.IRRMOVL && e_Cnd == 0) ? 0xf : E_dstE;
        e_dstM = E_dstM;
    }
}
