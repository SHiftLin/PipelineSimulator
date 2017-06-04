/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver;

import java.io.DataOutputStream;
import java.util.TimerTask;

/**
 *
 * @author lsh
 */
public class StepTask extends TimerTask {

    DataOutputStream out = null;

    StepTask(DataOutputStream _out) {
        out = _out;
    }

    @Override
    public void run() {
        try {
            PipelineServer.pipeline.Next(out, true);
        } catch (Exception e) {
        }
    }

}
