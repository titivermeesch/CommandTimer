package me.playbosswar.com.api.events;

import me.playbosswar.com.conditionsengine.validations.BaseCondition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;

import java.util.List;

public class EventCondition extends BaseCondition<EventCondition, EventSimpleCondition<?>> {
    public EventCondition(ConditionType conditionType,
                          EventSimpleCondition<?> simpleCondition,
                          List<EventCondition> conditions) {
        super(conditionType, simpleCondition, conditions);
    }
}
