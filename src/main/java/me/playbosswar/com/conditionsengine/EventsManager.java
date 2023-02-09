package me.playbosswar.com.conditionsengine;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.ConditionRule;
import me.playbosswar.com.api.events.EventCondition;
import me.playbosswar.com.api.events.EventConfiguration;
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.api.NeededValue;
import me.playbosswar.com.api.events.EventSimpleCondition;
import me.playbosswar.com.conditionsengine.conditions.ConditionHelpers;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskValidationHelpers;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jeasy.rules.api.Facts;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventsManager {
    private final TasksManager tasksManager;

    public EventsManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    // Mainly used by extensions with events
    public void handleTriggeredEvent(ConditionExtension extension, EventExtension ev,
                                     ArrayList<NeededValue<?>> values) {
        // List of related tasks
        List<Task> tasks = tasksManager
                .getLoadedTasks()
                .stream().
                filter(t -> t
                        .getEvents()
                        .stream()
                        .anyMatch(e ->
                                e.getConditionGroup().equals(extension.getConditionGroupName()) &&
                                        e.getEvent().equals(ev.getEventName()))).collect(Collectors.toList());

        List<EventConfiguration> configurations = new ArrayList<>();
        for(Task t : tasks) {
            for(EventConfiguration e : t.getEvents()) {
                if(!e.isActive()) {
                    Messages.sendDebugConsole("EVENT ENGINE: received event but ignoring because configuration is not" +
                            " active");
                    continue;
                }

                if(!e.getConditionGroup().equals(extension.getConditionGroupName())) {
                    Messages.sendDebugConsole("EVENT ENGINE: received event but condition group does not match");
                    continue;
                }

                if(!e.getEvent().equals(ev.getEventName())) {
                    Messages.sendDebugConsole("EVENT ENGINE: received event but condition name does not match");
                    continue;
                }

                configurations.add(e);
            }
        }

        List<Task> tasksToExecute = configurations
                .stream()
                .filter(c -> processConfiguration(c.getCondition(), values))
                .map(EventConfiguration::getTask)
                .collect(Collectors.toList());

        tasksToExecute.forEach(task -> {
            Player p = null;
            UUID uuid = findPotentialPlayer(values);
            if(uuid != null) {
                p = Bukkit.getPlayer(uuid);
            }

            boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), p);

            if(valid) {
                task.getCommands().forEach(tasksManager::processCommandExecution);
            }
        });
    }

    private boolean checkSimpleCondition(EventSimpleCondition<?> simpleCondition, List<NeededValue<?>> values) {
        ConditionCompare compare = simpleCondition.getCompare();
        String fieldName = simpleCondition.getFieldName();
        Object expectedValue = simpleCondition.getValue();
        Optional<NeededValue<?>> possibleValue = values.stream().filter(v -> v.getName().equals(fieldName)).findAny();
        if(!possibleValue.isPresent()) {
            return false;
        }
        NeededValue<?> receivedValue = possibleValue.get();

        // Shortcut the logic, we just want to compare string
        if(compare == null) {
            return receivedValue.getDefaultValue().equals(expectedValue);
        }

        if(receivedValue.getType() == Integer.class) {
            int value = (Integer) receivedValue.getDefaultValue();
            // Needed because GSON does not make a difference between int and doubles
            int expected;
            if(simpleCondition.getValue() instanceof Double) {
                Double conditionValue = (Double) simpleCondition.getValue();
                expected = conditionValue.intValue();
            } else {
                expected = (int) simpleCondition.getValue();
            }
            return ConditionHelpers.calculateConditionCompare(compare, value, expected);
        }

        if(receivedValue.getType() == Double.class) {
            double value = (Double) receivedValue.getDefaultValue();
            double expected = (double) simpleCondition.getValue();
            return ConditionHelpers.calculateConditionCompare(compare, value, expected);
        }

        return false;
    }

    @Nullable
    private UUID findPotentialPlayer(List<NeededValue<?>> values) {
        Optional<NeededValue<?>> optionalPlayer =
                values.stream().filter(v -> v.getName().equals("@PLAYER_UUID")).findAny();

        return optionalPlayer.map(neededValue -> UUID.fromString((String) neededValue.getDefaultValue())).orElse(null);
    }

    private boolean processConfiguration(EventCondition condition, List<NeededValue<?>> values) {
        ConditionType conditionType = condition.getConditionType();

        if(conditionType.equals(ConditionType.SIMPLE)) {
            return checkSimpleCondition(condition.getSimpleCondition(), values);
        }

        if(conditionType.equals(ConditionType.NOT)) {
            return !checkSimpleCondition(condition.getSimpleCondition(), values);
        }

        if(conditionType.equals(ConditionType.AND)) {
            return condition.getConditions().stream().allMatch(nestedCondition -> processConfiguration(nestedCondition, values));
        }

        if(conditionType.equals(ConditionType.OR)) {
            return condition.getConditions().stream().anyMatch(nestedCondition -> processConfiguration(nestedCondition, values));
        }

        return false;
    }
}


