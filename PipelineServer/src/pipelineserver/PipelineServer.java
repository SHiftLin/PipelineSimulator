/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lsh
 */
public class PipelineServer {

    static Pipeline pipeline = new Pipeline();
    private static ServerSocket serverSocket;
    private static Socket server;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static int MAX_CODELEN = 65536;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            serverSocket = new ServerSocket(10000);
        } catch (Exception e) {
            System.out.println("Port allocation failed!");
            return;
        }
        while (true) {
            try {
                server = serverSocket.accept();
                System.out.println("New connection!");

                serveStart();

                server.close();
            } catch (Exception e) {
            }
            System.out.println("Connection lost.\n");
        }

    }

    private static void serveStart() throws Exception {
        in = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
        while (true) {
            String request = in.readUTF();
            System.out.println("request: " + request);
            if (request == "quit") {
                break;
            }
            try {
                pipeline.work(request);
            } catch (Exception e) {
                System.out.println("Request process failed!"+e.getMessage());
            }
        }
    }

    static byte[] ReadBytes() throws Exception {
        byte[] buffer = new byte[MAX_CODELEN];
        int count = in.read(buffer);
        byte[] argv = new byte[count];
        for (int i = 0; i < count; i++) {
            argv[i] = buffer[i];
        }
        return argv;
    }

    static void WriteInfo(String result) throws Exception {
        out.writeUTF(result);
        System.out.println(result);
    }

    static void WriteInfo(byte[] result) throws Exception {
        out.write(result, 0, result.length);
    }
    /*
    static String readLine() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
     */
}
