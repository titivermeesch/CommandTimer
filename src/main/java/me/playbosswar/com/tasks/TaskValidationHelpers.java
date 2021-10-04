package me.playbosswar.com.tasks;

import me.playbosswar.com.Main;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.conditionsengine.ConditionParams;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionPart;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.Validation;
import org.bukkit.entity.Player;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import java.util.List;

public class TaskValidationHelpers {
    public static boolean processValidations(List<Validation> validations, Player p) {
        return validations.stream().allMatch(validation -> {
            Condition condition = validation.getCondition();
            List<ConditionPart> conditionParts = condition.getConditionParts();

            if (condition.getConditionType().equals(ConditionType.AND)) {
                return conditionParts.stream().allMatch(conditionPart -> checkConditionPart(conditionPart, p));
            }

            return conditionParts.stream().anyMatch(conditionPart -> checkConditionPart(conditionPart, p));
        });
    }

    private static boolean checkConditionPart(ConditionPart conditionPart, Player p) {
        final ConditionEngineManager conditionEngineManager = Main.getConditionEngineManager();
        Rule rule = conditionEngineManager.getRule(conditionPart.getRuleExecutor().getRuleName());
        ConditionParams conditionParams = conditionPart.getConditionParams();

        Facts facts = new Facts();
        facts.put("player", p);
        if (conditionParams != null) {
            facts.put("numericValue", conditionParams.getNumericValue());
            facts.put("conditionCompare", conditionParams.getConditionCompare());
        }

        if (conditionPart.getConditionType().equals(ConditionType.SIMPLE)) {
            return rule.evaluate(facts);
        }

        if (conditionPart.getConditionType().equals(ConditionType.NOT)) {
            return !rule.evaluate(facts);
        }

        if (conditionPart.getConditionType().equals(ConditionType.AND)) {
            return conditionPart.getConditionParts().stream().allMatch(conditionPart1 -> checkConditionPart(conditionPart1, p));
        }

        // OR CASE
        return conditionPart.getConditionParts().stream().anyMatch(conditionPart1 -> checkConditionPart(conditionPart1, p));

    }
}
