package me.playbosswar.com.api.events;

import me.playbosswar.com.conditionsengine.validations.ConditionType;

import java.util.List;

public class EventCondition {
    private final ConditionType conditionType;
    private final List<EventSimpleCondition<?>> simpleCondition;
    private final List<EventCondition> conditions;

    public EventCondition(ConditionType conditionType, List<EventSimpleCondition<?>> simpleCondition,
                          List<EventCondition> conditions) {
        this.conditionType = conditionType;
        this.simpleCondition = simpleCondition;
        this.conditions = conditions;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public List<EventSimpleCondition<?>> getSimpleCondition() {
        return simpleCondition;
    }

    public List<EventCondition> getConditions() {
        return conditions;
    }
}
