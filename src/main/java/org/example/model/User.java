package org.example.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * Date: 2016/12/11
 * Time: 16:36
 *
 * @author: Zero
 */
@DataObject
@VertxGen
public class User extends JsonObject {


    public static final String NAME = "name";//Java attribute name->Table column name

    public User() {

    }

    public User(JsonObject jsonObject) {
        this.getMap().putAll(jsonObject.getMap());
//        jsonObject.stream().parallel().forEach(e->this.put(e.getKey(),e.getValue()));
    }

    public User name(String name) {
        this.put(NAME, name);
        return this;
    }

    public Optional<String> name() {
        String name = getString(NAME);
        return Optional.of(name);
    }


}
