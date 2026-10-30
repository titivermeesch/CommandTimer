package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionRule;
import me.playbosswar.com.api.NeededValue;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.utils.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jeasy.rules.api.Facts;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskValidationHelpers {
    public static boolean processCondition(Condition topCondition, @Nullable OfflinePlayer p) {
        ConditionType conditionType = topCondition.getConditionType();
        if(conditionType.equals(ConditionType.SIMPLE)) {
            return checkSimpleCondition(topCondition.getSimpleCondition(), p);
        }

        if(conditionType.equals(ConditionType.NOT)) {
            return !checkSimpleCondition(topCondition.getSimpleCondition(), p);
        }

        List<Condition> conditions = topCondition.getConditions();
        boolean hasNoParts = conditions == null || conditions.isEmpty();

        if(conditionType.equals(ConditionType.AND)) {
            if(hasNoParts) {
                Messages.sendDebugConsole("AND condition has no parts configured, treating it as valid");
                return true;
            }

            return conditions.stream().allMatch(condition -> processCondition(condition, p));
        }

        if(conditionType.equals(ConditionType.OR)) {
            if(hasNoParts) {
                Messages.sendDebugConsole("OR condition has no parts configured, treating it as invalid");
                return false;
            }

            return conditions.stream().anyMatch(condition -> processCondition(condition, p));
        }

        Messages.sendDebugConsole("Could not process condition because the type is not valid");
        return false;
    }

    private static boolean checkSimpleCondition(SimpleCondition simpleCondition, @Nullable OfflinePlayer p) {
        if(simpleCondition == null) {
            Messages.sendDebugConsole("Condition has no simple condition configured, treating it as valid");
            return true;
        }

        final ConditionEngineManager conditionEngineManager =
                CommandTimerPlugin.getInstance().getConditionEngineManager();
        ConditionRule rule = conditionEngineManager.getRule(simpleCondition.getConditionGroup(),
                simpleCondition.getRule());
        ArrayList<ConditionParamField<?>> conditionParams = simpleCondition.getConditionParamFields();

        if(rule == null) {
            Messages.sendDebugConsole("Could not resolve rule '" + simpleCondition.getRule() + "' in condition group '"
                    + simpleCondition.getConditionGroup() + "', treating the condition as valid");
            return true;
        }

        ArrayList<NeededValue<?>> neededValues = rule.getNeededValues();

        Facts facts = new Facts();
        if(p != null) {
            facts.put("player", p);
        }

        if(conditionParams != null) {
            for(ConditionParamField<?> conditionParamField : conditionParams) {
                Optional<NeededValue<?>> optionalNeededValue =
                        neededValues.stream().filter(v -> v.getName().equals(conditionParamField.getName())).findFirst();

                if(!optionalNeededValue.isPresent()) {
                    continue;
                }

                NeededValue<?> neededValue = optionalNeededValue.get();
                if(neededValue.getType() == ConditionCompare.class) {
                    if(conditionParamField.getValue() instanceof ConditionCompare) {
                        facts.put(conditionParamField.getName(), conditionParamField.getValue());
                    } else {
                        facts.put(conditionParamField.getName(),
                                ConditionCompare.valueOf((String) conditionParamField.getValue()));
                    }
                    continue;
                }

                if(neededValue.getType() == Double.class) {
                    facts.put(conditionParamField.getName(), (Double) conditionParamField.getValue());
                }

                if(neededValue.getType() == String.class) {
                    facts.put(conditionParamField.getName(), (String) conditionParamField.getValue());
                }

                if(neededValue.getType() == World.class) {
                    facts.put(conditionParamField.getName(), (World) conditionParamField.getValue());
                }

                if(neededValue.getType() == Boolean.class) {
                    facts.put(conditionParamField.getName(), (Boolean) conditionParamField.getValue());
                }
            }
        }

        for (NeededValue<?> neededValue : neededValues) {
            if (facts.get(neededValue.getName()) == null) {
                facts.put(neededValue.getName(), neededValue.getDefaultValue());
            }
        }

        return rule.evaluate(facts);
    }
}
