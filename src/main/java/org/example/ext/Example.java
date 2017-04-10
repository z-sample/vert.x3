package org.example.ext;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * @author Zero
 *         Created on 2017/4/7.
 */
public class Example {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.get("/").handler(event -> {
            event.response().end("OK");
        });

        //回调1
        router.get("/test").handler((RoutingHandler) event -> {
            event.render("test");
        });

        //回调2
        router.get("/test2").handler(RoutingHandler.ext(event -> {
            event.render("test1");
        }));

        //方法引用
        router.get("/test3").handler(RoutingHandler.ext(Example::test));

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router::accept).listen(8080);
    }

    private static void test(RouteContext context) {
        context.render("test4");
    }

}
