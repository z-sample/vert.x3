package org.example.validation;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateField extends Field<Date, DateField> {

    protected DateField(String name, Object value) {
        super(name, value, Date.class);
    }

    public DateField range(Date min, Date max) {
        try {
            if (value == null) {
                return this;
            }
            if (value.getTime() < min.getTime() || value.getTime() > max.getTime()) {
                this.errmsg = MessageFormat.format("{0} must range from {1} to {2}", name, min, max);
            }
        } catch (NumberFormatException e) {
            this.errmsg = MessageFormat.format("{0} must range from {1} to {2}", name, min, max);
        }
        return this;
    }

}
