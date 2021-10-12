package me.playbosswar.com.conditionsengine;

public class ConditionParamField<T> {
    private final String name;
    private T value;

    public ConditionParamField(String name) {
        this.name = name;
    }

    public ConditionParamField(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
