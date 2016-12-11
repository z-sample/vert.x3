package org.example.async;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.Router;

/**
 * @author Zero
 *         Created on 2016/12/5.
 */
public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept);
        Future<HttpServer> httpServerFuture = Future.future();
        httpServer.listen(httpServerFuture.completer());
        NetServer netServer = vertx.createNetServer();
        Future<NetServer> netServerFuture = Future.future();
        netServer.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket event) {
                System.out.println(Thread.currentThread().getName());
            }
        });
        netServer.listen(netServerFuture.completer());

        CompositeFuture.all(httpServerFuture, netServerFuture).setHandler(ar -> {
            System.out.println(Thread.currentThread().getName());
            if (ar.succeeded()) {
                // All servers started
            } else {
                // At least one server failed
            }

        });
    }
}
