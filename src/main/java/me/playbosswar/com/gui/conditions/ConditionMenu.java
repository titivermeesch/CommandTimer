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
import me.playbosswar.com.gui.conditions.inputs.ConditionCompareItem;
import me.playbosswar.com.gui.tasks.general.TextInputConversationPrompt;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConditionMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Condition condition;
    private final Callback onClose;

    public ConditionMenu(Condition condition, Callback onClose) {
        this.condition = condition;
        this.onClose = onClose;
        INVENTORY = SmartInventory.builder()
                .id("condition")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title("§9§lCondition")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        final Callback internalCallback = (Callback<String>) data -> INVENTORY.open(player);

        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        // Item to toggle condition type
        List<ConditionType> availableConditionTypes = new ArrayList<>();
        availableConditionTypes.add(ConditionType.AND);
        availableConditionTypes.add(ConditionType.OR);
        availableConditionTypes.add(ConditionType.SIMPLE);
        availableConditionTypes.add(ConditionType.NOT);
        ClickableItem conditionTypeItem = ConditionTypeItem.get(
                availableConditionTypes,
                condition.getConditionType(),
                type -> {
                    condition.setConditionType(type);
                    this.INVENTORY.open(player);
                });
        contents.set(1, 1, conditionTypeItem);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));

        if (condition.getConditionType().equals(ConditionType.AND) || condition.getConditionType().equals(ConditionType.OR)) {
            String[] conditionPartsLore = new String[]{"",
                    "§7Condition parts allow you to create complex",
                    "§7conditions to fit your needs. They can",
                    "§7also be nested to give you endless",
                    "§7possibilities.",
                    "",
                    "§7Try to stick with simple conditions before",
                    "§7you go crazy on them"
            };
            ItemStack conditionsItem = Items.generateItem("§bCondition parts", XMaterial.CRAFTING_TABLE, conditionPartsLore);
            ClickableItem clickableConditions = ClickableItem.of(conditionsItem,
                    e -> new ConditionsMenu(condition, internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableConditions);
        } else {
            SimpleCondition simpleCondition = condition.getSimpleCondition();

            String conditionGroup = simpleCondition.getConditionGroup();
            String rule = simpleCondition.getRule();
            String[] simpleConditionLore = new String[]{"",
                    "§7A simple condition is a basic comparison between",
                    "§72 values. For example §oplayer is OP -> true/false",
                    "",
                    "§bCurrent settings:",
                    "§7 - Condition group: " + (conditionGroup == null ? "§eNot Set" : "§e" + conditionGroup),
                    "§7 - Rule: " + (rule == null ? "§eNot Set" : "§e" + rule),
            };
            ItemStack simpleConditionItem = Items.generateItem("§bConfigure condition", XMaterial.CRAFTING_TABLE,
                    simpleConditionLore);
            ClickableItem clickableSimpleCondition = ClickableItem.of(simpleConditionItem,
                    e -> new SimpleConditionMenu(
                            simpleCondition,
                            internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableSimpleCondition);

            ArrayList<ConditionParamField<?>> conditionParamFields = simpleCondition.getConditionParamFields();
            if (conditionParamFields != null && conditionParamFields.size() > 0) {
                ConditionRule conditionRule =
                        CommandTimerPlugin.getInstance().getConditionEngineManager().getRule(simpleCondition.getConditionGroup(), rule);

                if (conditionRule == null) {
                    return;
                }

                ArrayList<NeededValue<?>> neededValues = conditionRule.getNeededValues();
                int i = 3;
                for (ConditionParamField<?> conditionParamField : conditionParamFields) {
                    Optional<NeededValue<?>> optionalNeededValue =
                            neededValues.stream().filter(v -> v.getName().equals(conditionParamField.getName())).findFirst();

                    if (!optionalNeededValue.isPresent()) {
                        continue;
                    }

                    NeededValue<?> neededValue = optionalNeededValue.get();

                    if (neededValue.getType() == ConditionCompare.class) {
                        ConditionParamField<ConditionCompare> conditionCompareParamField =
                                (ConditionParamField<ConditionCompare>) conditionParamField;
                        ClickableItem clickableConditionCompare = ConditionCompareItem.get(
                                conditionCompareParamField,
                                conditionCompare -> {
                                    conditionCompareParamField.setValue(conditionCompare);
                                    condition.getTask().storeInstance();
                                    this.INVENTORY.open(player);
                                });
                        contents.set(1, i, clickableConditionCompare);
                        i++;
                        continue;
                    }

                    // From this point, we should treat native types only
                    String[] lore = new String[]{"",
                            "§7Current value: §e" + conditionParamField.getValue(),
                            "",
                            "§aLeft-Click to edit"};
                    ItemStack item = Items.generateItem("§7Set value: §e" + neededValue.getLabel(), XMaterial.PAPER, lore);
                    ClickableItem clickableItem = ClickableItem.of(item, e -> {
                        if (neededValue.getType() == Double.class) {
                            ConversationFactory conversationFactory = new ConversationFactory(CommandTimerPlugin.getPlugin())
                                    .withModality(true)
                                    .withFirstPrompt(new TextInputConversationPrompt("Enter your value:", data -> {
                                        double text = Double.parseDouble(data);
                                        ((ConditionParamField<Double>) conditionParamField).setValue(text);
                                        condition.getTask().storeInstance();
                                        new ConditionMenu(condition, onClose).INVENTORY.open(player);
                                    }));
                            conversationFactory.buildConversation(player).begin();
                            player.closeInventory();
                            return;
                        }

                        if (neededValue.getType() == String.class) {
                            ConversationFactory conversationFactory = new ConversationFactory(CommandTimerPlugin.getPlugin())
                                    .withModality(true)
                                    .withFirstPrompt(new TextInputConversationPrompt("Enter your value:", text -> {
                                        ((ConditionParamField<String>) conditionParamField).setValue(text);
                                        condition.getTask().storeInstance();
                                        new ConditionMenu(condition, onClose).INVENTORY.open(player);
                                    }));
                            conversationFactory.buildConversation(player).begin();
                            player.closeInventory();
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
