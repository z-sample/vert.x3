package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Zero
 *         Created on 2016/11/29.
 */
public class NotFoundHandler implements Handler<RoutingContext> {

    private static NotFoundHandler handler = new NotFoundHandler();

    public static NotFoundHandler create() {
        return handler;
    }
    
    @Override
    public void handle(RoutingContext context) {
        if (context.user() == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", "404");
            jsonObject.put("errmsg", "Not Found");
            context.response().setStatusCode(404).end(jsonObject.encodePrettily());
        } else {
            context.next();
        }
    }

}
