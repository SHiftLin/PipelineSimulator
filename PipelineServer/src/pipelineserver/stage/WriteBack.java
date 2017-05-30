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
public class WriteBack extends Thread {

    @Override
    public void run() {
        if (W_dstE != 0xf) {
            register[W_dstE] = W_valE;
        }
        if (W_dstM != 0xf) {
            register[W_dstM] = W_dstM;
        }
    }
}
