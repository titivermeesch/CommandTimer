package me.playbosswar.com.gui.integrations;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.hooks.HookType;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainIntegrationsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public MainIntegrationsMenu() {
        INVENTORY = SmartInventory.builder()
                .id("main-integrations")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lIntegrations")
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem()));

        int i = 1;
        for (HookType hookType : HookType.values()) {
            boolean enabled = Main.getHooksManager().isHookEnabled(hookType);
            ItemStack item = enabled ? XMaterial.GREEN_WOOL.parseItem() : XMaterial.RED_WOOL.parseItem();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§b" + hookType.getDisplayName());
            List<String> lore = Arrays.asList(hookType.getLoreDescription());
            lore = new ArrayList<>(lore);
            lore.add("");
            lore.add(enabled ? "§7Status: §a§lActive" : "§7Status: §6§lIntegration not loaded");
            meta.setLore(lore);
            item.setItemMeta(meta);

            contents.set(1, i, ClickableItem.empty(item));
            i++;
        }

        ItemStack back = Items.getBackItem();
        ClickableItem clickableBack = ClickableItem.of(back, e -> new MainMenu().INVENTORY.open(player));
        contents.set(1, 7, clickableBack);
    }

    public void update(Player player, InventoryContents contents) {
    }
}
