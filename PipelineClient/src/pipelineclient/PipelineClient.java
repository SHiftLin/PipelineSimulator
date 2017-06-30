/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineclient;

import pipelineclient.UI.ClientFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author lsh
 */
public class PipelineClient {

    private static String serverAddress;
    private static int port = 8888;
    private static Socket client;
    static DataOutputStream out;
    static DataInputStream in;
    static ClientFrame CF;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        serverAddress = JOptionPane.showInputDialog(null, "Server Address:", "Input", JOptionPane.QUESTION_MESSAGE);
        if (serverAddress.length() <= 0) {
            serverAddress = "127.0.0.1";
        }
        try {
            client = getSocket();
            client.setSoTimeout(0);
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to the server!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        CF = new ClientFrame();
        CF.setLocationRelativeTo(null);
        CF.setResizable(false);
        CF.setVisible(true);
    }

    public static Socket getSocket() throws Exception {
        return new Socket(serverAddress, port);
    }

    public static void Send(String request) {
        try {
            out.writeUTF(request);
        } catch (Exception e) {
        }
    }

    public static void SendBytes(byte[] argv, int len) {
        try {
            out.write(argv, 0, len);
        } catch (Exception e) {
        }
    }

    public static String Receive(DataInputStream inStream) {
        if (inStream == null) {
            inStream = in;
        }
        String result = "";
        try {
            result = inStream.readUTF();
        } catch (Exception e) {
        }
        return result;
    }

    public static byte[] ReceiveBytes(int len) {
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

    public static void clearInputStream() {
        try {
            in.skip(in.available());
        } catch (Exception e) {
        }
    }
}
