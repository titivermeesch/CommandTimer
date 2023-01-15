package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.tasks.Task;

import java.util.List;

public class BaseCondition<T, S> {
    private ConditionType conditionType;
    private List<T> conditions;
    private S simpleCondition;
    transient Task task;

    public BaseCondition(Task task, ConditionType conditionType, S simpleCondition, List<T> conditions) {
        this.task = task;
        this.conditionType = conditionType;
        this.simpleCondition = simpleCondition;
        this.conditions = conditions;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
        task.storeInstance();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<T> getConditions() {
        return conditions;
    }

    public void setConditions(List<T> conditions) {
        this.conditions = conditions;
        task.storeInstance();
    }

    public void addCondition(T condition) {
        this.conditions.add(condition);
        task.storeInstance();
    }

    public void removeCondition(T condition) {
        this.conditions.remove(condition);
        task.storeInstance();
    }

    public S getSimpleCondition() {
        return simpleCondition;
    }

    public void setSimpleCondition(S simpleCondition) {
        this.simpleCondition = simpleCondition;
        task.storeInstance();
    }
}
