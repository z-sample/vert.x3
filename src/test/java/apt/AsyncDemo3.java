package apt;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Zero
 *         Created on 2017/1/10.
 */
public class AsyncDemo3 {

    //
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> task1Future = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) { }
            System.out.println("Task-1:---------end-----------");
            return "abc";
        });
//      future.thenAccept()//非异步
        //依赖Task-1, 并以异步模式执行新的任务
        CompletableFuture<Void> task2Future = task1Future.thenRunAsync(() -> {
            System.out.println("Task-2:---------start-----------");
            String task1rs = task1Future.join();
            System.out.println(task1rs);
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            System.out.println("Task-2:---------end-----------" + Thread.currentThread().getName());
            //在Task-3任务完成前发生异常,Task-3如果任务被取消了将不会被继续执行
            throw new RuntimeException("Task-2 error");
        });
        //依赖Task-1, 并以异步模式执行新的任务
        CompletableFuture<Void> task3Future =task1Future.thenRunAsync(() -> {
            System.out.println("Task-3:---------start-----------");
            String task1rs = task1Future.join();
            System.out.println(task1rs);
            try { Thread.sleep(1100); } catch (InterruptedException e) { }
            System.out.println("Task-3:---------end-----------"+Thread.currentThread().getName());

        });
        //依赖Task-1, 并以阻塞形式返回值
        CompletableFuture<String> task4Future = task1Future.thenApply(aVoid -> "I am Task-4");
        System.out.println("Task-1 Result: "+task1Future.get());
        System.out.println("Task-4 Result: "+task4Future.get());

        CompletableFuture.allOf(task1Future, task2Future, task3Future, task4Future).thenAccept(aVoid -> {
            System.out.println("所有任务都成功完成了!");
        }).exceptionally(throwable -> {
            //异常处理
            Arrays.stream(new CompletableFuture[]{task2Future,task3Future,task4Future}).forEach(completableFuture -> {
                if(!completableFuture.isDone()&&!completableFuture.isCancelled()){
                    completableFuture.cancel(true);
                }
            });
            System.err.println("Error:"+throwable.getMessage());
            System.err.println("发送错误,任务取消!");
            return null;
        }).join();

//        Thread.sleep(2000);//上面调用了join,这里不用等待了

    }

//    Task-1:---------end-----------
//    Task-3:---------start-----------
//    abc
//    Task-2:---------start-----------
//    abc
//    Task-1 Result: abc
//    Task-4 Result: I am Task-4
//    Task-2:---------end-----------ForkJoinPool.commonPool-worker-2
//    Task-3:---------end-----------ForkJoinPool.commonPool-worker-1
//    所有任务都完成了!
}
