package me.playbosswar.com.conditionsengine.conditions;

import me.playbosswar.com.conditionsengine.ConditionCompare;

public class ConditionHelpers {
    /**
     *
     * @param conditionCompare
     * @param value - Actual dynamic value
     * @param configValue - Value from config files
     * @return
     */
    public static boolean calculateConditionCompare(ConditionCompare conditionCompare, double value, double configValue) {
        switch (conditionCompare) {
            case EQUAL -> {
                return value == configValue;
            }
            case LESS_THAN -> {
                return value < configValue;
            }
            case GREATER_THAN -> {
                return value > configValue;
            }
            case LESS_OR_EQUAL_THEN -> {
                return value <= configValue;
            }
            case GREATER_OR_EQUAL_THAN -> {
                return value >= configValue;
            }
            default -> {
                return false;
            }
        }
    }
}
