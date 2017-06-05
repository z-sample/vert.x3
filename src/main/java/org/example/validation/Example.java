package org.example.validation;

import io.vertx.ext.web.RoutingContext;

/**
 * @author Zero
 *         Created on 2017/1/3.
 */
public class Example {
    private void example(RoutingContext rc) {
        Validator v = Validator.create(rc);
        String name = v.field("name").require().in("zero", "frank").result();
        String company = v.field("company").defValue("google").result();
        int age = v.intField("age").require().in(18, 28, 38).result();
        if (v.hasErrors()) {
            rc.fail(400);
        }

    }
}
