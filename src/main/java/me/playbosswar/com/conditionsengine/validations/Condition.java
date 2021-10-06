package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.tasks.Task;

import java.util.List;

public class Condition {
    private ConditionType conditionType;
    private List<Condition> conditions;
    private SimpleCondition simpleCondition;
    private transient Task task;

    public Condition(ConditionType conditionType, List<Condition> conditions, SimpleCondition simpleCondition, Task task) {
        this.conditionType = conditionType;
        this.conditions = conditions;
        this.simpleCondition = simpleCondition;
        this.task = task;
    }

    private void storeInstance() { task.storeInstance(); }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
        storeInstance();
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
        storeInstance();
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
        storeInstance();
    }

    public void removeCondition(Condition condition) {
        this.conditions.remove(condition);
        storeInstance();
    }

    public SimpleCondition getSimpleCondition() {
        return simpleCondition;
    }

    public void setSimpleCondition(SimpleCondition simpleCondition) {
        this.simpleCondition = simpleCondition;
        storeInstance();
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
