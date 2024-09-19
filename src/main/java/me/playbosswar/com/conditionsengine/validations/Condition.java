package me.playbosswar.com.conditionsengine.validations;

import java.util.List;

public class Condition extends BaseCondition<Condition, SimpleCondition> {
    public Condition(ConditionType conditionType,
                     List<Condition> conditions,
                     SimpleCondition simpleCondition) {
        super(conditionType, simpleCondition, conditions);
    }
}
