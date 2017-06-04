/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineclient;

/**
 *
 * @author lsh
 */
public class Request {

    public final static char OPENFILE = 'I', PLAY = 'P', NEXT = 'N', STOP = 'S';
    public final static char RESET = 'R', REFRESH = 'F', CODE = 'C', MEMORY = 'M';

    public static String getReqStr(char cmd, String argv) {
        return cmd + argv;
    }
}
