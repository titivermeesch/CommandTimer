package me.playbosswar.com.api.events;

import me.playbosswar.com.conditionsengine.validations.BaseCondition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.tasks.Task;

import java.util.List;

public class EventCondition extends BaseCondition<EventCondition, EventSimpleCondition<?>> {
    transient Task task;

    public EventCondition(Task task,
                          ConditionType conditionType,
                          EventSimpleCondition<?> simpleCondition,
                          List<EventCondition> conditions) {
        super(task, conditionType, simpleCondition, conditions);
        this.task = task;
    }
}
