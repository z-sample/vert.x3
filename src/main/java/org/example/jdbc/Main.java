package org.example.jdbc;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import javax.xml.ws.AsyncHandler;
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
            ar.result().query("select version()", rs -> {
                rs.result().getRows().forEach(System.out::println);
                ar.result().query("select * from user", r -> {
                    if (r.failed()) {
                        r.cause().printStackTrace();
                    } else {
                        r.result().getRows().forEach(System.out::println);
                        ar.result().close();
                    }
                });
            });
        });


        client.getConnection(ar -> {
            SQLConnection conn = ar.result();
            conn.query("select * from user", r -> {
                //...
                r.result().getRows().forEach(user -> {
                    JsonArray params = new JsonArray().add(user.getInteger("user_id"));
                    conn.queryWithParams("select * from project where user_id=?", params, r2 -> {
                        //...
                        if (r2.succeeded()) {
                            r2.result().getResults().forEach(System.out::println);
                        }
                    }).close();
                });
            });
        });

        client.getConnection(ar -> {
            SQLConnection conn = ar.result();
            Handler<AsyncResult<ResultSet>> handleProject = r -> {
                //...
                if (r.succeeded()) {
                    r.result().getResults().forEach(System.out::println);
                }
            };
            Handler<AsyncResult<ResultSet>> handleUsers = r -> {
                //...
                r.result().getRows().forEach(user -> {
                    JsonArray params = new JsonArray().add(user.getInteger("user_id"));
                    conn.queryWithParams("select * from project where user_id=?", params, handleProject).close();
                });
            };
            conn.query("select * from user", handleUsers);
        });

    }
}
