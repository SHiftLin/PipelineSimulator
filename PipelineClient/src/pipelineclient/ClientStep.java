/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineclient;

import java.util.TimerTask;
/**
 *
 * @author 11437
 */
public class ClientStep extends TimerTask{
    @Override
    public void run(){
        try{
            PipelineClient.CF.Display();
        }catch(Exception e){
        }
    }
}
