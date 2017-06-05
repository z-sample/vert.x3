package org.example.demo;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.example.model.User;

import javax.sql.DataSource;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author Zero
 *         Created on 2017/4/5.
 */
public class UserHandler {


    public static void install(Router router) {
        UserHandler handler = new UserHandler();
        router.get("/users").handler(handler::get_users);


        JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false")
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("max_pool_size", 30);


    }

    private DataSource dataSource;

    private void get_users(RoutingContext context) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = dataSource.getConnection();
                ResultSet resultSet = connection.prepareStatement("").executeQuery();
                ResultSetMetaData metaData = resultSet.getMetaData();
//                connection.setSavepoint()
//                connection.prepareStatement("").setQueryTimeout();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            try {
                int rs = 10 / 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "";
        }, executor).whenComplete((rs, throwable) -> {
            System.out.println("--------1---------");
        }).exceptionally(throwable -> {
            System.out.println("--------2---------");
            return "";
        });
        executor.shutdown();
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                int rs = 10 / 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "";
        }, executor).whenComplete((rs, throwable) -> {
            System.out.println("--------1---------");
        }).exceptionally(throwable -> {
            System.out.println("--------2---------");
            return "";
        });
        executor.shutdown();
    }

}
