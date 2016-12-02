package org.example.auth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.example.handler.NotFoundHandler;
import org.example.handler.UnauthorizedHandler;

/**
 * @author Zero
 *         Created on 2016/11/29.
 */
public class AuthVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        //模拟登录
        router.route().handler(MockAuthHandler.create());


        //http://vertx.io/docs/vertx-web/java/#_creating_the_session_handler
        //Session,后面的请求会创建Session
        // We need a cookie handler first
        router.route().handler(CookieHandler.create());

        // Create a clustered session store using defaults
//        SessionStore store = ClusteredSessionStore.create(vertx);
        SessionStore store = LocalSessionStore.create(vertx);

        SessionHandler sessionHandler = SessionHandler.create(store);

        // Make sure all requests are routed through the session handler too
        router.route().handler(sessionHandler);

        //如果是未授权的用户返回401错误
        router.route().handler(UnauthorizedHandler.create());

        // Now your application handlers
        router.get("/principal").handler(this::userPrincipal);

        //404
        router.route().handler(NotFoundHandler.create());
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }



    private void userPrincipal(RoutingContext routingContext) {
        routingContext.response().end(Json.encodePrettily(routingContext.user().principal()));
    }

    private void fuck(RoutingContext routingContext) {
//        routingContext.user().isAuthorised("fuck",)
        routingContext.response().end(Json.encodePrettily(routingContext.user().principal()));
    }

    public static void main(String[] args) {
        VertxOptions vo = new VertxOptions();
        vo.setEventLoopPoolSize(1);
        Vertx vertx = Vertx.vertx(vo);
        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(2);
        vertx.deployVerticle(AuthVerticle.class.getName(), options);
    }


}
