package me.playbosswar.com.gui.validations;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
        final Callback internalCallback = new Callback() {
            @Override
            public <T> void execute(T data) {
                INVENTORY.open(player);
            }
        };

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

        if (condition.getConditionType().equals(ConditionType.AND) || condition.getConditionType().equals(ConditionType.OR)) {
            String[] conditionPartsLore = new String[]{ "",
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
            ItemStack simpleConditionItem = Items.generateItem("§bConfigure condition", XMaterial.CRAFTING_TABLE, simpleConditionLore);
            ClickableItem clickableSimpleCondition = ClickableItem.of(simpleConditionItem,
                                                                      e -> new SimpleConditionMenu(
                                                                              simpleCondition,
                                                                              internalCallback).INVENTORY.open(player));
            contents.set(1, 2, clickableSimpleCondition);


        }

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
