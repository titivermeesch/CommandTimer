package me.playbosswar.com.gui.conditions.inputs;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.gui.conditions.ConditionMenu;
import me.playbosswar.com.utils.Callback;
import net.wesjd.anvilgui.AnvilGUI;

public class DoubleInputMenu {
    public AnvilGUI.Builder INVENTORY;

    public DoubleInputMenu(ConditionParamField<Double> conditionParamField, Condition condition, Callback callback) {
        INVENTORY = new AnvilGUI.Builder()
                .onComplete((player, text) -> {
                    conditionParamField.setValue(Double.parseDouble(text));
                    condition.getTask().storeInstance();
                    new ConditionMenu(condition, callback).INVENTORY.open(player);
                    return AnvilGUI.Response.close();
                })
                .text(conditionParamField.getValue().toString())
                .title("Write a value")
                .plugin(CommandTimerPlugin.getPlugin());
    }
}
