package org.example.validation;

import java.text.MessageFormat;

public class FloatField extends Field<Float, FloatField> {

    protected FloatField(String name, Object value) {
        super(name, value, Float.class);
    }

    public FloatField range(float min, float max) {
        try {
            if (value == null) {
                return this;
            }
            if (value < min || value > max) {
                this.errmsg = MessageFormat.format("{0} must gt {1} and lt {2}", name, min, max);
            }
        } catch (NumberFormatException e) {
            this.errmsg = MessageFormat.format("{0} must gt {1} and lt {2}", name, min, max);
        }
        return this;
    }

}
