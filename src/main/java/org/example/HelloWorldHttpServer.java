package org.example;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

/**
 * @author Zero
 *         Created on 2016/11/28.
 */
public class HelloWorldHttpServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.executeBlocking(future -> {
            // Call some blocking API that takes a significant amount of time to return
//            String result = someAPI.blockingMethod("hello");
            future.complete("hello");
        }, event -> {

        });
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(event -> {
//            System.out.println(event.absoluteURI());
            event.response().end("hello world!");
        }).listen(8080);
    }
}
