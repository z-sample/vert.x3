package org.example.verticle;

import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.*;

/**
 * @author Zero
 *         Created on 2016/11/29.
 */
public class APIVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(APIVerticle.class);

    @Override
    public void start(Future<Void> future) throws Exception {

        HttpServerOptions options = new HttpServerOptions();
        options.setCompressionSupported(true);
        options.setCompressionLevel(4);
        options.setMaxChunkSize(Integer.MAX_VALUE);
        HttpServer server = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        router.route("/api/*").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
            routingContext.next();
        }).failureHandler(ErrorHandler.create(true));//统一处理错误

        router.route("/api/xxx").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            if (routingContext.request().getParam("id") == null) {
                response.setStatusCode(400).setStatusMessage("id is required");
            } else {
                response.end("{}");
            }
        });

        router.exceptionHandler(throwable -> {
            logger.warn("Unhandled exceptions on this router", throwable);
        });

        server.requestHandler(router::accept).listen(8080);

    }


}
