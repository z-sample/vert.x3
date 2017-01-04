package apt;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.example.verticle.User;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * @author Zero
 *         Created on 2017/1/3.
 */
public class Async {
    public static void main(String[] args) throws Exception {
//        Future.
//        CompositeFuture.()
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println(Thread.currentThread().getName());
//        });
//        CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
//            System.out.println("这是第1个任务");
//        }), CompletableFuture.runAsync(() -> {
//            System.out.println("这是第2个任务");
//        }), CompletableFuture.runAsync(() -> {
//            System.out.println("这是第3个任务");
//        }));
//
//        CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> {
//            System.out.println("这是第1个任务");
//            return "a";
//        }), CompletableFuture.supplyAsync(() -> {
//            System.out.println("这是第2个任务");
//            return "b";
//        }), CompletableFuture.supplyAsync(() -> {
//            System.out.println("这是第3个任务");
//            return "c";
//        }));
//
//
//        exec(() -> {
//            sleep(5000);
//            System.out.println(Thread.currentThread().getName());
//        }, () -> {
//            sleep(5000);
//            System.out.println(Thread.currentThread().getName());
//        }, () -> {
//            sleep(5000);
//            System.out.println(Thread.currentThread().getName());
//        }, () -> {
//            sleep(5000);
//            System.out.println(Thread.currentThread().getName());
//        }, () -> {
//            sleep(50);
//            System.out.println("--" + Thread.currentThread().getName());
//        }, () -> {
//            sleep(20);
//            System.out.println("--" + Thread.currentThread().getName());
//        });


        Async.exec(Async.task("hot", () -> {
            sleep(1000);
            System.out.println("第一个任务");
            return Arrays.asList("a", "b", "c");
        }), Async.task("sport", () -> {
            System.out.println("第二个任务");
//            throw new RuntimeException("000");
            return Arrays.asList("d", "e", "f");
        }), Async.task("tech", () -> {
//            sleep(500);
            System.out.println("第三个任务");
            return Arrays.asList(1, 2, 3);
        })).thenApply(rs -> {
            List<Object> list = new ArrayList<>();
            if (rs.hasError()) {
                System.out.println(rs);
                return list;
            } else {
                System.out.println(rs.data().get("hot"));
                list.add(rs.data().values());
                return list;
            }
        }).thenAccept(r -> {
            System.out.println(r);
        }).join();

//        Async.exec(Async.task("hot", () -> {
//            sleep(500);
//            System.out.println("第一个任务");
//        }), Async.task("sport", () -> {
//            System.out.println("第二个任务");
//        }), Async.task("tech", () -> {
//            System.out.println("第三个任务");
////            throw new RuntimeException("000");
//        })).thenAccept(rs -> {
//
//        });
//        Thread.sleep(1000);

        Async.exec(new NameRunnable("hot", () -> {
            sleep(500);
            System.out.println("第一个任务");
        })).thenAccept(rs -> {

        });
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final Executor executor = Executors.newCachedThreadPool();

    public static CompletableFuture<Void> exec(Runnable... task) {
        CompletableFuture[] futures = new CompletableFuture[task.length];
        for (int i = 0; i < task.length; i++) {
            futures[i] = CompletableFuture.runAsync(task[i]);
        }
        return CompletableFuture.allOf(futures);
    }


    public static CompletableFuture<Result> exec(NameSupplier<?>... task) {
        return exec(true, task);
    }

    public static CompletableFuture<Result> exec(boolean cancelOnError, NameSupplier<?>... task) {
        Map<String, Object> rs = new HashMap<>(task.length);
        Result result = new Result(rs);
        CompletableFuture<?>[] futures = new CompletableFuture[task.length];
        AtomicInteger index = new AtomicInteger();
        for (; index.get() < task.length; index.getAndIncrement()) {
            final int i = index.get();
            CompletableFuture<?> future = CompletableFuture.supplyAsync(task[i]);
            future.exceptionally(throwable -> {
                result.setThrowable(throwable);
                if (cancelOnError) {
                    for (CompletableFuture f : futures) {
                        f.cancel(true);
                    }
                }
                return null;
            });
            futures[i] = future.thenAccept(aVoid -> rs.put(task[i].name, future.join()));
        }
        return CompletableFuture.allOf(futures).exceptionally(throwable -> {
            result.setThrowable(throwable);
            return null;
        }).thenApply(aVoid -> result);
    }


    public static NameRunnable task(String name, Runnable runnable) {
        return new NameRunnable(name, runnable);
    }

    public static <T> NameSupplier task(String name, Supplier<T> supplier) {
        return new NameSupplier<>(name, supplier);
    }

    public static class Result {
        private boolean error;
        private Throwable throwable;
        private Map<String, Object> data;

        public Result(Throwable throwable) {
            this.throwable = throwable;
            this.error = true;
        }

        public Result(Map<String, Object> data) {
            this.data = data;
        }

        public boolean hasError() {
            return error;
        }

        public Throwable throwable() {
            return throwable;
        }

        public Map<String, Object> data() {
            return data;
        }

        protected void setThrowable(Throwable throwable) {
            this.throwable = throwable;
            this.error = throwable != null;
        }

        @Override
        public String toString() {
            if (error) {
                final StringBuilder sb = new StringBuilder("{error:true,");
                sb.append("msg:").append(throwable.getMessage());
                sb.append("}");
                return sb.toString();
            } else {
                return Objects.toString(data);
            }
        }
    }


    public static class NameRunnable implements Runnable {
        public final String name;
        public final Runnable delegate;

        public NameRunnable(String name, Runnable supplier) {
            this.name = name;
            this.delegate = supplier;
        }

        @Override
        public void run() {
            delegate.run();
        }
    }

    public static class NameSupplier<T> implements Supplier<T> {
        public final String name;
        public final Supplier<T> delegate;

        public NameSupplier(String name, Supplier<T> supplier) {
            this.name = name;
            this.delegate = supplier;
        }

        @Override
        public T get() {
            return delegate.get();
        }
    }


}
