package org.example.auth.jwt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;

/**
 * <pre>
 * Standard fields[edit]
 * The Internet drafts define the following standard fields ("claims") that can be used inside a JWT claim set:
 *
 * Issuer (iss) - identifies principal that issued the JWT;
 * Subject (sub) - identifies the subject of the JWT;
 * Audience (aud) - The "aud" (audience) claim identifies the recipients that the JWT is intended for. Each principal intended to process the JWT MUST identify itself with a value in the audience claim. If the principal processing the claim does not identify itself with a value in the aud claim when this claim is present, then the JWT MUST be rejected.
 * Expiration time (exp) - The "exp" (expiration time) claim identifies the expiration time on or after which the JWT MUST NOT be accepted for processing.
 * Not before (nbf) - Similarly, the not-before time claim identifies the time on which the JWT will start to be accepted for processing.
 * Issued at (iat) - The "iat" (issued at) claim identifies the time at which the JWT was issued.
 * JWT ID (jti) - case sensitive unique identifier of the token even among different issuers.
 * The following fields can be used in authentication headers:
 *
 * Token type (typ) - If present, it is recommended to set this to JWT.[9]
 * Content type (cty) - If nested signing or encryption is employed, it is recommended to set this to JWT, otherwise omit this field.[9]
 * Message authentication code algorithm (alg) - The issuer can freely set an algorithm to verify the signature on the token. However, some supported algorithms are insecure.[4]
 * All other headers introduced by JWS and JWE[6][7]
 * </pre>
 * Created with IntelliJ IDEA.
 * Date: 2016/12/4
 * Time: 11:10
 *
 * @author: Zero
 */
public class JwtVerticle extends AbstractVerticle {
    //claims:声明
    //realm:领域
    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().produces("application/json");
        //如果当前目录已经存在keystore.jceks需要预先删掉
        //keytool -genkeypair -keystore keystore.jceks -storetype jceks -storepass secret -keyalg EC -keysize 256 -alias ES256 -keypass secret -sigalg SHA256withECDSA -dname "CN=,OU=,O=,L=,ST=,C=" -validity 360
        String keyPath = System.getProperty("user.dir") + "/src/main/resources/keystore.jceks";
        JsonObject authConfig = new JsonObject().put("keyStore", new JsonObject()
                .put("type", "jceks")
                .put("path", keyPath)
                .put("password", "secret"));
        JWTAuth authProvider = JWTAuth.create(vertx, authConfig);

        //router.route("/get_token")
        router.route("/login").produces("application/json").handler(ctx -> {
            // this is an example, authentication should be done with another provider...
            if ("paulo".equals(ctx.request().getParam("username")) && "123456".equals(ctx.request().getParam("password"))) {
                JsonObject claims = new JsonObject().put("sub", "paulo");
                JWTOptions options = new JWTOptions();
                //这里是生成密钥时alias的参数值,alias只能是["HS256", "HS384", "HS512", "RS256", "RS384", "RS512", "ES256", "ES384", "ES512"]之一, 请看io.vertx.ext.auth.jwt.impl.JWT源码
                options.setAlgorithm("ES256");
                //options.setAlgorithm("SHA256withECDSA"); //错误的写法
                ctx.response().end(authProvider.generateToken(claims, options));
            } else {
                ctx.fail(401);
            }
        });

        //设置受保护目录,类似于过滤器
        router.route("/protected/*").produces("application/json").handler(JWTAuthHandler.create(authProvider)).failureHandler(context -> {
            //添加failureHandler,否则会使用默认的handle(RoutingContextImpl.next()), 返回的就是html了
            HttpServerResponse response = context.response().setStatusCode(context.statusCode());
            response.putHeader("WWW-Authenticate", "Bearer realm=\"Secure Area\" ");
//            response.putHeader("WWW-Authenticate", "Bearer realm=\"Secure Area\" error=\"invalid_token\",error_description=\"The access token expired\"");
//            response.putHeader("WWW-Authenticate", "OAuth realm=\"http://server.example.com/\", oauth_error=\"access token invalid\"");
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", context.statusCode());
            jsonObject.put("errmsg", context.response().getStatusMessage());
            response.end(jsonObject.encodePrettily());
        });

        router.route("/protected/somepage").handler(ctx -> {
            // some handle code...
        });
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    }

    //第一步: 访问http://localhost:8080/login?username=paulo&password=123456 获得jwt
    //{"typ":"JWT","alg":"ES256"}{"sub":"paulo","iat":1480854617}
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJzdWIiOiJwYXVsbyIsImlhdCI6MTQ4MDg1NDYxN30=.MEUCIBmXU8tUadkWcf8MfGDrWVPEWfUohPseaeNp3HkQ8n81AiEAqTcCkwLlsC7dYAFZ3_uoOUjpHqjL8YpvWb8Z19-KQLU=
    //第二步:访问http://localhost:8080/protected/1 ,并添加头"Authorization:Bearer {jwt}"
    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new JwtVerticle());
    }
}
