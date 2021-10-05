package me.playbosswar.com.gui.validations;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;

public class SimpleConditionMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final SimpleCondition simpleCondition;
    private final Callback onClose;

    public SimpleConditionMenu(SimpleCondition simpleCondition, Callback onClose) {
        this.simpleCondition = simpleCondition;
        this.onClose = onClose;
        INVENTORY = SmartInventory.builder()
                .id("simple-condition")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lSimple Condition")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        // Select executor
        // Select compare
        // Select numeric value
        // Select string value

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
