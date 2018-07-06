package org.example.ext;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Zero
 *         Created on 2017/6/5.
 */
public interface HttpHandler extends Handler<RoutingContext> {

    @Override
    default void handle(RoutingContext event) {
        /*if (event instanceof HttpContext) {
            handle((HttpContext)event);
        } else {
            handle(new HttpContext(event));
        }*/
        
        HttpContext context = event.get("$");
        if (context == null) {
            context = new HttpContext(event);
            event.put("$", context);
        }
    }

    void handle(HttpContext event);

    static Handler<RoutingContext> adapt(HttpHandler handler) {
        return handler;
    }

}
