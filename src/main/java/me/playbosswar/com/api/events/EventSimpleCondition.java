package me.playbosswar.com.api.events;

import me.playbosswar.com.conditionsengine.ConditionCompare;

public class EventSimpleCondition<T> {
    private String fieldName;
    private T value;
    private ConditionCompare compare;

    public EventSimpleCondition(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public EventSimpleCondition(String fieldName, T value, ConditionCompare compare) {
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
}
