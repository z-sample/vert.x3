package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * DeploymentOptions options = new DeploymentOptions();
 * options.setWorker(true);
 * options.setInstances(100);//启动100个实例
 * vertx.deployVerticle(WorkerVerticle.class.getName(),options);
 * </pre>
 */
public class WorkerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        try {
            System.out.println("WorkerVerticle : " + Thread.currentThread().getName());//WorkerVerticle : vert.x-worker-thread-2
            Thread.sleep(3000);//worker线程可以执行比较耗时间的任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        VertxOptions vo = new VertxOptions();
        Vertx vertx = Vertx.vertx(vo);
        DeploymentOptions options = new DeploymentOptions();
        options.setWorker(true).setHa(false);
        options.setInstances(20);
        vertx.deployVerticle(WorkerVerticle.class.getName(), options);

    }

}