/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver;

import static java.lang.Math.*;
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

    private void Reset(boolean transmit) throws Exception {
        Var.reset();
        Runnable = true;
        for (int i = 0; i < codeLen; i++) {
            memory[i] = code[i];
        }
        if (transmit) {
            Refresh();
        }
    }

    private void OpenFile(byte argv[]) throws Exception {
        Reset(false);
        codeLen = min(argv.length, MEMORY_SIZE);
        code = new byte[codeLen];
        for (int i = 0; i < codeLen; i++) {
            memory[i] = argv[i];
            code[i] = argv[i];
        }
    }

    private void Play(int frequency) throws Exception {
        int interval = 1000 / frequency;
        task = new StepTask();
        timer.schedule(task, 0, interval);
    }

    void Next(boolean transmit) throws Exception {
        if (!Runnable) {
            if (task != null) {
                task.cancel();
            }
            PipelineServer.WriteInfo(JsonUtil.HALT());
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
            Refresh();
        }
        if (W_stat != Stats.SAOK) {
            if (task != null) {
                task.cancel();
            }
            Runnable = false;
            PipelineServer.WriteInfo(JsonUtil.HALT());
        }
    }

    private void Stop(int cycle) throws Exception {
        if (task != null) {
            task.cancel();
        }
        if (cycle < Cycle) {
            OpenFile(code);
            for (int i = 0; i < cycle; i++) {
                Next(false);
            }
        }
    }

    private void Refresh() throws Exception {
        PipelineServer.WriteInfo(JsonUtil.Process());
    }

    private void Code() throws Exception {
        PipelineServer.WriteInfo(getString.showCode());
    }

    private void Memory() throws Exception {
        PipelineServer.WriteInfo(memory);
    }

    public void work(String request) throws Exception {
        Request req = new Request(request);
        switch (req.cmd) {
            case Request.RESET:
                Reset(true);
                break;
            case Request.OPENFILE:
                OpenFile(PipelineServer.ReceiveBytes(req.argv));
                break;
            case Request.PLAY:
                Play(req.argv);
                break;
            case Request.NEXT:
                Next(true);
                break;
            case Request.STOP:
                Stop(req.argv);
                break;
            case Request.REFRESH:
                Refresh();
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
