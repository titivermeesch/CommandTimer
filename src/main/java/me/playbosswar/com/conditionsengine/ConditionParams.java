package me.playbosswar.com.conditionsengine;

import me.playbosswar.com.tasks.Task;
import org.jetbrains.annotations.Nullable;

public class ConditionParams {
    private ConditionCompare conditionCompare;
    private double numericValue;
    private String stringValue;
    private transient Task task;

    public ConditionParams(Task task) {
        this.task = task;
    }

    public ConditionParams(ConditionCompare conditionCompare, double numericValue, String stringValue, Task task) {
        this.conditionCompare = conditionCompare;
        this.numericValue = numericValue;
        this.stringValue = stringValue;
        this.task = task;
    }

    private void storeInstance() { task.storeInstance(); }

    @Nullable
    public ConditionCompare getConditionCompare() {
        return conditionCompare;
    }

    public void setConditionCompare(ConditionCompare conditionCompare) {
        this.conditionCompare = conditionCompare;
        storeInstance();
    }

    public double getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(double numericValue) {
        this.numericValue = numericValue;
        storeInstance();
    }

    public String getStringValue() {
        if(stringValue == null) {
            return "";
        }

        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
        storeInstance();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
