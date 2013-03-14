import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * a class to limit the count of threads those can access the shared resource
 * 
 * @author handle 2013-3-13 ÏÂÎç4:57:13
 */
public class ResourceLimitor{

    /**
     * wait threads to call the method
     */
    private LinkedBlockingDeque<Integer> waitQueue           = new LinkedBlockingDeque<Integer>(20);
    /**
     * remaining thread count that can call the method
     */
    private Semaphore                    canExecuteCount     = new Semaphore(10, true);
    /**
     * the time every thread can stay in wait queue
     */
    private static final int             TRY_EXECUTE_TIMEOUT = 20;

    public boolean applyResource() {
        // check whether the thread can enter in the wait queue
        if (!waitQueue.offer(0)) {
            return false;
        }
        // after enter the wait queue,try to acquire a permit to call the methos with a timeout
        boolean canExecute = false;
        try {
            canExecute = canExecuteCount.tryAcquire(1, TRY_EXECUTE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        // if this thread can't call the method,before return a result ,should poll it out of the wait queue
        // if this thread can call the method,then after call the method ,shoud call releaseResource() to poll it out of
        // the wait queue
        if (!canExecute) {
            waitQueue.poll();
        }
        return canExecute;

    }

    public void releaseResource() {
        canExecuteCount.release(1);
        waitQueue.poll();
    }

}
