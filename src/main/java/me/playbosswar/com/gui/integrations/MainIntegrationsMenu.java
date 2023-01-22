package me.playbosswar.com.gui.integrations;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.sentry.Sentry;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.hooks.HookType;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainIntegrationsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public MainIntegrationsMenu() {
        INVENTORY = SmartInventory.builder()
                .id("main-integrations")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.INTEGRATIONS_GUI_TITLE))
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        int i = 1;
        for(HookType hookType : HookType.values()) {
            boolean enabled = CommandTimerPlugin.getInstance().getHooksManager().isHookEnabled(hookType);
            ItemStack item = enabled ? XMaterial.GREEN_WOOL.parseItem() : XMaterial.RED_WOOL.parseItem();
            if(item == null) {
                Sentry.captureException(new Exception("Tried creating intergrations menu but item not found"));
                return;
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§b" + hookType.getDisplayName());
            List<String> lore = hookType.getLoreDescription();
            lore = new ArrayList<>(lore);
            lore.add("");
            lore.add(languageManager.get(LanguageKey.STATUS,
                    enabled ?
                            languageManager.get(LanguageKey.STATUS_ACTIVE) :
                            languageManager.get(LanguageKey.STATUS_NOT_ACTIVE)));
            meta.setLore(lore);
            item.setItemMeta(meta);

            contents.set(1, i, ClickableItem.empty(item));
            i++;
        }

        ItemStack back = Items.getBackItem();
        ClickableItem clickableBack = ClickableItem.of(back, e -> new MainMenu().INVENTORY.open(player));
        contents.set(2, 8, clickableBack);
    }

    public void update(Player player, InventoryContents contents) {
    }
}
