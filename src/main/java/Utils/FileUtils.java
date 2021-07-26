package Utils;

import java.io.*;

public class FileUtils {
    File file;

    public FileUtils() {
        file = new File("list.txt");
        if (file.exists()) {
            Logger.logFuncStart("list file exists!");
        }else {
            try {
                if (file.createNewFile())
                {
                    System.out.println("Utils.ConfigUtils--Utils.ConfigUtils(): list file created!");
                } else {
                    System.out.println("Utils.ConfigUtils--Utils.ConfigUtils(): ERROR creating list file!");
                }
            } catch (IOException e) {
                System.out.println("Utils.ConfigUtils--Utils.ConfigUtils(): ERROR creating list file!");
            }
        }
    }

    public void fileWriter(String writeString) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String lines = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                lines += line + "\n";
            }
            lines += writeString;
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(lines.toString());
            writer.close();
            br.close();
        }catch (Exception ex) {

        }
    }
}
