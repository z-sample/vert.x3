package org.example.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HttpProxy2 extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
//        Runner.runExample(HttpProxy.class);
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(HttpProxy2.class.getName());
    }
    @Override
    public void start() throws Exception {
        HttpServerOptions options = new HttpServerOptions();
        JksOptions jksOptions = new JksOptions();
        String keyPath = System.getProperty("user.dir") + "/src/main/resources/server-keystore.jks";
        jksOptions.setPath(keyPath).setPassword("wibble");
        options.setSsl(true).setKeyStoreOptions(jksOptions);
        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(false));

        vertx.createHttpServer(options).requestHandler(req -> {
            System.out.println("Proxying request: " + req.uri());
//            req.response().end("8888");
//            HttpClientRequest c_req = client.request(req.method(), 443, "www.marstranslation.com", req.uri(), c_res -> {
            HttpClientRequest c_req = client.request(req.method(), 8080, "localhost", req.uri(), c_res -> {
                System.out.println("Proxying response: " + c_res.statusCode());
                req.response().setChunked(true);
                req.response().setStatusCode(c_res.statusCode());
                req.response().headers().setAll(c_res.headers());
                c_res.handler(data -> {
                    System.out.println("Proxying response body: " + data.toString("ISO-8859-1"));
                    req.response().write(data);
                });
                c_res.endHandler((v) -> req.response().end());
            });
            c_req.setChunked(true);
            c_req.headers().setAll(req.headers());
            req.handler(data -> {
                System.out.println("Proxying request body " + data.toString("ISO-8859-1"));
                c_req.write(data);
            });
            req.endHandler((v) -> c_req.end());
        }).listen(443);
    }
}