package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.conditionsengine.ConditionParamField;

import java.util.ArrayList;

public class SimpleCondition {
    private String conditionGroup;
    private String rule;
    private ArrayList<ConditionParamField<?>> conditionParamFields;

    public SimpleCondition() {
        conditionParamFields = new ArrayList<>();
    }

    public SimpleCondition(String conditionGroup, String rule, ArrayList<ConditionParamField<?>> conditionParamFields) {
        this.conditionGroup = conditionGroup;
        this.rule = rule;
        this.conditionParamFields = conditionParamFields;
    }


    public String getConditionGroup() {
        return conditionGroup;
    }

    public void setConditionGroup(String conditionGroup) {
        this.conditionGroup = conditionGroup;
    }

    // Rules belong to a group, so a stale rule could never be resolved in the new one
    public void selectConditionGroup(String conditionGroup) {
        this.conditionGroup = conditionGroup;
        this.rule = null;
        this.conditionParamFields = new ArrayList<>();
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public ArrayList<ConditionParamField<?>> getConditionParamFields() {
        return conditionParamFields;
    }

    public void setConditionParamFields(ArrayList<ConditionParamField<?>> conditionParamFields) {
        this.conditionParamFields = conditionParamFields;
    }
}
