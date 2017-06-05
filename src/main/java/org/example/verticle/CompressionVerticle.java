package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class CompressionVerticle extends AbstractVerticle {

    @Override
    public void start() {
        HttpServerOptions options = new HttpServerOptions();
        options.setCompressionSupported(true);
        options.setCompressionLevel(4);
        options.setMaxChunkSize(Integer.MAX_VALUE);
        HttpServer server = vertx.createHttpServer(options);
        Router router = Router.router(vertx);
        router.route("/").handler(context -> {
            HttpServerResponse response = context.response();
            System.out.println(Thread.currentThread().getName());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                builder.append("hello world\n");
            }
            response.end(builder.toString());

        });
        server.requestHandler(router::accept).listen(8080);
    }


}
