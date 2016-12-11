import java.util.concurrent.CompletableFuture;

/**
 * Created with IntelliJ IDEA.
 * Date: 2016/12/11
 * Time: 23:11
 *
 * @author: Zero
 */
public class asyc {
    public static void main(String[] args) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            sleep(500);
            System.out.println(Thread.currentThread().getName() + "--------1--------");
        });
        future.thenAccept(aVoid -> System.out.println(Thread.currentThread().getName() + "--2--"));
        future.thenRun(() -> {
            sleep(300);
            System.out.println(Thread.currentThread().getName() + "----------thenRun----1---------");
        });
        for (int i = 0; i < 100; i++) {
            CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "--------runAsync--------");
            });
        }
        CompletableFuture.allOf(CompletableFuture.runAsync(() -> {

        }),CompletableFuture.runAsync(() -> {

        }));
        future.thenRun(() -> {
            System.out.println(Thread.currentThread().getName() + "----------thenRun----2---------");
        });
        future.thenRun(() -> {
            sleep(200);
            System.out.println("----------thenRun----3---------");
        }).thenRun(() -> {
            System.out.println(Thread.currentThread().getName() + "----------thenRun----3---thenRun--1----");
        });

        System.out.println("----------4----------------");
        future.join();
    }

    public static void sleep(long s) {
        try {
            Thread.sleep(s);
            throw new InterruptedException("pp");
        } catch (InterruptedException e) {
        }
    }
}
