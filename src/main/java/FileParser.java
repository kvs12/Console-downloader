import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by iddqd on 8/8/15.
 */
public class FileParser {
    String   line;
    String[] splittedLine;
    final String                  splitPattern   = "\\s+";
    final HashMap<String, String> linkAndNameMap = new HashMap<>();

    HashMap<String, String> parseFile(String filename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {

            while ((line = bufferedReader.readLine()) != null) {
                splittedLine = line.split(splitPattern);
                if (splittedLine.length == 2) {
                    linkAndNameMap.put(splittedLine[1], splittedLine[0]);
                } else {
                    System.out.println(String.format("Line '%s' probably doesn't have link or filename, " +
                                                     "it will be skipped", line));
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return linkAndNameMap;
    }
}
