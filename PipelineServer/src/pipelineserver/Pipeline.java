/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver;

import java.io.DataOutputStream;
import static java.lang.Math.*;
import java.net.Socket;
import java.util.Timer;
import pipelineserver.stage.*;
import pipelineserver.utility.*;
import static pipelineserver.stage.Var.*;

/**
 *
 * @author lsh
 */
public class Pipeline {

    Thread F, D, E, M, W;
    boolean Runnable;
    Timer timer;
    StepTask task;
    Socket socket;

    public Pipeline() {
        /*
        F = new Fetch();
        D = new Decode();
        E = new Execute();
        M = new MemoryVisit();
        W = new WriteBack();
         */
        Var.reset();
        codeLen = 0;
        Runnable = true;
        timer = new Timer();
        task = null;
    }

    private void Reset() throws Exception {
        Var.reset();
        Runnable = true;
        for (int i = 0; i < codeLen; i++) {
            memory[i] = code[i];
        }
    }

    private void OpenFile(byte argv[]) throws Exception {
        codeLen = min(argv.length, MEMORY_SIZE);
        code = new byte[codeLen];
        for (int i = 0; i < codeLen; i++) {
            code[i] = argv[i];
        }
        Reset();
    }

    private void Play(int frequency) throws Exception {
        socket = PipelineServer.Accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        int interval = 1000 / frequency;
        task = new StepTask(out);
        timer.schedule(task, 0, interval);
    }

    void Next(DataOutputStream out, boolean transmit) throws Exception {
        if (!Runnable) {
            if (task != null) {
                task.cancel();
            }
            PipelineServer.WriteInfo(out, JsonUtil.HALT());
            return;
        }
        Cycle++;
        F = new Fetch();
        D = new Decode();
        E = new Execute();
        M = new MemoryVisit();
        W = new WriteBack();
        F.start();
        E.start();
        M.start();
        W.start();
        E.join();
        M.join();
        D.start();
        F.join();
        D.join();
        W.join();
        Control.stall_bubble();
        Control.updateRegisters();
        if (transmit) {
            Refresh(out);
        }
        if (W_stat != Stats.SAOK) {
            Runnable = false;
        }
    }

    private void Stop(int cycle) throws Exception {
        if (task != null) {
            task.cancel();
        }
        try {
            socket.close();
        } catch (Exception e) {
        }
        if (cycle < Cycle) {
            Reset();
            for (int i = 0; i < cycle; i++) {
                Next(null, false);
            }
        }
    }

    private void Refresh(DataOutputStream out) throws Exception {
        PipelineServer.WriteInfo(out, JsonUtil.Process());
    }

    private void Code() throws Exception {
        PipelineServer.WriteInfo(null, getString.showCode());
    }

    private void Memory() throws Exception {
        PipelineServer.WriteInfo(memory);
    }

    public void work(String request) throws Exception {
        Request req = new Request(request);
        switch (req.cmd) {
            case Request.RESET:
                Reset();
                break;
            case Request.OPENFILE:
                OpenFile(PipelineServer.ReceiveBytes(req.argv));
                break;
            case Request.PLAY:
                Play(req.argv);
                break;
            case Request.NEXT:
                Next(null, true);
                break;
            case Request.STOP:
                Stop(req.argv);
                break;
            case Request.REFRESH:
                Refresh(null);
                break;
            case Request.CODE:
                Code();
                break;
            case Request.MEMORY:
                Memory();
                break;
        }
    }
}

class Request {

    public final static char OPENFILE = 'I', PLAY = 'P', NEXT = 'N', STOP = 'S';
    public final static char RESET = 'R', REFRESH = 'F', CODE = 'C', MEMORY = 'M';
    char cmd;
    int argv;

    public Request(String str) {
        cmd = str.charAt(0);
        if (str.length() > 1) {
            argv = Integer.parseInt(str.substring(1));
        }
    }
}
