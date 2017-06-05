package apt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zero
 *         Created on 2017/1/3.
 */
public class Example {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            Thread.currentThread().interrupt();//标记中断
            Thread.currentThread().setName("Test Thread");
            System.out.println(Thread.currentThread().getName() + " isInterrupted : " + Thread.currentThread().isInterrupted());
        });

        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " isInterrupted : " + Thread.currentThread().isInterrupted());
            System.out.println("-----");
        });
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        Thread.interrupted();
    }
}
