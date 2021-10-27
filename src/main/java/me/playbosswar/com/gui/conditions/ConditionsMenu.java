package me.playbosswar.com.gui.conditions;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConditionsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Condition condition;
    private final Callback onClose;

    public ConditionsMenu(Condition condition, Callback onClose) {
        this.condition = condition;
        this.onClose = onClose;
        INVENTORY = SmartInventory.builder()
                .id("condition-parts")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title("§9§lCondition Parts")
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
        Pagination pagination = contents.pagination();

        pagination.setItems(getAllConditions(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        ItemStack createItem = Items.generateItem("§bAdd condition", XMaterial.ANVIL);
        ClickableItem clickableCreate = ClickableItem.of(createItem, e -> {
            Condition newCondition = new Condition(
                    ConditionType.SIMPLE,
                    new ArrayList<>(),
                    new SimpleCondition(condition.getTask()),
                    condition.getTask());
            this.condition.addCondition(newCondition);

            new ConditionMenu(newCondition, internalCallback).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableCreate);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem[] getAllConditions(Player p) {
        final Callback internalCallback = new Callback() {
            @Override
            public <T> void execute(T data) {
                INVENTORY.open(p);
            }
        };
        List<Condition> conditions = this.condition.getConditions();

        if (conditions == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[conditions.size()];

        for (int i = 0; i < items.length; i++) {
            Condition condition = conditions.get(i);
            ConditionType conditionType = condition.getConditionType();

            String[] lore;
            if (conditionType.equals(ConditionType.OR) || conditionType.equals(ConditionType.AND)) {
                lore = new String[]{
                        "",
                        "§7Type: §e" + conditionType,
                        "",
                        "§aLeft-Click to edit",
                        "§cRight-Click to delete",
                };
            } else {
                SimpleCondition simpleCondition = condition.getSimpleCondition();
                String conditionGroup = simpleCondition.getConditionGroup();
                String rule = simpleCondition.getRule();

                lore = new String[]{ "",
                        "§7Type: §e" + conditionType,
                        "",
                        "§bCurrent configuration:",
                        "§7 - Condition group: " + (conditionGroup == null ? "§eNot Set" : "§e" + conditionGroup),
                        "§7 - Rule: " + (rule == null ? "§eNot Set" : "§e" + rule),
                        "",
                        "§aLeft-Click to edit",
                        "§cRight-Click to delete",
                };
            }


            ItemStack item = Items.generateItem("§bCondition " + (i + 1), XMaterial.COMMAND_BLOCK, lore);
            items[i] = ClickableItem.of(item, e -> {
                if (e.getClick().equals(ClickType.LEFT)) {
                    new ConditionMenu(condition, internalCallback).INVENTORY.open(p);
                    return;
                }

                if (e.getClick().equals(ClickType.RIGHT)) {
                    this.condition.removeCondition(condition);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
