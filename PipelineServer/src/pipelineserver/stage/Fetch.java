/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver.stage;

import static pipelineserver.stage.Var.*;
import pipelineserver.utility.*;

/**
 *
 * @author lsh
 */
public class Fetch extends Thread {

    @Override
    public void run() {
        int f_pc = 0;
        if (M_icode == Instrs.IJXX && M_Cnd == 0) {
            f_pc = M_valA;
        } else if (W_icode == Instrs.IRET) {
            f_pc = W_valM;
        } else {
            f_pc = F_predPC;
        }

        if (f_pc < 0 || f_pc >= MEMORY_SIZE) {
            f_code = "AddrEr";
            f_stat = Stats.SADR;
            f_icode = Instrs.INOP;
            f_ifun = 0;
            f_rA = 0xf;
            f_rB = 0xf;
            f_valC = 0;
            f_valP = 0;
            f_needr = 0;
            f_needvc = 0;
            f_predPC = f_pc + 1;
            return;
        }

        byte icode = Convert.getHigh(memory[f_pc]);
        byte ifun = Convert.getLow(memory[f_pc]);

        String instr = getString.Instr(icode, ifun);
        String operand = getString.Operand(icode, f_pc);

        if (instr.equals("error") || operand.equals("error")) {
            f_code = "InstrEr";
            f_stat = Stats.SINS;
            f_icode = Instrs.INOP;
            f_ifun = 0;
            f_rA = 0xf;
            f_rB = 0xf;
            f_valC = 0;
            f_valP = 0;
            f_needr = 0;
            f_needvc = 0;
            f_predPC = f_pc + 1;
            return;
        }
        if (icode == 0) {
            f_code = "Halt";
            f_stat = Stats.SHLT;
            f_icode = Instrs.IHALT;
            f_ifun = 0;
            f_rA = 0xf;
            f_rB = 0xf;
            f_valC = 0;
            f_valP = 0;
            f_needr = 0;
            f_needvc = 0;
            f_predPC = f_pc + 1;
            return;
        }

        f_code = instr + " " + operand;
        f_stat = Stats.SAOK;
        f_icode = Instrs.values()[icode];
        f_ifun = ifun;

        byte rA = Convert.getHigh(memory[f_pc + 1]);
        byte rB = Convert.getLow(memory[f_pc + 1]);

        switch (f_icode) {
            case IRRMOVL:
            case IIRMOVL:
            case IRMMOVL:
            case IMRMOVL:
            case IOPL:
            case IPUSHL:
            case IPOPL:
                f_needr = 1;
                break;
            default:
                f_needr = 0;
                break;
        }
        switch (f_icode) {
            case IIRMOVL:
            case IRMMOVL:
            case IMRMOVL:
            case IJXX:
            case ICALL:
                f_needvc = 1;
                break;
            default:
                f_needvc = 0;
                break;
        }

        f_rA = (f_needr == 1) ? rA : 0xf;
        f_rB = (f_needr == 1) ? rB : 0xf;

        if (f_needvc == 1) {
            f_valC = Convert.ByteArray2Int(memory, f_pc + 1 + f_needr);
        } else {
            f_valC = 0;
        }
        f_valP = f_pc + 1 + f_needr + 4 * f_needvc;

        f_predPC = (f_icode == Instrs.IJXX || f_icode == Instrs.ICALL) ? f_valC : f_valP;
    }
}
