package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Condition extends BaseCondition<Condition, SimpleCondition> {
    public Condition(ConditionType conditionType,
                     List<Condition> conditions,
                     SimpleCondition simpleCondition,
                     Task task) {
        super(task, conditionType, simpleCondition, conditions);
    }
}
