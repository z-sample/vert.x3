package org.example.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class MockLogin extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
//        Runner.runExample(HttpProxy.class);
        Vertx vertx = Vertx.vertx();
        String host = "github.com";
        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true));
        client.request(HttpMethod.POST, 443, "github.com", "/session", response -> {
            response.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
        })
                .putHeader("Host", host)
                .putHeader("Connection", "keep-alive")
                .putHeader("Upgrade-Insecure-Requests", "1")
                .putHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .putHeader("Origin", host)
                .putHeader("Accept", "text/html")
                .putHeader("Accept-Encoding", "text/html")
                .putHeader("Referer", host + "/")
//                .putHeader("Accept-Encoding", "gzip,deflate")
                .putHeader("Accept-Encoding", "chunked")
                .putHeader("Cookie", "GH1.1.1308919905.1489144352; logged_in=no; _gh_sess=eyJzZXNzaW9uX2lkIjoiMzA0MTFmYjBhMDkxNGI5ODYyZGUxZDZjZDMyMzEzNzgiLCJfY3NyZl90b2tlbiI6InN2aHhGOWRTeGxrYXBITnBaSlFqOTJsSGo1alAxVGlEVnFwMkZEZ0EvbE09IiwicmVmZXJyYWxfY29kZSI6Imh0dHBzOi8vZ2l0aHViLmNvbS8iLCJmbGFzaCI6eyJkaXNjYXJkIjpbXSwiZmxhc2hlcyI6eyJhbmFseXRpY3NfbG9jYXRpb25fcXVlcnlfc3RyaXAiOiJ0cnVlIn19fQ%3D%3D--a7a384a78c439c6a684a652dd0debf141649f855")
                .setChunked(true)
                .write("utf8=%E2%9C%93&authenticity_token=BDSPBFvNff%2Bby65pryc3RrR%2Fi2f4kqqEIPTRb%2Bxs086EqdPdNTs7Xgc3orTN1mBgOQngubBcOxWep35N2Dt73w%3D%3D&login=qq&password=eee&commit=Sign+in")
                .end();//request()方法请求时,必须调用end()方法
    }

}