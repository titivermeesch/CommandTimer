package me.playbosswar.com.gui.conditions;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionRule;
import me.playbosswar.com.api.NeededValue;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.gui.MenuUtils;
import me.playbosswar.com.gui.conditions.inputs.ConditionCompareItem;
import me.playbosswar.com.gui.TextInputManager;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConditionMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;
    private final Condition condition;
    private final Callback<?> onClose;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public ConditionMenu(Task task, Condition condition, Callback<?> onClose) {
        this.task = task;
        this.condition = condition;
        this.onClose = onClose;
        INVENTORY = SmartInventory.builder()
                .id("condition")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.CONDITION_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        final Callback<String> internalCallback = data -> INVENTORY.open(player);

        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ClickableItem conditionTypeItem = MenuUtils.getConditionTypeItem(condition, type -> {
            condition.setConditionType(type);
            task.storeInstance();
            this.INVENTORY.open(player);
        });
        contents.set(1, 1, conditionTypeItem);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));

        if(condition.getConditionType().equals(ConditionType.AND) || condition.getConditionType().equals(ConditionType.OR)) {
            String[] conditionPartsLore =
                    languageManager.getList(LanguageKey.CONDITION_TYPE_LORE).toArray(new String[0]);
            ItemStack conditionsItem = Items.generateItem(LanguageKey.CONDITION_PARTS_TITLE,
                    XMaterial.CRAFTING_TABLE, conditionPartsLore);
            ClickableItem clickableConditions = ClickableItem.of(conditionsItem, e -> new ConditionsMenu(task, condition,
                    internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableConditions);
        } else {
            SimpleCondition simpleCondition = condition.getSimpleCondition();

            String conditionGroup = simpleCondition.getConditionGroup();
            String rule = simpleCondition.getRule();
            List<String> simpleConditionLore = languageManager.getList(LanguageKey.SIMPLE_CONDITION_LORE);
            simpleConditionLore.addAll(Arrays.stream(new String[]{
                            "",
                            languageManager.get(LanguageKey.GUI_CURRENT, ""),
                            languageManager.get(LanguageKey.CONDITION_GROUP) + (conditionGroup == null ?
                                    languageManager.get(LanguageKey.NOT_SET) :
                                    "§e" + conditionGroup),
                            languageManager.get(LanguageKey.CONDITION_RULE) + (rule == null ?
                                    languageManager.get(LanguageKey.NOT_SET) : "§e" + rule)})
                    .collect(Collectors.toList()));
            ItemStack simpleConditionItem = Items.generateItem(languageManager.get(LanguageKey.CONFIGURE_CONDITION),
                    XMaterial.CRAFTING_TABLE,
                    simpleConditionLore.toArray(new String[0]));
            ClickableItem clickableSimpleCondition = ClickableItem.of(simpleConditionItem,
                    e -> new SimpleConditionMenu(task, simpleCondition, internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableSimpleCondition);

            ArrayList<ConditionParamField<?>> conditionParamFields = simpleCondition.getConditionParamFields();
            if(conditionParamFields != null && conditionParamFields.size() > 0) {
                ConditionRule conditionRule =
                        CommandTimerPlugin.getInstance().getConditionEngineManager().getRule(simpleCondition.getConditionGroup(), rule);

                if(conditionRule == null) {
                    return;
                }

                // TODO: This needs to be moved to a different view
                ArrayList<NeededValue<?>> neededValues = conditionRule.getNeededValues();
                int i = 3;
                for(ConditionParamField<?> conditionParamField : conditionParamFields) {
                    Optional<NeededValue<?>> optionalNeededValue =
                            neededValues.stream().filter(v -> v.getName().equals(conditionParamField.getName())).findFirst();

                    if(!optionalNeededValue.isPresent()) {
                        continue;
                    }

                    NeededValue<?> neededValue = optionalNeededValue.get();

                    if(neededValue.getType() == ConditionCompare.class) {
                        ConditionParamField<ConditionCompare> conditionCompareParamField =
                                (ConditionParamField<ConditionCompare>) conditionParamField;
                        ClickableItem clickableConditionCompare = ConditionCompareItem.get(conditionCompareParamField
                                , conditionCompare -> {
                                    conditionCompareParamField.setValue(conditionCompare);
                                    task.storeInstance();
                                    this.INVENTORY.open(player);
                                });
                        contents.set(1, i, clickableConditionCompare);
                        i++;
                        continue;
                    }

                    // From this point, we should treat native types only
                    String[] lore = new String[]{
                            "",
                            languageManager.get(LanguageKey.GUI_CURRENT, conditionParamField.getValue().toString()),
                            "",
                            languageManager.get(LanguageKey.LEFT_CLICK_EDIT)
                    };
                    ItemStack item =
                            Items.generateItem(languageManager.get(LanguageKey.SET_VALUE) + neededValue.getLabel(),
                                    XMaterial.PAPER,
                                    lore);
                    ClickableItem clickableItem = ClickableItem.of(item, e -> {
                        if(neededValue.getType() == Double.class) {
                            player.closeInventory();
                            TextInputManager.getInstance().startTextInput(player, LanguageKey.TEXT_INPUT_DEFAULT, data -> {
                                double text = Double.parseDouble(data);
                                ((ConditionParamField<Double>) conditionParamField).setValue(text);
                                task.storeInstance();
                                new ConditionMenu(task, condition, onClose).INVENTORY.open(player);
                            });
                            return;
                        }

                        if(neededValue.getType() == Integer.class) {
                            player.closeInventory();
                            TextInputManager.getInstance().startTextInput(player, LanguageKey.TEXT_INPUT_DEFAULT, data -> {
                                int text = Integer.parseInt(data);
                                ((ConditionParamField<Integer>) conditionParamField).setValue(text);
                                task.storeInstance();
                                new ConditionMenu(task, condition, onClose).INVENTORY.open(player);
                            });
                            return;
                        }

                        if(neededValue.getType() == String.class) {
                            player.closeInventory();
                            TextInputManager.getInstance().startTextInput(player, LanguageKey.TEXT_INPUT_DEFAULT, text -> {
                                ((ConditionParamField<String>) conditionParamField).setValue(text);
                                task.storeInstance();
                                new ConditionMenu(task, condition, onClose).INVENTORY.open(player);
                            });
                        }
                    });

                    contents.set(1, i, clickableItem);
                    i++;
                }
            }


        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
