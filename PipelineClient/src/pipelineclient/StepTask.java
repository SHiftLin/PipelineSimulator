/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineclient;

import java.io.DataInputStream;
import java.util.TimerTask;

/**
 *
 * @author 11437
 */
public class StepTask extends TimerTask {

    DataInputStream in;

    public StepTask(DataInputStream _in) {
        in = _in;
    }

    @Override
    public void run() {
        try {
            PipelineClient.CF.Display(in);
        } catch (Exception e) {
        }
    }
}
