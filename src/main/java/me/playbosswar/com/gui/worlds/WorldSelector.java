package me.playbosswar.com.gui.worlds;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorldSelector implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final List<String> selectedWorlds;
    private final Callback callback;
    private final boolean allowMulti;

    public WorldSelector(Callback callback, List<String> selectedWorlds, boolean allowMulti) {
        this.callback = callback;
        this.selectedWorlds = selectedWorlds;
        this.allowMulti = allowMulti;
        INVENTORY = SmartInventory.builder()
                .id("task-times-specific")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title("§9§lEdit specific time")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        Pagination pagination = contents.pagination();
        pagination.setItems(getWorlds(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> callback.execute(selectedWorlds)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem[] getWorlds(Player player) {
        List<World> worlds = Bukkit.getWorlds();

        ClickableItem[] items = new ClickableItem[worlds.size()];

        for (int i = 0; i < items.length; i++) {
            World world = worlds.get(i);

            ItemStack item = Items.getToggleItem("§b" + world.getName(), new String[]{}, selectedWorlds.contains(world.getName()));
            items[i] = ClickableItem.of(item, e -> {
                if(!allowMulti) {
                    selectedWorlds.clear();
                }

                if(selectedWorlds.contains(world.getName())) {
                    selectedWorlds.remove(world.getName());
                } else {
                    selectedWorlds.add(world.getName());
                }

                new WorldSelector(callback, selectedWorlds, allowMulti).INVENTORY.open(player);
            });
        }

        return items;
    }
}
