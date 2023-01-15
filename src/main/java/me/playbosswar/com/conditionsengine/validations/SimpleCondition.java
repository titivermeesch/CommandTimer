package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.tasks.Task;

import java.util.ArrayList;

public class SimpleCondition {
    private String conditionGroup;
    private String rule;
    private ArrayList<ConditionParamField<?>> conditionParamFields;
    private transient Task task;

    public SimpleCondition(Task task) {
        this.task = task;
        conditionParamFields = new ArrayList<>();
    }

    public SimpleCondition(String conditionGroup, String rule, ArrayList<ConditionParamField<?>> conditionParamFields
            , Task task) {
        this.conditionGroup = conditionGroup;
        this.rule = rule;
        this.task = task;
        this.conditionParamFields = conditionParamFields;
    }

    private void storeInstance() {
        task.storeInstance();
    }

    public String getConditionGroup() {
        return conditionGroup;
    }

    public void setConditionGroup(String conditionGroup) {
        this.conditionGroup = conditionGroup;
        storeInstance();
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
        storeInstance();
    }

    public ArrayList<ConditionParamField<?>> getConditionParamFields() {
        return conditionParamFields;
    }

    public void setConditionParamFields(ArrayList<ConditionParamField<?>> conditionParamFields) {
        this.conditionParamFields = conditionParamFields;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
