/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver;

import java.util.TimerTask;

/**
 *
 * @author lsh
 */
public class StepTask extends TimerTask {
    
    @Override
    public void run() {
        try {
            PipelineServer.pipeline.Next(true);
        } catch (Exception e) {
        }
    }
    
}
