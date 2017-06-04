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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            serverSocket = new ServerSocket(10000);
        } catch (Exception e) {
            System.out.println("Port allocation failed!");
            System.exit(0);
        }
        while (true) {
            try {
                server = Accept();
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
                System.out.println("Request process failed!" + e.getMessage());
            }
        }
    }

    public static Socket Accept() throws Exception {
        return serverSocket.accept();
    }

    static byte[] ReceiveBytes(int len) throws Exception {
        byte[] result = new byte[len];
        try {
            int left = len, offset = 0;
            while (left > 0) {
                int x = in.read(result, offset, left);
                left -= x;
                offset += x;
            }
        } catch (Exception e) {
        }
        return result;
    }

    static void WriteInfo(DataOutputStream outStream, String result) {
        if (outStream == null) {
            outStream = out;
        }
        try {
            outStream.writeUTF(result);
            System.out.println(result);
        } catch (Exception e) {
        }
    }

    static void WriteInfo(byte[] result) throws Exception {
        out.write(result, 0, result.length);
    }
}
