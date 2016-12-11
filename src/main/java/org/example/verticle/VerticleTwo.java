package org.example.verticle;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

public class VerticleTwo implements Verticle {
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
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println("VerticleTwo : "+Thread.currentThread().getName());

        startFuture.complete();
//        System.out.println(Thread.currentThread().getName());
//        System.out.println(VerticleTwo.class.getName() + " start: " + (startFuture.succeeded() ? "succeeded" : "fail"));
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }


}