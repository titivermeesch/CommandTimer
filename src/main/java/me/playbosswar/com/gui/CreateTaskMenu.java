package me.playbosswar.com.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import org.bukkit.entity.Player;

public class CreateTaskMenu implements InventoryProvider {
    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("create-task")
            .provider(new MainMenu())
            .manager(Main.getInventoryManager())
            .size(3, 9)
            .title("§9§lCreate a new task").build();

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem()));
    }

    public void update(Player player, InventoryContents contents) {
    }
}
