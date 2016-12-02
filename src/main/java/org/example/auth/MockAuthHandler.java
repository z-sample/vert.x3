package org.example.auth;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Zero
 *         Created on 2016/11/29.
 */
public class MockAuthHandler implements Handler<RoutingContext> {

    private static MockAuthHandler handler = new MockAuthHandler();

    public static MockAuthHandler create() {
        return handler;
    }

    @Override
    public void handle(RoutingContext context) {
        context.setUser(new AuthUser("zero"));
        context.next();
    }

}
