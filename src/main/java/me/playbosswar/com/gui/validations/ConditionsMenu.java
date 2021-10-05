package me.playbosswar.com.gui.validations;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.Main;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import me.playbosswar.com.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
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
                .manager(Main.getInventoryManager())
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

        ItemStack createItem = Items.generateItem("§bAdd condition", XMaterial.LIME_DYE);
        ClickableItem clickableCreate = ClickableItem.of(createItem, e -> {
            Condition newCondition = new Condition(ConditionType.SIMPLE, null);
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

        if(conditions == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[conditions.size()];

        for (int i = 0; i < items.length; i++) {
            Condition condition = conditions.get(i);
            String[] lore = new String[]{ "",
                    "",
                    "§aLeft-Click to edit",
                    "§cRight-Click to delete",
            };

            ItemStack item = Items.generateItem("§bCondition " + (i + 1), XMaterial.COMMAND_BLOCK, lore);
            items[i] = ClickableItem.of(item, e -> {
                if (e.getClick().equals(ClickType.LEFT)) {
                    new ConditionMenu(condition, internalCallback).INVENTORY.open(p);
                    return;
                }

                if (e.getClick().equals(ClickType.RIGHT)) {
                    //                    try {
                    //                        Main.getTasksManager().removeTask(task);
                    //                        this.INVENTORY.open(p);
                    //                    } catch (IOException ioException) {
                    //                        Messages.sendFailedIO(p);
                    //                        ioException.printStackTrace();
                    //                    }
                }
            });
        }

        return items;
    }
}
