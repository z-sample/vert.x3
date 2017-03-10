package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

import java.text.DateFormat;
import java.util.Date;

public class TestHttpServer extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route("/test/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            System.out.println(Thread.currentThread().getName());
            response.end(DateFormat.getDateTimeInstance().format(new Date()) + " : " + Thread.currentThread().getName());
        });
        server.requestHandler(router::accept).listen(8081);
    }

    //ab -n 200000 -c 1000 http://localhost:8081/test
    public static void main(String[] args) {
        VertxOptions vo = new VertxOptions();
        vo.setEventLoopPoolSize(16);//16
        Vertx vertx = Vertx.vertx(vo);
        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(50);//30s
        //20:65M,39T
        //1:69M,22T

        vertx.deployVerticle(TestHttpServer.class.getName(),options);

    }
}
