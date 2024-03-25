import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Writer {
    FileWriter fileWrite = null;
    BufferedWriter bufferWrite = null;
    PrintWriter printWrite = null;

    // this method appends content provided as an argument to the specified filename
    public void appendWrite(String filename, String content) {

        try {
            fileWrite = new FileWriter(filename, true);
            bufferWrite = new BufferedWriter(fileWrite);
            printWrite = new PrintWriter(bufferWrite);
            printWrite.println(content);
            printWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (printWrite != null) {
                    printWrite.close();
                }
                if (bufferWrite != null) {
                    bufferWrite.close();
                }
                if (fileWrite != null) {
                    fileWrite.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
