import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by iddqd on 8/8/15.
 */
public class ArgumentsHelper {

    private ArgumentsHelper(){}

    private static final HelpFormatter helpFormatter = new HelpFormatter();
    private static final Options options = new Options();

    private static final CommandLineParser parser = new DefaultParser();

    static final String THREADS_LIMIT         = "n";
    static final String SPEED_LIMIT           = "l";
    static final String INPUT_FILEPATH        = "f";
    static final String OUTPUT_DIR            = "o";
    static final String DEFAULT_THREADS_LIMIT = "2";
    /* No speed limit */
    static final String DEFAULT_SPEED_LIMIT   = "0";

    static final String SPEED_LIMIT_DESC   = "general download speed limit, k and m suffixes are allowed; " + "no speed limit by default";
    static final String INPUT_FILE_DESC    = "path to file with filenames and links which will be downloaded;\n" + "example: http://site.org/file.zip archive.zip";
    static final String OUTPUT_DIR_DESC    = "existing output directory";
    static final String THREADS_LIMIT_DESC = "amount of concurrent downloading threads, default value = 2";
    static final String FILE_PATH_ERROR    = "Path or file %s doesn't exist";
    static final String APP_NAME           = "console-downloader";
    static final String INVALID_ARG        = "%s is not valid argument value";


    private static class CommandLineOptions {

        static Option speedLimit = Option.builder(SPEED_LIMIT).required(false).hasArg().desc(SPEED_LIMIT_DESC).build();

        static Option inputPath = Option.builder(INPUT_FILEPATH).required(true).desc(INPUT_FILE_DESC).hasArg().build();

        static Option outputPath = Option.builder(OUTPUT_DIR).required(true).desc(OUTPUT_DIR_DESC).hasArg().build();

        static Option threads = Option.builder(THREADS_LIMIT).required(false).desc(THREADS_LIMIT_DESC).hasArg().build();

    }

    public static long handleUnitSuffix(String bytesAsStr) {
        String  numberPattern = "(\\d+)";
        Pattern numPattern    = Pattern.compile(numberPattern);
        String  kbPattern     = "^\\d+k$";
        String  mbPattern     = "^\\d+m$";
        Matcher matcher       = numPattern.matcher(bytesAsStr);
        if (Pattern.matches(numberPattern, bytesAsStr) && matcher.find()) {
            return Long.parseLong(matcher.group());
        } else if (Pattern.matches(kbPattern, bytesAsStr) && matcher.find()) {
            return Long.parseLong(matcher.group()) * 1024;
        } else if (Pattern.matches(mbPattern, bytesAsStr) && matcher.find()) {
            return Long.parseLong(matcher.group()) * 1024 * 1024;
        } else {
            System.out.println(String.format(INVALID_ARG, bytesAsStr));
            printUsageHelp();
            System.exit(1);
        }
        return 0;
    }

    public static void printUsageHelp() {
        helpFormatter.printHelp(APP_NAME, options);
    }

    public static void checkFilePaths(String... paths) {
        for (String path : paths) {
            if (!Files.exists(Paths.get(path))) {
                System.out.println(String.format(FILE_PATH_ERROR, path));
                printUsageHelp();
                System.exit(1);
            }
        }
    }

    public static CommandLine parse(String[] args) throws ParseException {
        options.addOption(CommandLineOptions.inputPath);
        options.addOption(CommandLineOptions.speedLimit);
        options.addOption(CommandLineOptions.outputPath);
        options.addOption(CommandLineOptions.threads);

        return parser.parse(options, args);
    }

}
