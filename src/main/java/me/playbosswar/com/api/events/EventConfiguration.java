package me.playbosswar.com.api.events;

import me.playbosswar.com.tasks.Task;

public class EventConfiguration {
    private boolean active;
    private String conditionGroup;
    private String event;
    private EventCondition condition;
    transient Task task;

    public EventConfiguration(Task task, boolean active, String conditionGroup, String event,
                              EventCondition condition) {
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
