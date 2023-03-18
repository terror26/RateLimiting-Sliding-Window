import java.util.HashMap;
import java.util.Map;

public class RateLimitingService {
    Map<String, RateLimiting> map; // id vs RateLimiting
    public RateLimitingService(String id, long threshold, long maxAllowedRequests) {

        map = new HashMap<>();
        map.put(id, new SlidingWindowImpl(threshold,maxAllowedRequests));
    }

    public boolean checkAccess(String id, long currentTimeStamp) {
        boolean v = map.get(id).checkAccess(currentTimeStamp);
        System.out.println("currentTimeStamp = " + currentTimeStamp + " , thread = " + Thread.currentThread().getName() + " , access = " + v);
        return v;
    }
}
