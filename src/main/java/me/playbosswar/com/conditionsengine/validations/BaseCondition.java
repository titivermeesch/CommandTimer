package me.playbosswar.com.conditionsengine.validations;

import java.util.List;

public class BaseCondition<T, S> {
    private ConditionType conditionType;
    private List<T> conditions;
    private S simpleCondition;

    public BaseCondition(ConditionType conditionType, S simpleCondition, List<T> conditions) {
        this.conditionType = conditionType;
        this.simpleCondition = simpleCondition;
        this.conditions = conditions;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public List<T> getConditions() {
        return conditions;
    }

    public void setConditions(List<T> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(T condition) {
        this.conditions.add(condition);
    }

    public void removeCondition(T condition) {
        this.conditions.remove(condition);
    }

    public S getSimpleCondition() {
        return simpleCondition;
    }

    public void setSimpleCondition(S simpleCondition) {
        this.simpleCondition = simpleCondition;
    }
}
