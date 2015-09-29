import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iddqd on 8/8/15.
 */
public class DownloadManager {
    private static PoolingHttpClientConnectionManager connectionManager;
    /* Download limit per thread in bytes */
    private static long                               downloadLimit;
    private        CloseableHttpClient                httpClient;
    private        HashMap<String, String>            linksAndFilenames;
    private        HttpGet                            httpGet;
    private        String                             outputDest;


    private static       AtomicCounter       bytesCounter  = new AtomicCounter();
    private              List<GetDownloader> downloaders   = new ArrayList<>();
    private static final String              ERROR_MESSAGE = "Can't start downloading: %s\n" +
                                                             "It might be related to internet connection";

    DownloadManager(HashMap<String, String> linksAndFilenames,
                    String outputDest,
                    int poolSize,
                    long speedLimit) throws IOException {
        downloadLimit = speedLimit / poolSize;
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(poolSize);
        /* In case it can be multiple downloads from the same host */
        connectionManager.setDefaultMaxPerRoute(poolSize);
        httpClient = HttpClients.custom()
                                .setConnectionManager(connectionManager)
                                .build();
        this.linksAndFilenames = linksAndFilenames;
        this.outputDest = outputDest;
        collectDownloaders();
    }

    public void collectDownloaders() {
        for (Map.Entry<String, String> entry : linksAndFilenames.entrySet()) {
            httpGet = new HttpGet(entry.getValue());
            String filename = entry.getKey();
            Path outputPath = Paths.get(this.outputDest, filename);
            downloaders.add(new GetDownloader(httpClient, httpGet, outputPath));
        }
    }

    public void start() throws InterruptedException {

        for (GetDownloader downloader : downloaders) {
            downloader.start();
        }

        for (GetDownloader downloader : downloaders) {
            downloader.join();
        }

    }

    public long getBytesCounter() {
        return bytesCounter.getValue();
    }

    static class GetDownloader extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpContext         context;
        private final HttpGet             httpGet;
        private final Path                destination;

        GetDownloader(CloseableHttpClient httpClient,
                      HttpGet httpGet,
                      Path destination) {
            this.httpClient = httpClient;
            this.destination = destination;
            this.context = HttpClientContext.create();
            this.httpGet = httpGet;

        }

        private void writeToFile(InputStream inputStream, String outputDest, long contentLength) throws IOException {
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                         new FileOutputStream(outputDest)
                 )) {

                System.out.println("Starting to write " + outputDest);

                int getBytes;
                    while ((getBytes = inputStream.read()) != -1) {
                        bufferedOutputStream.write(getBytes);
                    }

                bytesCounter.increment(contentLength);

            } catch (IOException e) {
                throw e;
            }

        }

        @Override
        public void run() {
            try (CloseableHttpResponse response = httpClient.execute(httpGet, context)) {

                HttpEntity entity = response.getEntity();
                InputStream inputStream;
                if (downloadLimit != 0) {
                    inputStream = new InputStreamWrapper(entity.getContent(), downloadLimit);
                } else {
                    inputStream = entity.getContent();
                }

                writeToFile(inputStream, destination.toString(), entity.getContentLength());

            } catch (IOException e) {
                System.out.println(String.format(ERROR_MESSAGE, e.getMessage()));
            }
        }
    }
}
