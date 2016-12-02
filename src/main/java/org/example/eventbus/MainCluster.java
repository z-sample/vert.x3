package org.example.eventbus;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * 多个vert.x实例模拟
 *
 * @author Zero
 *         Created on 2016/12/2.
 */
public class MainCluster {
    public static void main(String[] args) {
        //是在多个vert.x实例上的测试,需要添加io.vertx.core.spi.cluster.ClusterManager实现(也是使用Java提供的服务发现实现可插拔)
        //vert.x提供了多个实现,这里使用hazelcast, 只需要添加vertx-hazelcast依赖
        //默认配置在vertx-hazelcast.jar包里:default-cluster.xml
        //在vert.x技术栈内部通讯使用没有问题,如果涉及到跨语言(vertx之外的语言)需求,可以使用MQ中间件,或者自己实现
        VertxOptions options = new VertxOptions();
        Vertx.clusteredVertx(options, event -> {
            System.out.println(event.succeeded());
            if (event.succeeded()) {
                event.result().deployVerticle(ConsumerVerticle.class.getName());
            }
        });
        Vertx.clusteredVertx(options, event -> {
            System.out.println(event.succeeded());
            if (event.succeeded()) {
                event.result().deployVerticle(ProducerVerticle.class.getName());
            }
        });
        Vertx.clusteredVertx(options, event -> {
            System.out.println(event.succeeded());
            if (event.succeeded()) {
                event.result().setTimer(1000, e -> event.result().eventBus().send("news.uk.sport", "--------"));
            }
        });


    }
}
