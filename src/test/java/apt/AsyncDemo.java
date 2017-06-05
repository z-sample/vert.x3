package apt;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Zero
 *         Created on 2017/1/10.
 */
public class AsyncDemo {

    //
    public static void main(String[] args) throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) { }
            System.out.println("Task-1:--------------------");
        });
//      future.thenAccept()//非异步
        //依赖Task-1, 并以异步模式执行新的任务
        future.thenRunAsync(() -> {
            System.out.println("Task-2:---------start-----------");
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            System.out.println("Task-2:---------end-----------"+Thread.currentThread().getName());
        });
        //依赖Task-1, 并以异步模式执行新的任务
        future.thenRunAsync(() -> {
            System.out.println("Task-3:---------start-----------");
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            System.out.println("Task-3:---------end-----------"+Thread.currentThread().getName());
        });
        //依赖Task-1, 并以阻塞形式返回值
        CompletableFuture<String> future2 = future.thenApply(aVoid -> "Task-4 result");
        System.out.println("=============================================================");
        System.out.println(future.get());
        System.out.println(future2.get());
        Thread.sleep(2000);

    }

//    Task-1:--------------------
//    Task-3:---------start-----------
//    null
//    Task-4 result
//    Task-2:---------start-----------
//    Task-2:---------end-----------ForkJoinPool.commonPool-worker-2
//    Task-3:---------end-----------ForkJoinPool.commonPool-worker-1
}
