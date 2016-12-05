package org.example.validation;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

class Field<T, R> {
    String name;
    T value;
    boolean require;
    String errmsg;
    boolean error;
    T defValue;

    protected Field(String name, Object value, Class<T> clazz) {
        this.name = name;
        if (clazz.isInstance(value)) {
            this.value = (T) value;
        } else {
            error = true;
            errmsg = MessageFormat.format("{0} value is error type", name);
        }
    }

    public R require() {
        require = true;
        if (!error) {
            if (value == null) {
                errmsg = MessageFormat.format("{0} is require", name);
            }
        }
        return (R) this;
    }

    public R in(T... valuse) {
        if (!error) {
            if (value == null && !require) {
                return (R) this;
            }
            for (T v : valuse) {
                if (Objects.equals(v, value)) {
                    return (R) this;
                }
            }
            errmsg = MessageFormat.format("{0} value must in {0}", Arrays.toString(valuse));
        }
        return (R) this;
    }

    public R notIn(String... valuse) {
        if (!error) {
            if (value == null && !require) {
                return (R) this;
            }
            for (String v : valuse) {
                if (Objects.equals(v, value)) {
                    errmsg = MessageFormat.format("{0} value must in {0}", Arrays.toString(valuse));
                    return (R) this;
                }
            }
        }
        return (R) this;
    }


    public R defValue(T value) {
        this.defValue = value;
        return (R) this;
    }

    public R errmsg(String msg) {
        this.errmsg = msg;
        return (R) this;
    }

    public T result() {
        return this.value == null ? this.defValue : this.value;
    }

}
