package me.playbosswar.com.api.events;

import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.tasks.Task;

public class EventSimpleCondition<T> {
    private String fieldName;
    private T value;
    private ConditionCompare compare;
    transient Task task;

    public EventSimpleCondition(Task task, String fieldName, T value) {
        this.task = task;
        this.fieldName = fieldName;
        this.value = value;
    }

    public EventSimpleCondition(Task task, String fieldName, T value, ConditionCompare compare) {
        this.task = task;
        this.fieldName = fieldName;
        this.value = value;
        this.compare = compare;
    }

    public String getFieldName() {
        return fieldName;
    }

    public T getValue() {
        return value;
    }

    public ConditionCompare getCompare() {
        return compare;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setCompare(ConditionCompare compare) {
        this.compare = compare;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
