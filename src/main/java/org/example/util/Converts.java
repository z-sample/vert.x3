package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;

/**
 * @author Zero
 *         Created on 2017/4/7.
 */
public class Converts {


    public static <T> T convert(JsonObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        T t=(T)new Object();
        mapper.convertValue(jsonObject, t.getClass());
        return null;
    }

}
