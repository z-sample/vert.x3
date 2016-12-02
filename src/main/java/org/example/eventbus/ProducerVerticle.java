package org.example.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * 如果已经安装了Vert.x, 可以通过<code>vertx run VerticleOne.java</code>部署Verticle
 */
public class ProducerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();
        vertx.setPeriodic(3000, e -> {
            //这个地址可以随便起,consumer保持一致即可
            eventBus.publish("news.uk.sport", "Yay! Someone kicked a ball");
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}