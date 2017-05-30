/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver.utility;

import java.io.*;
import static pipelineserver.stage.Var.*;
/**
 *
 * @author lsh
 */
public class FileUtil {
    
    public static void printInfo(int Cycle) throws IOException{
        File fout=new File("output.txt");
        fout.createNewFile();
        FileWriter writer=new FileWriter(fout,true);
        writer.write("Fetch:\n");
        writer.write("\n");
        writer.write("Decode:\n");
        writer.write(D_code+"\n");
        writer.write("Execute:\n");
        writer.write(E_code+"\n");
        writer.write("Memory:\n");
        writer.write(M_code+"\n");
        writer.write("Write Back:\n");
        writer.write(W_code+"\n");
        writer.write("\n\n");
        writer.flush();
        writer.close();
    }
}
