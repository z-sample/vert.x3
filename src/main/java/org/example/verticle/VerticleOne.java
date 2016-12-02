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
        Handler<HttpServerRequest> handler = e -> {
            HttpServerResponse response = e.response();
            System.out.println(Thread.currentThread().getName());
            response.putHeader("content-type", "application/json").end("Hello world");
        };
        vertx.createHttpServer().requestHandler(handler).listen(8080);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}