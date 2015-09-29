import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by iddqd on 8/9/15.
 */
public class InputStreamWrapper extends FilterInputStream {

    private long timestamp = System.currentTimeMillis();
    private long LIMIT;
    private static final int MS_PER_SEC = 1000;
    private int counter;


    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected InputStreamWrapper(InputStream in, long limit) {
        super(in);
        this.LIMIT = limit;
    }

    @Override
    public int read() throws IOException {
        if (counter > LIMIT) {
            long now = System.currentTimeMillis();
            if (timestamp + MS_PER_SEC >= now) {
                try {
                    Thread.sleep(timestamp + MS_PER_SEC - now);
                } catch (InterruptedException ignore) {
                }
            }
            timestamp = now;
            counter = 0;
        }
        int result = super.read();
        if (result > 0) counter++;
        return result;
    }

}
