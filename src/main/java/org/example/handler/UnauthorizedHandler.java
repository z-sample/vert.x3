package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Zero
 *         Created on 2016/11/29.
 */
public class UnauthorizedHandler implements Handler<RoutingContext> {

    private static UnauthorizedHandler handler = new UnauthorizedHandler();

    public static UnauthorizedHandler create() {
        return handler;
    }

    @Override
    public void handle(RoutingContext context) {
        if (context.user() == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", "401");
            jsonObject.put("errmsg", "Unauthorized");
            context.response().setStatusCode(401).end(jsonObject.encodePrettily());
        } else {
            context.next();
        }
    }


}
