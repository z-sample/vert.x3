package org.example.eventbus.cluster;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.example.eventbus.ConsumerVerticle;

/**
 * 多进程多vert.x模拟
 *
 * @author Zero
 *         Created on 2016/12/2.
 */
public class MainCluster2 {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        Vertx.clusteredVertx(options, event -> {
            System.out.println(event.succeeded());
            if (event.succeeded()) {
                event.result().deployVerticle(ConsumerVerticle.class.getName());
            }
        });

    }
}
