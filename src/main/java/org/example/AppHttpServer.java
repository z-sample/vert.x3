package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * https://github.com/vert-x3/vertx-examples/blob/master/kotlin-example/build.gradle
 * @author Zero
 *         Created on 2016/11/28.
 */
public class AppHttpServer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        Route route1 = router.route("/path/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            // enable chunked responses because we will be adding data as
            // we execute over other handlers. This is only required once and
            // only if several handlers do output.
            response.setChunked(true);
            response.write("route1\n");

            // Call the next matching route after a 5 second delay
            routingContext.vertx().setTimer(1000, tid -> routingContext.next());
        });
        router.route("/path/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.write("route2\n");
            routingContext.vertx().setTimer(2000, tid ->  routingContext.next());
        });

        router.route().handler(routingContext -> {
            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });
        server.requestHandler(router::accept).listen(8080);
    }
}
