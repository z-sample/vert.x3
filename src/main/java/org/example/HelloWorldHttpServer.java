package org.example;

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
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(event -> {
            System.out.println(event.absoluteURI());
            event.response().end("hello world!");
        }).listen(8080);
    }
}
