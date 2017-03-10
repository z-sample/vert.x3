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
        HttpClient client = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true).setDefaultPort(443));

        vertx.createHttpServer(options).requestHandler(clientRequest -> {
            System.out.println("Proxying request: " + clientRequest.uri());
            HttpClientRequest proxyRequest = client.request(HttpMethod.GET, 443, "github.com", clientRequest.uri(), response -> {
//                response.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
                response.bodyHandler(body -> {
                    System.out.println("Got data " + body.toString("ISO-8859-1"));
                    String charset = getCharset(response);
                    String encoding = response.headers().get("Content-Encoding");
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
                        MultiMap proxyRespHeaders = response.headers().set("Content-Encoding", "chunked").set("Transfer-Encoding", "chunked").set("Server","CCJK Translator");
                        clientRequest.response().headers().setAll(proxyRespHeaders);
                        reader.lines().forEach(line->{
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
            proxyRequest.headers().remove("Host");//必须移除Host
            proxyRequest.putHeader("Accept-Encoding", "gzip,deflate");
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