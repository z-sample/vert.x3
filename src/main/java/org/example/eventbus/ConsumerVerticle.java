package org.example.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * 如果已经安装了Vert.x, 可以通过<code>vertx run VerticleOne.java</code>部署Verticle
 */
public class ConsumerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();
        MessageConsumer<String> consumer = eventBus.consumer("news.uk.sport");
        consumer.handler(message -> System.out.println(message.body()));
//        consumer.unregister();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}