package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.conditionsengine.ConditionParams;
import me.playbosswar.com.tasks.Task;

public class SimpleCondition {
    private String conditionGroup;
    private String rule;
    private ConditionParams conditionParams;
    private transient Task task;

    public SimpleCondition(Task task) {
        this.task = task;
        this.conditionParams = new ConditionParams(task);
    }

    public SimpleCondition(String conditionGroup, String rule, ConditionParams conditionParams, Task task) {
        this.conditionGroup = conditionGroup;
        this.rule = rule;
        this.conditionParams = conditionParams;
        this.task = task;
    }

    private void storeInstance() { task.storeInstance(); }

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

    public ConditionParams getConditionParams() {
        return conditionParams;
    }

    public void setConditionParams(ConditionParams conditionParams) {
        this.conditionParams = conditionParams;
        storeInstance();
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
