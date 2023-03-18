import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowImpl implements RateLimiting {

    private final Queue<Long> queue;
    private final ReentrantLock reentrantLock;
    private final long allowedThreshold;
    private final long maxAllowedRequests;
    private Long currentTimeStamp;

    public SlidingWindowImpl(long allowedThreshold, long maxAllowedRequests) {
        this.allowedThreshold = allowedThreshold;
        this.maxAllowedRequests = maxAllowedRequests;
        this.reentrantLock = new ReentrantLock();
        this.queue = new LinkedList<>();
    }

    @Override
    public boolean checkAccess(long currentTimeStamp) {
        reentrantLock.lock();
        updateOldTimeStamp(currentTimeStamp);
        boolean canAccess = checkAccessHelper();
        if(canAccess) {
            //System.out.println("queue.size() = " + queue.size() );
            queue.offer(currentTimeStamp);
        }
        //System.out.println("queue = " + Arrays.toString(queue.toArray()));
        reentrantLock.unlock();
        return canAccess;
    }

    private boolean checkAccessHelper() {
        return (maxAllowedRequests > queue.size());
    }

    private void updateOldTimeStamp(long currentTimeStamp) {
        if (queue.size() == 0) {
            return;
        }
        long diff = (currentTimeStamp - queue.peek())/1000;
        while (diff >= allowedThreshold) { // not in same window
            queue.poll();
            if (queue.size() == 0) {
                return;
            }
            diff = (currentTimeStamp - queue.peek());
        }
    }
}
