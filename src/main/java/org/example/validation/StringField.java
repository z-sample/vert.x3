package org.example.validation;

public class StringField extends Field<String, StringField> {

    protected StringField(String name, Object value) {
        super(name, value, String.class);
    }

    public StringField email(String name) {
        return this;
    }

    public StringField regex(String name, String regex) {
        return this;
    }

}