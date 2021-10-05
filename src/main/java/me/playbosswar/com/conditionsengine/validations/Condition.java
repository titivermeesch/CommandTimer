package me.playbosswar.com.conditionsengine.validations;

import java.util.List;

public class Condition {
    private ConditionType conditionType;
    private List<Condition> conditions;
    private SimpleCondition simpleCondition;

    public Condition(ConditionType conditionType, List<Condition> conditions) {
        this.conditionType = conditionType;
        this.conditions = conditions;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(Condition condition) { this.conditions.add(condition); }

    public void removeCondition(Condition condition) { this.conditions.remove(condition); }

    public SimpleCondition getSimpleCondition() {
        return simpleCondition;
    }

    public void setSimpleCondition(SimpleCondition simpleCondition) {
        this.simpleCondition = simpleCondition;
    }
}
