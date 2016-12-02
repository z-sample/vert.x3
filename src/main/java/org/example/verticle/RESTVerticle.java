package org.example.verticle;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.*;

/**
 * @author Zero
 *         Created on 2016/11/29.
 */
public class RESTVerticle implements Verticle {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    /**
     * Reference to the Vert.x instance that deployed this verticle
     */
    protected Vertx vertx;

    /**
     * Reference to the context of the verticle
     */
    protected Context context;

    /**
     * Get the Vert.x instance
     *
     * @return the Vert.x instance
     */
    @Override
    public Vertx getVertx() {
        return vertx;
    }

    /**
     * Initialise the verticle.<p>
     * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.
     *
     * @param vertx   the deploying Vert.x instance
     * @param context the context of the verticle
     */
    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.context = context;
    }


    @Override
    public void start(Future<Void> future) throws Exception {
        Router router = Router.router(vertx);

        // CORS support
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(BodyHandler.create());
        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        //类似AOP,给所有的响应都加上Header
        router.route().handler(event -> {
            event.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            event.next();
        });

        //
//        router.get("/login").handler(this::login);

        router.get("/users").handler(this::userList);
        router.get("/user/:uid").handler(this::user);
        router.delete("/user/:uid").handler(this::deleteUser);
//        router.put("/user/:uid").handler(this::xxx);
//        router.post("/user/:uid").handler(this::xxx);

        //Redirect auth handler : http://vertx.io/docs/vertx-auth-oauth2/java/


        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);
        future.complete();//必须调用complete()方法

    }


    static List<User> mockUsers = new ArrayList<>();

    static {
        mockUsers.add(new User("1", "zero"));
        mockUsers.add(new User("2", "frank"));
        mockUsers.add(new User("3", "may"));
        mockUsers.add(new User("4", "cindy"));
        mockUsers.add(new User("5", "JY"));
        mockUsers.add(new User("6", "cora"));
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }


    private void userList(RoutingContext routingContext) {
        routingContext.response().end(Json.encodePrettily(mockUsers));
    }

    private void user(RoutingContext rc) {
        String uid = rc.pathParam("uid");
        Optional<User> first = mockUsers.stream().filter(user -> user.getId().equals(uid)).findFirst();
        if (first.isPresent()) {
            rc.response().end(Json.encodePrettily(first.get()));
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", "404");
            jsonObject.put("errmsg", "用户不存在");
            rc.response().setStatusCode(404).end(jsonObject.encodePrettily());
        }
    }

    private void deleteUser(RoutingContext rc) {
        String uid = rc.pathParam("uid");
        Optional<User> first = mockUsers.stream().filter(user -> user.getId().equals(uid)).findFirst();
        if (first.isPresent()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", "0");
            jsonObject.put("errmsg", "OK");
            mockUsers.remove(first);
            rc.response().end(jsonObject.encodePrettily());
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", "404");
            jsonObject.put("errmsg", "用户不存在");
            rc.response().setStatusCode(404).end(jsonObject.encodePrettily());
        }
    }


}
