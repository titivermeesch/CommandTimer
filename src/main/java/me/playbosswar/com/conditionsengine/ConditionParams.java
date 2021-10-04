package me.playbosswar.com.conditionsengine;

import org.jetbrains.annotations.Nullable;

public class ConditionParams {
    private ConditionCompare conditionCompare;
    private double numericValue;
    private String stringValue;

    public ConditionParams(ConditionCompare conditionCompare, double numericValue, String stringValue) {
        this.conditionCompare = conditionCompare;
        this.numericValue = numericValue;
        this.stringValue = stringValue;
    }

    @Nullable
    public ConditionCompare getConditionCompare() {
        return conditionCompare;
    }

    public void setConditionCompare(ConditionCompare conditionCompare) {
        this.conditionCompare = conditionCompare;
    }

    public double getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(double numericValue) {
        this.numericValue = numericValue;
    }

    public String getStringValue() {
        if(stringValue == null) {
            return "";
        }

        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
