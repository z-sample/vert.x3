package org.example.eventbus;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.impl.CodecManager;
import io.vertx.core.eventbus.impl.codecs.JsonObjectMessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * @author Zero
 *         Created on 2016/12/2.
 */
public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();


        //自定义MessageCodec
//        MessageCodec jsonCodec = new CustomMessageCodec();
//        DeliveryOptions options = new DeliveryOptions().setCodecName(jsonCodec.name());
//        vertx.eventBus().registerCodec(jsonCodec).send("news.uk.sport",new MyPoJo(),options);

        vertx.deployVerticle(ConsumerVerticle.class.getName());
        vertx.deployVerticle(ProducerVerticle.class.getName());

        //vert.x实现了多种类型的MessageCodec,可以查看实现类或者在CodecManager中查看
        vertx.setTimer(2000, e -> {
            vertx.eventBus().send("bus.json",new JsonObject("{\"name\":\"zero\"}"));
        });

    }
}
