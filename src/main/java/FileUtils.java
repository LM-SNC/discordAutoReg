import java.io.*;

public class FileUtils {
    File file;

    public FileUtils() {
        file = new File("list.txt");
        if (file.exists()) {
            System.out.println("Utils.ConfigUtils--Utils.ConfigUtils(): list file exists!");
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

    void fileWriter(String writeString) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(line + "\n" + writeString);
            writer.close();
            br.close();
        }catch (Exception ex) {

        }
    }


}
