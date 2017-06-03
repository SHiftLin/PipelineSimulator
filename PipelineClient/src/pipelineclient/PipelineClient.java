/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineclient;

import pipelineclient.UI.ClientFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author lsh
 */
public class PipelineClient {

    private static String serverAddress = "127.0.0.1";
    private static int port = 10000;
    static Socket client;
    static DataOutputStream out;
    static DataInputStream in;
    static ClientFrame CF;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            client = new Socket(serverAddress, port);
            client.setSoTimeout(500);
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to the server!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CF = new ClientFrame();
        CF.setLocationRelativeTo(null);
        CF.setResizable(false);
        CF.setVisible(true);
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

    public static String Receive() {
        String result = "";
        try {
            result = in.readUTF();
        } catch (Exception e) {
        }
        return result;
    }

    public static byte[] ReceiveBytes() {
        byte[] result = new byte[Var.MEMORY_SIZE];
        try {
            in.read(result, 0, Var.MEMORY_SIZE);
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
