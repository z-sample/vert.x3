package org.example.validation;

import java.text.MessageFormat;

public class IntField extends Field<Integer, IntField> {

    protected IntField(String name, Object value) {
        super(name, value, Integer.class);
    }

    public IntField range(int min, int max) {
        try {
            if (value == null) {
                return this;
            }
            if (value < min || value > max) {
                this.errmsg = MessageFormat.format("{0} must range from {1} to {2}", name, min, max);
            }
        } catch (NumberFormatException e) {
            this.errmsg = MessageFormat.format("{0} must range from {1} to {2}", name, min, max);
        }
        return this;
    }
}
