public interface RateLimiting {
    boolean checkAccess(long currentTimeStamp);
}
