package apt;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zero
 *         Created on 2017/1/10.
 */
public class AsyncDemo2 {

    //
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) { }
            System.out.println("Task-1:--------------------");
            return "abc";
        });
//      future.thenAccept()//非异步
        //依赖Task-1, 并以异步模式执行新的任务
        future.thenRunAsync(() -> {
            System.out.println("Task-2:---------start-----------");
            String task1rs = future.join();
            System.out.println(task1rs);
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            System.out.println("Task-2:---------end-----------"+Thread.currentThread().getName());
        });
        //依赖Task-1, 并以异步模式执行新的任务
        future.thenRunAsync(() -> {
            System.out.println("Task-3:---------start-----------");
            String task1rs = future.join();
            System.out.println(task1rs);
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            System.out.println("Task-3:---------end-----------"+Thread.currentThread().getName());
        });
        //依赖Task-1, 并以阻塞形式返回值
        CompletableFuture<String> future2 = future.thenApply(aVoid -> "I am Task-4");
        System.out.println("Task-1 Result: "+future.get());
        System.out.println("Task-4 Result: "+future2.get());
        Thread.sleep(2000);

    }
//
//    Task-1:--------------------
//    Task-1 Result: abc
//    Task-2:---------start-----------
//    Task-3:---------start-----------
//    abc
//            abc
//    Task-4 Result: I am Task-4
//    Task-3:---------end-----------ForkJoinPool.commonPool-worker-1
//    Task-2:---------end-----------ForkJoinPool.commonPool-worker-2
}
