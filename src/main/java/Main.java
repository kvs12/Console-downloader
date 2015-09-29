import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by iddqd on 8/8/15.
 */
public class Main {
    public static void main(String[] args) {
        try {
            final String STATISTIC_MESSAGE = "Work time: %02d:%02d:%02d\nBytes downloaded: %d";
            long startTime = System.currentTimeMillis();

            String inputFilename = ArgumentsHelper.parse(args).getOptionValue(ArgumentsHelper.INPUT_FILEPATH);
            String outputDest = ArgumentsHelper.parse(args).getOptionValue(ArgumentsHelper.OUTPUT_DIR);
            String maxThreads = ArgumentsHelper.parse(args).getOptionValue(ArgumentsHelper.THREADS_LIMIT,
                                                                           ArgumentsHelper.DEFAULT_THREADS_LIMIT);
            String speedLimit = ArgumentsHelper.parse(args).getOptionValue(ArgumentsHelper.SPEED_LIMIT,
                                                                           ArgumentsHelper.DEFAULT_SPEED_LIMIT);

            ArgumentsHelper.checkFilePaths(inputFilename, outputDest);
            long convertedSpeedLimit = ArgumentsHelper.handleUnitSuffix(speedLimit);

            HashMap<String, String> links = new FileParser().parseFile(inputFilename);
            DownloadManager downloadManager = new DownloadManager(links,
                                                   outputDest,
                                                   Integer.parseInt(maxThreads),
                                                   convertedSpeedLimit);
            downloadManager.start();

            long endTime = System.currentTimeMillis();
            long workingTime = endTime - startTime;
            long workingHours = TimeUnit.MILLISECONDS.toHours(workingTime);
            long workingMinutes = TimeUnit.MILLISECONDS.toMinutes(workingTime) -
                                 TimeUnit.HOURS.toMinutes(workingHours);
            long workingSeconds = TimeUnit.MILLISECONDS.toSeconds(workingTime) -
                                  TimeUnit.MINUTES.toSeconds(workingMinutes);

            System.out.println(String.format(STATISTIC_MESSAGE,
                                             workingHours, workingMinutes, workingSeconds,
                                             downloadManager.getBytesCounter()));

        } catch (ParseException | InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            ArgumentsHelper.printUsageHelp();
        }
    }
}
