package org.example.dns;

import io.vertx.core.Vertx;
import io.vertx.core.dns.DnsClient;

import java.util.List;

/**
 * @author Zero
 *         Created on 2017/3/10.
 */
public class Dns {
    //windows下如何查看以及清除DNS缓存: http://jingyan.baidu.com/article/59a015e34fcd70f7948865a9.html
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DnsClient client = vertx.createDnsClient(53, "8.8.8.8");//DNS 服务器
        //解析后记得缓存到本地
        client.resolveCNAME("marstranslation.cn", ar -> {//该域名CNAME到哪
            if (ar.succeeded()) {
                System.out.println("succeeded");
                List<String> records = ar.result();
                for (String record : records) {
                    System.out.println(record);//zho-marstranslation.marstranslation.com
                }
            } else {
                System.out.println("Failed to resolve entry" + ar.cause());
            }
        });
    }
}
