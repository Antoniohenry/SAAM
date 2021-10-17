package SaamAlgo.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter {

    public static void append(String filename, String text) {

        BufferedWriter bufWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename, true);
            bufWriter = new BufferedWriter(fileWriter);
            bufWriter.newLine();
            bufWriter.write(text);
            bufWriter.flush();
            fileWriter.flush();
            bufWriter.close();
            fileWriter.close();
        } catch (IOException ex) {
            try {
                if (bufWriter != null) {
                    bufWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                throw new Error("ecriture dans fichier");
            }

        }
    }

}
