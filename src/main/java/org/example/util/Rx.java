package org.example.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Zero
 *         Created on 2017/3/27.
 */
public class Rx {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            test();
        }
        Thread.sleep(4000);
    }

    private static void test() {
        Rx.Fork(task -> {
            System.out.println("-----1--------" + Thread.currentThread().getName());
            task.next("hello");
        }).fork(task -> {
            System.out.println(task.getResult());
            System.out.println("------2-------" + Thread.currentThread().getName());
            System.out.println(task.getResult());
            task.next("world");
        }).fork(task -> {
            System.out.println("------3-------" + Thread.currentThread().getName());
            task.next();
        }).join(task -> {
            System.out.println("------4-------" + Thread.currentThread().getName());
        }).end();
        System.out.println("---------0000000000000-----------------" + Thread.currentThread().getName());
        System.out.println();
    }
    //

    static ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 4, new WorkerThreadFactory(), null, false);

    private Context context = new Context();

    public static Rx Fork(ForkFun fun) {
        Rx first = new Rx();
        first.fork(fun);
        return first;
    }


    public Rx fork(ForkFun fun) {
        context.execQueue.add(fun);
        return this;
    }

    public Rx join(JoinFun fun) {
        context.execQueue.add(fun);
        return this;
    }


    public void end() {
        context.next();
        context.execQueue.add(context -> {

        });
    }


    public static class Context {

        Queue<Fun> execQueue = new LinkedList<>();
        private Object result;
        private Map<String, Object> data;

        public void next() {
            next(null);
        }

        public void next(Object rs) {
            this.result = rs;
            Fun fun = execQueue.poll();
            if (fun != null) {
                if (fun instanceof ForkFun) {
                    CompletableFuture.runAsync(() -> {
                        fun.apply(this);
                    }, forkJoinPool);
                } else {
                    fun.apply(this);
                }
            } else {
                System.out.println("---clear-data---");
            }
        }

        public void put(String key, Object value) {
            if (data == null) {
                data = new HashMap<>();
            }
            data.put(key, value);
        }

        public <T> T get(String key) {
            if (data == null) {
                return null;
            }
            return (T) data.get(key);
        }


        public Object getResult() {
            return result;
        }
    }

    public interface Fun {
        void apply(Context context) /*throws Exception*/;
    }

    public interface ForkFun extends Fun {

    }

    public interface JoinFun extends Fun {

    }

    static final class WorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public WorkerThreadFactory() {
            namePrefix = "Worker-";
        }

        public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            return new MyThread(pool, namePrefix + threadNumber.getAndIncrement());
        }
    }

/*    private static class MyThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory() {
            namePrefix = "Worker-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new MyThread(r, namePrefix + threadNumber.getAndIncrement(), new HashMap<>());
            t.setDaemon(false);
            t.setPriority(0);
            return t;
        }
    }*/

    private static class MyThread extends ForkJoinWorkerThread {
        private Map<String, Object> data = new HashMap<>();

        protected MyThread(ForkJoinPool pool, String name) {
            super(pool);
            setName(name);
        }

        public Map<String, Object> getData() {
            return data;
        }
    }


}
