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
import org.bukkit.entity.Player;
import org.jeasy.rules.api.Facts;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

        if(conditionType.equals(ConditionType.AND)) {
            return topCondition.getConditions().stream().allMatch(condition -> processCondition(condition, p));
        }

        if(conditionType.equals(ConditionType.OR)) {
            return topCondition.getConditions().stream().anyMatch(condition -> processCondition(condition, p));
        }

        Messages.sendDebugConsole("Could not process condition because the type is not valid");
        return false;
    }

    private static boolean checkSimpleCondition(SimpleCondition simpleCondition, @Nullable OfflinePlayer p) {
        final ConditionEngineManager conditionEngineManager =
                CommandTimerPlugin.getInstance().getConditionEngineManager();
        ConditionRule rule = conditionEngineManager.getRule(simpleCondition.getConditionGroup(),
                simpleCondition.getRule());
        ArrayList<ConditionParamField<?>> conditionParams = simpleCondition.getConditionParamFields();

        if(rule == null) {
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
