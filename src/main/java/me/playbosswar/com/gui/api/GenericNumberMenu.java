package me.playbosswar.com.gui.api;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

public class GenericNumberMenu implements InventoryProvider {
    private final SmartInventory INVENTORY;
    private final Consumer<Integer> consumer;
    private final Player player;
    private int value;

    public GenericNumberMenu(Player player, String inventoryTitle, int value, Consumer<Integer> consumer) {
        this.value = value;
        this.consumer = consumer;
        this.player = player;
        INVENTORY = SmartInventory.builder()
                .id(UUID.randomUUID().toString())
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title(inventoryTitle)
                .build();
        INVENTORY.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ItemStack min100 = Items.generateItem("§c-100", XMaterial.STONE_BUTTON);
        ClickableItem clickableMin100 = ClickableItem.of(min100, e -> decrementValue(100));
        contents.set(1, 1, clickableMin100);

        ItemStack min10 = Items.generateItem("§c-10", XMaterial.STONE_BUTTON);
        ClickableItem clickableMin10 = ClickableItem.of(min10, e -> decrementValue(10));
        contents.set(1, 2, clickableMin10);

        ItemStack min1 = Items.generateItem("§c-1", XMaterial.STONE_BUTTON);
        ClickableItem clickableMin1 = ClickableItem.of(min1, e -> decrementValue(1));
        contents.set(1, 3, clickableMin1);

        ItemStack valueItem = Items.generateItem("§b" + value, XMaterial.REDSTONE_BLOCK);
        contents.set(1, 4, ClickableItem.empty(valueItem));

        ItemStack plus1 = Items.generateItem("§a+1", XMaterial.STONE_BUTTON);
        ClickableItem clickablePlus1 = ClickableItem.of(plus1, e -> incrementValue(1));
        contents.set(1, 5, clickablePlus1);

        ItemStack plus10 = Items.generateItem("§a+10", XMaterial.STONE_BUTTON);
        ClickableItem clickablePlus10 = ClickableItem.of(plus10, e -> incrementValue(10));
        contents.set(1, 6, clickablePlus10);

        ItemStack plus100 = Items.generateItem("§a+100", XMaterial.STONE_BUTTON);
        ClickableItem clickablePlus100 = ClickableItem.of(plus100, e -> incrementValue(100));
        contents.set(1, 7, clickablePlus100);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> consumer.accept(value)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void decrementValue(int amount) {
        value -= amount;

        if (value < -1) {
            value = -1;
        }

        this.INVENTORY.open(player);
    }

    private void incrementValue(int amount) {
        value += amount;
        this.INVENTORY.open(player);
    }
}
