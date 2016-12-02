package org.example.jdbc;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.io.IOException;

/**
 * @author Zero
 *         Created on 2016/12/2.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false")
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("max_pool_size", 30);

        Vertx vertx = Vertx.vertx();
        JDBCClient client = JDBCClient.createShared(vertx, config);
        client.getConnection(ar -> {
            ar.result().query("select * from user", rs -> {
                rs.result().getRows().forEach(System.out::println);
            });
        });

    }
}
