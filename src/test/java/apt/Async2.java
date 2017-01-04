package apt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author Zero
 *         Created on 2017/1/3.
 */
public class Async2 {
    public static void main(String[] args) throws Exception {
//        Future.
//        CompositeFuture.()
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println(Thread.currentThread().getName());
//        });

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
//        }).join();

//        exec(() -> {
//
//        }, v -> {
//            System.out.println(v);
//        });

        exec(Async2.ns("name", () -> {
            System.out.println("第一个任务");
            return "zero";
        }), Async2.ns("name", () -> {
            System.out.println("第二个任务");
//            throw new RuntimeException("000");
            return "may";
        })).thenAccept(v -> {
            System.out.println("---------------");
            System.out.println(v);
        });
        Thread.sleep(1000);
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static CompletableFuture exec(Runnable... task) {
//        CompletableFuture[] futures = new CompletableFuture[task.length];
//        for (int i = 0; i < task.length; i++) {
//            futures[i] = CompletableFuture.runAsync(task[i]);
//        }
//        return CompletableFuture.allOf(futures);
//    }
//
//    public static CompletableFuture<Void> exec(Runnable runnable, Consumer<Void> action) {
//        return CompletableFuture.runAsync(runnable).thenAcceptAsync(action);
//    }


    public static CompletableFuture<Result> exec(NameSupplier<?>... task) {
        Map<String, Object> rs = new HashMap<>(task.length);
        Result result = new Result(rs);
        CompletableFuture<?>[] futures = new CompletableFuture[task.length];
        AtomicInteger index = new AtomicInteger(0);
        for (; index.get() < task.length; index.getAndIncrement()) {
            int i = index.get();
            futures[i] = CompletableFuture.supplyAsync(task[i]);
            futures[i].thenAccept(aVoid -> {
                rs.put(task[i].name, futures[i].join());
            });
        }
        return CompletableFuture.allOf(futures).exceptionally(throwable -> {
            throwable.printStackTrace();
            result.setThrowable(throwable);
            return null;
        }).thenApply(aVoid -> result);
    }

//    public static CompletableFuture<Void> exec(Runnable r1, Runnable r2, Consumer<Void> action) {
//        return CompletableFuture.allOf(
//                CompletableFuture.runAsync(r1),
//                CompletableFuture.runAsync(r2)
//        ).thenAcceptAsync(action);
//    }

    public static NameRunnable nr(String name, Runnable runnable) {
        return new NameRunnable(name) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public static <T> NameSupplier ns(String name, Supplier<T> supplier) {
        return new NameSupplier<T>(name) {
            @Override
            public T get() {
                return supplier.get();
            }
        };
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
    }

    public static abstract class NameRunnable implements Runnable {
        public final String name;

        public NameRunnable(String name) {
            this.name = name;
        }
    }

    public static abstract class NameSupplier<T> implements Supplier<T> {
        public final String name;

        public NameSupplier(String name) {
            this.name = name;
        }

    }


}
