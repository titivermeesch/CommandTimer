package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.language.LanguageManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Regression tests for #327
class ConditionTreeTest {

    @BeforeAll
    static void setUpPlugin() throws Exception {
        // ConditionType's enum constructor resolves its description through the language manager
        setStaticField(CommandTimerPlugin.class, "languageManager", Mockito.mock(LanguageManager.class));
    }

    @AfterAll
    static void tearDownPlugin() throws Exception {
        setStaticField(CommandTimerPlugin.class, "languageManager", null);
    }

    @Test
    void selectingAnotherGroupDropsTheRuleOfThePreviousGroup() {
        SimpleCondition simpleCondition = new SimpleCondition("Player", "has-permission", paramFields());

        simpleCondition.selectConditionGroup("Server");

        assertEquals("Server", simpleCondition.getConditionGroup());
        assertNull(simpleCondition.getRule(), "the rule of the previous group cannot be resolved in the new one");
        assertTrue(simpleCondition.getConditionParamFields().isEmpty());
    }

    @Test
    void deselectingTheGroupClearsTheWholeSelection() {
        SimpleCondition simpleCondition = new SimpleCondition("Player", "has-permission", paramFields());

        simpleCondition.selectConditionGroup(null);

        assertNull(simpleCondition.getConditionGroup());
        assertNull(simpleCondition.getRule());
        assertTrue(simpleCondition.getConditionParamFields().isEmpty());
    }

    @Test
    void simpleConditionWithLeftoverPartsIsNotConfigured() {
        Task task = taskWithCondition(new Condition(ConditionType.SIMPLE, parts(), new SimpleCondition()));

        assertFalse(task.hasCondition(), "a SIMPLE condition never evaluates its parts");
    }

    @Test
    void configuredSimpleConditionIsConfigured() {
        Task task = taskWithCondition(new Condition(ConditionType.SIMPLE, new ArrayList<>(),
                new SimpleCondition("Player", "has-permission", paramFields())));

        assertTrue(task.hasCondition());
    }

    @Test
    void compositeConditionWithPartsIsConfigured() {
        Task task = taskWithCondition(new Condition(ConditionType.AND, parts(), new SimpleCondition()));

        assertTrue(task.hasCondition());
    }

    @Test
    void compositeConditionWithoutPartsIsNotConfigured() {
        Task task = taskWithCondition(new Condition(ConditionType.OR, new ArrayList<>(),
                new SimpleCondition("Player", "has-permission", paramFields())));

        assertFalse(task.hasCondition());
    }

    private static Task taskWithCondition(Condition condition) {
        Task task = new Task();
        task.setCondition(condition);
        return task;
    }

    private static List<Condition> parts() {
        List<Condition> parts = new ArrayList<>();
        parts.add(new Condition(ConditionType.SIMPLE, new ArrayList<>(),
                new SimpleCondition("Player", "is-op", new ArrayList<>())));
        return parts;
    }

    private static ArrayList<ConditionParamField<?>> paramFields() {
        ArrayList<ConditionParamField<?>> paramFields = new ArrayList<>();
        paramFields.add(new ConditionParamField<>("permission", "essentials.fly"));
        return paramFields;
    }

    private static void setStaticField(Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}
