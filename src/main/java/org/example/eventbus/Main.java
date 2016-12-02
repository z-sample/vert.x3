package org.example.eventbus;

import io.vertx.core.Vertx;

/**
 * @author Zero
 *         Created on 2016/12/2.
 */
public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ConsumerVerticle.class.getName());
        vertx.deployVerticle(ProducerVerticle.class.getName());
    }
}
