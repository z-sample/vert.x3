package eventloop;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author Zero
 *         Created on 2017/4/27.
 */
public class MyEventLoop {
    private static final ScheduledExecutorService eventLoop =
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "EventLoop"));
    private static ExecutorService service = Executors.newFixedThreadPool(1);
    static BlockingDeque<Runnable> tasks = new LinkedBlockingDeque<>();

    public void start() {
        eventLoop.schedule(() -> {
            while (true) {
                Runnable task = tasks.poll();
                if (task != null) {
                    service.submit(task);
                }
            }
        }, 20, TimeUnit.MICROSECONDS);

    }

    public static void main(String[] args) {
        MyEventLoop eventLoop = new MyEventLoop();
        eventLoop.start();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            tasks.add(() -> {
                Random random = new Random();
                    Math.sin(random.nextInt(10000));
//                for (int i = 0; i < 1000; i++) {
//                }
//                System.out.println(new Date() + " : " + Thread.currentThread().getName());
            });

        }, 20, 10, TimeUnit.MICROSECONDS);
    }

}
