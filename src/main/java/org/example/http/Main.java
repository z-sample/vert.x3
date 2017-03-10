package org.example.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.net.JksOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Main extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
//        Runner.runExample(HttpProxy.class);
        Vertx vertx = Vertx.vertx();
        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true));
//        client .getNow(443, "github.com", "https://github.com/explore", resp -> {
//            System.out.println("Got response " + resp.statusCode());
//            resp.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
//        });

         client.request(HttpMethod.GET, 443, "github.com","https://github.com/explore", response -> {
             response.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
        }).end();//request()方法请求时,必须调用end()方法
    }

}