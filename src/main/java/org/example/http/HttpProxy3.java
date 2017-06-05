package org.example.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.dns.DnsClient;
import io.vertx.core.http.*;
import io.vertx.core.net.JksOptions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.util.List;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HttpProxy3 extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
//        Runner.runExample(HttpProxy.class);
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(HttpProxy3.class.getName());
    }

    //    .put("type", "jceks")
//    .put("path", keyPath)
//    .put("password", "secret"));
    @Override
    public void start() throws Exception {
        HttpServerOptions options = new HttpServerOptions();
        JksOptions jksOptions = new JksOptions();
        String keyPath = System.getProperty("user.dir") + "/src/main/resources/server-keystore.jks";
        jksOptions.setPath(keyPath).setPassword("wibble");
        options.setSsl(true).setKeyStoreOptions(jksOptions);
//        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true).setDefaultPort(443));
        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(false).setTrustAll(true).setDefaultPort(443));
        vertx.createHttpServer(options).requestHandler(clientRequest -> {
            System.out.println("Proxying request: " + clientRequest.uri());
//            String host = "github.com";
            String host = "www.iteye.com";
            HttpClientRequest proxyRequest = client.request(clientRequest.method(), 80, host, clientRequest.uri(), response -> {
//                response.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
                response.bodyHandler(body -> {
                    System.out.println("Got data " + body.toString("ISO-8859-1"));
                    String charset = getCharset(response);
                    String encoding = response.headers().get("Content-Encoding");
                    if (encoding == null) {
                        System.out.println(body);
                        //TODO sdsd
                        clientRequest.response().end(body);
                        return;
                    }
                    try {
                        BufferedReader reader;
                        if ("gzip".equalsIgnoreCase(encoding)) {
                            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(body.getBytes())), charset));
                        } else if ("deflate".equalsIgnoreCase(encoding)) {
                            reader = new BufferedReader(new InputStreamReader(new DeflaterInputStream(new ByteArrayInputStream(body.getBytes())), charset));
                        } else {
                            clientRequest.response().end("Un content encoding:" + encoding);
                            return;
                        }
                        //You must set the Content-Length header to be the total size of the message body BEFORE sending any data if you are not using HTTP chunked encoding.
                        clientRequest.response().setChunked(true);//不然需要设置Content-Length
                        //不压缩使用chunked
                        MultiMap proxyRespHeaders = response.headers().set("Content-Encoding", "chunked").set("Transfer-Encoding", "chunked").set("Server", "CCJK Translator");
                        proxyRespHeaders.set("Host", clientRequest.getHeader("Host"));
                        clientRequest.response().headers().setAll(proxyRespHeaders);
                        List<String> cookies = response.cookies();
                        for (int i = 0; i < cookies.size(); i++) {
                            cookies.set(i, cookies.get(i).replace("iteye.com", "wl.com"));
                        }
                        clientRequest.response().putHeader("Set-Cookie", cookies);
                        List<String> all = clientRequest.response().headers().getAll("Set-Cookie");
                        System.out.println(cookies);
                        reader.lines().forEach(line -> {
                            clientRequest.response().write(line).write("\n");
                        });
                        reader.close();
                        clientRequest.response().end();
                    } catch (Exception e) {
                        e.printStackTrace();
                        clientRequest.response().end("error:" + e.getMessage());
                    }
                });
            }); //proxyRequest .end();//request()方法请求时,必须调用end()方法

//            proxyRequest.setChunked(true);
            proxyRequest.headers().setAll(clientRequest.headers());//
            System.out.println("-------------old------------------");
            clientRequest.headers().forEach(System.out::println);
//            proxyRequest.headers().remove("Host");//必须移除Host
            proxyRequest.putHeader("Host", host);
            proxyRequest.putHeader("Connection", "keep-alive");
//            proxyRequest.putHeader("Upgrade-Insecure-Requests", "1");
            proxyRequest.putHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            proxyRequest.putHeader("Origin", host);
            proxyRequest.putHeader("Accept", "text/html");
            proxyRequest.putHeader("Accept-Encoding", "text/html");
//            proxyRequest.putHeader("Referer",clientRequest.getHeader("Referer").replace(clientRequest.getHeader("Host"),host));
            proxyRequest.putHeader("Referer", host + "/");
            proxyRequest.putHeader("Accept-Encoding", "gzip,deflate");
            proxyRequest.putHeader("Cache-Control", "max-age=0");
//            proxyRequest.putHeader("Cookie", "GH1.1.1308919905.1489144352; logged_in=no; _gh_sess=eyJzZXNzaW9uX2lkIjoiMzA0MTFmYjBhMDkxNGI5ODYyZGUxZDZjZDMyMzEzNzgiLCJfY3NyZl90b2tlbiI6InN2aHhGOWRTeGxrYXBITnBaSlFqOTJsSGo1alAxVGlEVnFwMkZEZ0EvbE09IiwicmVmZXJyYWxfY29kZSI6Imh0dHBzOi8vZ2l0aHViLmNvbS8iLCJmbGFzaCI6eyJkaXNjYXJkIjpbXSwiZmxhc2hlcyI6eyJhbmFseXRpY3NfbG9jYXRpb25fcXVlcnlfc3RyaXAiOiJ0cnVlIn19fQ%3D%3D--a7a384a78c439c6a684a652dd0debf141649f855");
            System.out.println("-------------new------------------");
            proxyRequest.headers().forEach(System.out::println);
//            proxyRequest.write(clientRequest.body);
//            proxyRequest.setChunked(true);
            clientRequest.handler(data -> {
                System.out.println("Proxying request body: " + data.toString("ISO-8859-1"));
                proxyRequest.putHeader("Content-Length", "" + data.length());
                proxyRequest.write(data);
            });
            /**
             POST /session HTTP/1.1
             Host: github.com
             Connection: keep-alive
             Content-Length: 169
             Cache-Control: max-age=0
             Origin: https://github.com
             Upgrade-Insecure-Requests: 1
             User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
             Content-Type: application/x-www-form-urlencoded
             Accept: text/html
             Referer:xxx
             Accept-Encoding: gzip, deflate, br
             Accept-Language: zh-CN,zh;q=0.8
             Cookie: _octo=GH1.1.1308919905.1489144352; logged_in=no; _gh_sess=eyJzZXNzaW9uX2lkIjoiMzA0MTFmYjBhMDkxNGI5ODYyZGUxZDZjZDMyMzEzNzgiLCJfY3NyZl90b2tlbiI6InN2aHhGOWRTeGxrYXBITnBaSlFqOTJsSGo1alAxVGlEVnFwMkZEZ0EvbE09IiwicmVmZXJyYWxfY29kZSI6Imh0dHBzOi8vZ2l0aHViLmNvbS8iLCJmbGFzaCI6eyJkaXNjYXJkIjpbXSwiZmxhc2hlcyI6eyJhbmFseXRpY3NfbG9jYXRpb25fcXVlcnlfc3RyaXAiOiJ0cnVlIn19fQ%3D%3D--a7a384a78c439c6a684a652dd0debf141649f855
             */
            proxyRequest.exceptionHandler(event -> {
                event.printStackTrace();
            });
            clientRequest.endHandler((v) -> proxyRequest.end());
        }).listen(443);
    }

    private String getCharset(HttpClientResponse response) {
        String contentType = response.getHeader("Content-Type");
        if (contentType == null) return "utf-8";
        String[] kvs = contentType.split(";");
        return "utf-8";
    }
}