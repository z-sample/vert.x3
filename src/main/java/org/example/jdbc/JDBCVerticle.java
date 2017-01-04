package org.example.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.example.model.User;

import java.util.Optional;

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
        client.getConnection(ar -> {
            if (ar.succeeded()) {
                SQLConnection connection = ar.result();
                connection.query("select * from user", qar -> {
                    if (qar.succeeded()) {
//                        Optional<User> optional = qar.result().getRows().stream().map(User::new).findFirst();
                        Optional<User> optional = qar.result().getRows().stream().map(j -> Json.decodeValue(j.encode(), User.class)).findFirst();
                    }

                });
            }

        });


    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}