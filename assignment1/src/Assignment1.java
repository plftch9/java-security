
import java.io.IOException;
// additional imports
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment1 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double maxBytes = 8000000;
        FileInputStream inputStream = null;

        try {

            // Read the filename from the command line argument

            // documentation item 1
            final String filename = args[0];

            String currentDir = System.getProperty("user.dir");
            String path = currentDir + "\\" + filename;

            File file = new File(path);

            double fileBytes = file.length();

            if (maxBytes < fileBytes) {
                System.out.println("File size of " + filename + " too large. Terminating prgram.");
                System.exit(0);
            }

            final String PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
            inputStream = new FileInputStream(filename);
            Scanner fileScan = new Scanner(inputStream);
            Pattern pattern = Pattern.compile(PATTERN);

            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                Matcher matcher = pattern.matcher(line);

                // documentation item 2
                if (matcher.matches()) {
                    System.out.println("Email " + line + "  is valid");
                } else {
                    System.out.println("Email " + line + " is invalid");
                }
            }
            fileScan.close();

            // documentation item 3
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Command line argument not supplied. Program terminating");
            System.exit(0);
        } catch (IOException io) {
            System.out.println("File IO exception" + io.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException io) {
                System.out.println("Issue closing the Files" + io.getMessage());
            }

        }
    }
}