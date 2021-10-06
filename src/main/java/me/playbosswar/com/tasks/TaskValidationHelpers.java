package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.conditionsengine.ConditionParams;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import org.bukkit.entity.Player;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

public class TaskValidationHelpers {
    public static boolean processCondition(Condition topCondition, Player p) {
        if (topCondition.getSimpleCondition() != null) {
            return checkSimpleCondition(topCondition.getSimpleCondition(), topCondition.getConditionType(), p);
        }

        if (topCondition.getConditionType().equals(ConditionType.AND)) {
            return topCondition.getConditions().stream().allMatch(condition -> {
                if (condition.getSimpleCondition() != null) {
                    return checkSimpleCondition(condition.getSimpleCondition(), condition.getConditionType(), p);
                }

                return processCondition(condition, p);
            });
        }

        if (topCondition.getConditionType().equals(ConditionType.OR)) {
            return topCondition.getConditions().stream().anyMatch(condition -> {
                if (condition.getSimpleCondition() != null) {
                    return checkSimpleCondition(condition.getSimpleCondition(), condition.getConditionType(), p);
                }

                return processCondition(condition, p);
            });
        }

        return false;
    }

    private static boolean checkSimpleCondition(SimpleCondition simpleCondition, ConditionType conditionType, Player p) {
        final ConditionEngineManager conditionEngineManager = CommandTimerPlugin.getInstance().getConditionEngineManager();
        Rule rule = conditionEngineManager.getRule(simpleCondition.getConditionGroup(), simpleCondition.getRule());
        ConditionParams conditionParams = simpleCondition.getConditionParams();

        Facts facts = new Facts();
        facts.put("player", p);
        if (conditionParams != null) {
            facts.put("numericValue", conditionParams.getNumericValue());
            facts.put("conditionCompare", conditionParams.getConditionCompare());
        }

        return conditionType.equals(ConditionType.SIMPLE) == rule.evaluate(facts);
    }
}
