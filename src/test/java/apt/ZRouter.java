package apt;

import io.vertx.ext.web.Router;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

/**
 * @author Zero
 *         Created on 2017/1/4.
 */
public interface ZRouter  {

    Router validation();


    Router cache(String key);



}
