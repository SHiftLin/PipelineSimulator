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
public class MemoryVisit extends Thread {

    @Override
    public void run() {
        m_code = M_code;
        m_stat = M_stat;
        m_icode = M_icode;
        m_valE = M_valE;
        m_valA = M_valA;
        m_dstE = M_dstE;
        m_dstM = M_dstM;

        int m_addr = 0;
        if (m_stat != Stats.SAOK || m_icode == Instrs.INOP) {
            m_addr = 0;
            m_valM = 0;
            return;
        }
        switch (m_icode) {
            case IRMMOVL:
            case IMRMOVL:
            case ICALL:
            case IPUSHL:
                m_addr = m_valE;
                break;
            case IRET:
            case IPOPL:
                m_addr = m_valA;
                break;
        }

        boolean Memread = (m_icode == Instrs.IMRMOVL || m_icode == Instrs.IRET || m_icode == Instrs.IPOPL);
        boolean Memwrite = (M_icode == Instrs.IRMMOVL || m_icode == Instrs.ICALL || m_icode == Instrs.IPUSHL);
        if (Memread) {
            if (0 <= m_addr && m_addr < MEMORY_SIZE) {
                m_valM = memory[m_addr + 3]; //little endian
                m_valM = (m_valM << 8) | memory[m_addr + 2];
                m_valM = (m_valM << 8) | memory[m_addr + 1];
                m_valM = (m_valM << 8) | memory[m_addr];
            } else {
                m_stat = Stats.SADR;
            }
        }
        if (Memwrite) {
            if (0 <= m_addr && m_addr < MEMORY_SIZE) {
                memory[m_addr] = (byte) (m_valA & 0x000000ff);
                memory[m_addr + 1] = (byte) ((m_valA & 0x0000ff00) >> 8);
                memory[m_addr + 2] = (byte) ((m_valA & 0x00ff0000) >> 16);
                memory[m_addr + 3] = (byte) ((m_valA & 0xff000000) >> 24);
            } else {
                m_stat = Stats.SADR;
            }
        }
    }
}
