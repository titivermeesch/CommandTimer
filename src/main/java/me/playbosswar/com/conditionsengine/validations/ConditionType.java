package me.playbosswar.com.conditionsengine.validations;

public enum ConditionType {
    AND("Every condition need to be true"),
    OR("Any condition needs to be true"),
    SIMPLE("A condition in it simplest form. Result needs to be true"),
    NOT("A condition in it simplest form. Result needs to be false");

    String description;

    ConditionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
