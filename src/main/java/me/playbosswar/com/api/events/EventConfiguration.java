package me.playbosswar.com.api.events;

public class EventConfiguration {
    private boolean active;
    private String conditionGroup;
    private String event;
    private EventCondition condition;

    public EventConfiguration(boolean active, String conditionGroup, String event,
                              EventCondition condition) {
        this.active = active;
        this.conditionGroup = conditionGroup;
        this.event = event;
        this.condition = condition;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getConditionGroup() {
        return conditionGroup;
    }

    public String getEvent() {
        return event;
    }

    public EventCondition getCondition() {
        return condition;
    }
}
