package org.example.verticle;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

public class Main {


    public static void main(String[] args) {
        VertxOptions vo = new VertxOptions();
        //16个事件循环(BOSS线程),要结合Instance, 实例数>=事件循环数
        vo.setEventLoopPoolSize(16);
        Vertx vertx = Vertx.vertx(vo);
        JsonObject config = vertx.fileSystem().readFileBlocking("conf.json").toJsonObject();

        DeploymentOptions options = new DeploymentOptions();
        options.setConfig(config);
        //100个Verticle实例,可以看到每个Verticle.start()方法会被调用一百次
//        options.setInstances(100);
        options.setInstances(10);

        //先部署的Verticle优先级高
        vertx.deployVerticle(RESTVerticle.class.getName(), options, ar -> {
            System.out.println("当start()调用complete()后会执行到这里");
            if (ar.succeeded()) {
                System.out.println("deploymentID: " + ar.result());//deploymentID
                //vertx.undeploy(ar.result()); //撤销部署
            }
        });
        vertx.deployVerticle(VerticleOne.class.getName(), options);
        vertx.deployVerticle(VerticleTwo.class.getName(), options);


        DeploymentOptions options2 = new DeploymentOptions();
        options2.setWorker(true).setHa(false);
        options2.setInstances(20);
        vertx.deployVerticle(WorkerVerticle.class.getName(), options2);

    }


}