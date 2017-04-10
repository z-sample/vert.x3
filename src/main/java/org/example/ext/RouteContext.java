package org.example.ext;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zero
 *         Created on 2017/4/7.
 */
public class RouteContext {//这里直接使用类,省略了接口设计

    private RoutingContext delegate;

    public RouteContext(RoutingContext delegate) {
        this.delegate = delegate;
    }

    public void render(String name) {
        delegate.response().end("render view : " + name);
    }

    public <T> T getBodyAsObject(Class<T> clazz) {
        return Json.decodeValue(delegate.getBodyAsString(),clazz);
    }


    //其他方法...

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zero");
        User user= mapper.convertValue(map, User.class);
        System.out.println(user);
    }

    private static class User{
        public String name;
        public String password;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("User{");
            sb.append("name='").append(name).append('\'');
            sb.append(", password='").append(password).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
