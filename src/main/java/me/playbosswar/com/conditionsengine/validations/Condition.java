package me.playbosswar.com.conditionsengine.validations;

import java.util.List;

public class Condition {
    // AND, OR, ...
    private ConditionType conditionType;
    private List<ConditionPart> conditionParts;

    public Condition(ConditionType conditionType, List<ConditionPart> conditionParts) {
        this.conditionType = conditionType;
        this.conditionParts = conditionParts;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public List<ConditionPart> getConditionParts() {
        return conditionParts;
    }

    public void setConditionParts(List<ConditionPart> conditionParts) {
        this.conditionParts = conditionParts;
    }
}
