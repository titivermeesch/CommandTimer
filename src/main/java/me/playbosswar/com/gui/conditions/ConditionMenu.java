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
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.ArrayUtils;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ConditionMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Condition condition;
    private final Callback<?> onClose;
    List<ConditionType> availableConditionTypes = new ArrayList<>();
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public ConditionMenu(Condition condition, Callback<?> onClose) {
        this.condition = condition;
        this.onClose = onClose;

        // Populate condition types array
        availableConditionTypes.add(ConditionType.AND);
        availableConditionTypes.add(ConditionType.OR);
        availableConditionTypes.add(ConditionType.SIMPLE);
        availableConditionTypes.add(ConditionType.NOT);

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

        ClickableItem conditionTypeItem = getConditionTypeItem(type -> {
            condition.setConditionType(type);
            this.INVENTORY.open(player);
        });
        contents.set(1, 1, conditionTypeItem);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));

        if(condition.getConditionType().equals(ConditionType.AND) || condition.getConditionType().equals(ConditionType.OR)) {
            String[] conditionPartsLore =
                    languageManager.getList(LanguageKey.CONDITION_TYPE_LORE).toArray(new String[0]);
            ItemStack conditionsItem = Items.generateItem(LanguageKey.CONDITION_PARTS_TITLE,
                    XMaterial.CRAFTING_TABLE, conditionPartsLore);
            ClickableItem clickableConditions = ClickableItem.of(conditionsItem, e -> new ConditionsMenu(condition,
                    internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableConditions);
        } else {
            SimpleCondition simpleCondition = condition.getSimpleCondition();

            String conditionGroup = simpleCondition.getConditionGroup();
            String rule = simpleCondition.getRule();
            List<String> simpleConditionLore = languageManager.getList(LanguageKey.SIMPLE_CONDITION_LORE);
            simpleConditionLore.addAll(List.of(new String[]{
                    "",
                    languageManager.get(LanguageKey.GUI_CURRENT, ""),
                    languageManager.get(LanguageKey.CONDITION_GROUP) + (conditionGroup == null ?
                            languageManager.get(LanguageKey.NOT_SET) :
                            "§e" + conditionGroup),
                    languageManager.get(LanguageKey.CONDITION_RULE) + (rule == null ?
                            languageManager.get(LanguageKey.NOT_SET) : "§e" + rule)}));
            ItemStack simpleConditionItem = Items.generateItem(languageManager.get(LanguageKey.CONFIGURE_CONDITION),
                    XMaterial.CRAFTING_TABLE,
                    simpleConditionLore.toArray(String[]::new));
            ClickableItem clickableSimpleCondition = ClickableItem.of(simpleConditionItem,
                    e -> new SimpleConditionMenu(simpleCondition, internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableSimpleCondition);

            ArrayList<ConditionParamField<?>> conditionParamFields = simpleCondition.getConditionParamFields();
            if(conditionParamFields != null && conditionParamFields.size() > 0) {
                ConditionRule conditionRule =
                        CommandTimerPlugin.getInstance().getConditionEngineManager().getRule(simpleCondition.getConditionGroup(), rule);

                if(conditionRule == null) {
                    return;
                }

                ArrayList<NeededValue<?>> neededValues = conditionRule.getNeededValues();
                int i = 3;
                for(ConditionParamField<?> conditionParamField : conditionParamFields) {
                    Optional<NeededValue<?>> optionalNeededValue =
                            neededValues.stream().filter(v -> v.getName().equals(conditionParamField.getName())).findFirst();

                    if(optionalNeededValue.isEmpty()) {
                        continue;
                    }

                    NeededValue<?> neededValue = optionalNeededValue.get();

                    if(neededValue.getType() == ConditionCompare.class) {
                        ConditionParamField<ConditionCompare> conditionCompareParamField =
                                (ConditionParamField<ConditionCompare>) conditionParamField;
                        ClickableItem clickableConditionCompare = ConditionCompareItem.get(conditionCompareParamField
                                , conditionCompare -> {
                                    conditionCompareParamField.setValue(conditionCompare);
                                    condition.getTask().storeInstance();
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
                            ConversationFactory conversationFactory =
                                    new ConversationFactory(CommandTimerPlugin.getPlugin())
                                            .withModality(true)
                                            .withFirstPrompt(new TextInputConversationPrompt(data -> {
                                                double text = Double.parseDouble(data);
                                                ((ConditionParamField<Double>) conditionParamField).setValue(text);
                                                condition.getTask().storeInstance();
                                                new ConditionMenu(condition, onClose).INVENTORY.open(player);
                                            }));
                            conversationFactory.buildConversation(player).begin();
                            player.closeInventory();
                            return;
                        }

                        if(neededValue.getType() == String.class) {
                            ConversationFactory conversationFactory =
                                    new ConversationFactory(CommandTimerPlugin.getPlugin())
                                            .withModality(true)
                                            .withFirstPrompt(new TextInputConversationPrompt(text -> {
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

    private ClickableItem getConditionTypeItem(
            @NotNull Consumer<ConditionType> conditionTypeChange) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.addAll(languageManager.getList(LanguageKey.CONDITION_TYPE_LORE));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.AVAILABLE_OPTIONS));
        this.availableConditionTypes.forEach(conditionType -> lore.add("§7 - " + conditionType + ": " + conditionType.getDescription()));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.GUI_CURRENT, this.condition.getConditionType().toString()));

        ItemStack item = Items.generateItem("§bChange condition type", XMaterial.COMPARATOR,
                lore.toArray(new String[0]));
        return ClickableItem.of(item, e -> {
            ConditionType nextConditionType = ArrayUtils.getNextValueInArray(
                    this.availableConditionTypes,
                    this.condition.getConditionType());
            conditionTypeChange.accept(nextConditionType);
        });
    }
}
