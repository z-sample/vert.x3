package org.example.verticle;

import io.vertx.core.*;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * 如果已经安装了Vert.x, 可以通过<code>vertx run VerticleOne.java</code>部署Verticle
 */
public class VerticleOne extends AbstractVerticle {

    @Override
    public void start() {
        System.out.println("VerticleOne : "+Thread.currentThread().getName());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}