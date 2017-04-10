package org.example.ext;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Zero
 *         Created on 2017/4/7.
 */
public interface RoutingHandler extends Handler<RoutingContext> {

    void handle(RouteContext event);

    @Override
    default void handle(RoutingContext event) {
        handle(new RouteContext(event));
    }

    static Handler<RoutingContext> ext(RoutingHandler handler) {
        return event -> handler.handle(new RouteContext(event));
    }

    /*
    static Handler<RoutingContext> ext(RoutingHandler handler) {
        return new ExtHandler(handler);
    }

    class ExtHandler implements Handler<RoutingContext> {
        private RoutingHandler handler;
        private ExtHandler(RoutingHandler handler) {
            this.handler = handler;
        }
        public void handle(RoutingContext context) {
            handler.handle(new RouteContext(context));
        }
    }
    */

}
