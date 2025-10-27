package me.playbosswar.com.conditionsengine;

import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.NeededValue;
import me.playbosswar.com.api.events.EventCondition;
import me.playbosswar.com.api.events.EventConfiguration;
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.api.events.EventSimpleCondition;
import me.playbosswar.com.conditionsengine.conditions.ConditionHelpers;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskValidationHelpers;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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

        HashMap<Task, List<EventConfiguration>> events = new HashMap<>();
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

                events.putIfAbsent(t, new ArrayList<>());
                events.get(t).add(e);
            }
        }

        events.forEach((task, configurations) -> {
            boolean hasMatchingConfiguration = configurations
                    .stream()
                    .anyMatch(c -> processConfiguration(c.getCondition(), values));

            if(!hasMatchingConfiguration) {
                return;
            }

            OfflinePlayer p = null;
            UUID uuid = findPotentialPlayer(values);
            if(uuid != null) {
                p = Bukkit.getOfflinePlayer(uuid);
            }

            boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), p);

            if(valid) {
                task.getCommands().forEach(command -> tasksManager.processCommandExecution(task, command));
            } else {
                Messages.sendDebugConsole("EVENT ENGINE: Processed event but conditions did not match");
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
                expected = (Integer) simpleCondition.getValue();
            }
            return ConditionHelpers.calculateConditionCompare(compare, value, expected);
        }

        if(receivedValue.getType() == Double.class) {
            double value = (Double) receivedValue.getDefaultValue();
            double expected = (Double) simpleCondition.getValue();
            return ConditionHelpers.calculateConditionCompare(compare, value, expected);
        }

        if(receivedValue.getType() == String.class) {
            String value = (String) receivedValue.getDefaultValue();
            String expected = (String) simpleCondition.getValue();
            return value.equals(expected);
        }

        if(receivedValue.getType() == World.class) {
            World value = (World) receivedValue.getDefaultValue();
            World expected = Bukkit.getWorld(simpleCondition.getValue().toString());

            return value.getName().equals(expected.getName());
        }

        if(receivedValue.getType() == Boolean.class) {
            Boolean value = (Boolean) receivedValue.getDefaultValue();
            Boolean expected = (Boolean) simpleCondition.getValue();

            return value.equals(expected);
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


