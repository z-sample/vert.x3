package org.example.validation;

import java.text.MessageFormat;

public class DoubleField extends Field<Double, DoubleField> {

    protected DoubleField(String name, Object value) {
        super(name, value, Double.class);
    }

    public DoubleField range(double min, double max) {
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
