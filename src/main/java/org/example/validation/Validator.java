package org.example.validation;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zero
 *         Created on 2016/12/5.
 */
public class Validator {

    private RoutingContext context;
    private HttpServerRequest request;

    private List<Field> fields = new ArrayList<>();

    public Validator(RoutingContext context) {
        this.context = context;
        request = context.request();
    }



    public boolean hasErrors() {
        for (Field f : fields) {
            if (f.error) {
                return true;
            }
        }
        return false;
    }

//    public void require()

    public StringField field(String name) {
        return new StringField(name, request.getParam(name));
    }

    public IntField intField(String name) {
        return new IntField(name, request.getParam(name));
    }

    public StringField formField(String name) {
        return new StringField(name, request.getFormAttribute(name));
    }

    public IntField intFormField(String name) {
        return new IntField(name, request.getFormAttribute(name));
    }

    private JsonObject jsonBody;

    public IntField intJsonField(String name) {
        checkJsonBody();
        return new IntField(name, jsonBody.getValue(name));
    }

    public StringField jsonField(String name) {
        checkJsonBody();
        return new StringField(name, jsonBody.getValue(name));
    }

    public static Validator create(RoutingContext context) {
        Validator v = new Validator(context);
        return v;
    }

    private void checkJsonBody() {
        if (jsonBody == null) {
            jsonBody = context.getBodyAsJson();
        }
    }


}
