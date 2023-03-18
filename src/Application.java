import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    private String id = "101";
    private long threshold = 1000L; // 5sec Sec
    private long maxAllowedRequests = 10; // allowed requests in time-frame

    public static void main(String[] args) {
        new Application().helperMain();
    }

    public void helperMain() {
        RateLimitingService rateLimitingService = new RateLimitingService(id, threshold, maxAllowedRequests);
        for(int i = 0;i<12;i++) {
            executor.execute(()-> rateLimitingService.checkAccess(id, System.currentTimeMillis()));
        }
        executor.shutdown();
    }
}
