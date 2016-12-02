package org.example.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * 如果已经安装了Vert.x, 可以通过<code>vertx run VerticleOne.java</code>部署Verticle
 */
public class JDBCVerticle extends AbstractVerticle {

    @Override
    public void start() {
        JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false")
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("max_pool_size", 30);

        JDBCClient client = JDBCClient.createShared(vertx, config);



    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}