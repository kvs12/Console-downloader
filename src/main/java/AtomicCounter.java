import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by iddqd on 8/11/15.
 */
public class AtomicCounter {
    private AtomicLong counter = new AtomicLong(0);

    public void increment(long value) {
        counter.addAndGet(value);
    }

    public long getValue() {
        return counter.get();
    }

}
