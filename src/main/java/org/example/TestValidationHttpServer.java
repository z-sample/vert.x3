package org.example;

import io.vertx.core.*;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

public class TestValidationHttpServer extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route("/test/").handler(this::test);
        server.requestHandler(router::accept).listen(8081);
    }

    @Option
    private void test(RoutingContext context) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validateValue(User.class, "name", null);
        //User user=Validations.convert(context,User.class);
        HttpServerResponse response = context.response();
//        context.request().get
        System.out.println(Thread.currentThread().getName());
        response.end(DateFormat.getDateTimeInstance().format(new Date()) + " : " + Thread.currentThread().getName());
    }

    private static class User{

        @NotNull
        public String name;



    }



    //ab -n 200000 -c 1000 http://localhost:8081/test
    public static void main(String[] args) {
//        VertxOptions vo = new VertxOptions();
//        Vertx vertx = Vertx.vertx(vo);
//        DeploymentOptions options = new DeploymentOptions();
//        options.setInstances(1);
//        vertx.deployVerticle(TestValidationHttpServer.class.getName(), options);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> set = validator.validateValue(User.class, "name", null);
        System.out.println(set);

    }
}
