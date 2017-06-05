package org.example.demo;

import io.vertx.core.Vertx;

/**
 * @author Zero
 *         Created on 2017/6/5.
 */
public class Blocking {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.<String>executeBlocking(future -> {
            // Do the blocking operation in here
            // Imagine this was a call to a blocking API to get the result
            try {
                Thread.sleep(500);
            } catch (Exception ignore) {
            }
            String result = "armadillos!";
            future.complete(result);
        }, res -> {
            if (res.succeeded()) {
                //TODO do something
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
