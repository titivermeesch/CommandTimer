package me.playbosswar.com.conditionsengine.validations;

public class Validation {
    private String name;
    private ConditionType conditionType;
    private Condition condition;

    public Validation(String name, ConditionType conditionType, Condition condition) {
        this.name = name;
        this.conditionType = conditionType;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
