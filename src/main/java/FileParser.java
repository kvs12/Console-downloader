import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iddqd on 8/8/15.
 */
public class FileParser {
    final String splitPattern = "\\s+";
    private final HashMap<String, String> linkAndNameMap = new HashMap<>();

    public Map<String, String> parseFile(String filename) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitLine = line.split(splitPattern);
                if (splitLine.length == 2) {
                    linkAndNameMap.put(splitLine[1], splitLine[0]);
                } else {
                    System.out.println(String.format("Line '%s' probably doesn't have link or filename, " +
                            "it will be skipped", line));
                }
            }
        }
        return linkAndNameMap;
    }
}
