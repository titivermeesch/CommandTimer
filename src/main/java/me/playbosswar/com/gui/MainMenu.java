package me.playbosswar.com.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.integrations.MainIntegrationsMenu;
import me.playbosswar.com.gui.tasks.AllTasksMenu;
import me.playbosswar.com.gui.tasks.ScheduledExecutionsMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

public class MainMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public MainMenu() {
        INVENTORY = SmartInventory.builder()
                .id("main")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.MAIN_MENU_GUI_TITLE))
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ItemStack listTimersItem = Items.generateItem(LanguageKey.ALL_TASKS_ITEM,
                XMaterial.PAPER,
                languageManager.getList(LanguageKey.ALL_TASKS_LORE).toArray(new String[]{}));
        ClickableItem listItem = ClickableItem.of(listTimersItem, e -> new AllTasksMenu().INVENTORY.open(player));
        contents.set(1, 1, listItem);

        ItemStack integrationsItem = Items.generateItem(LanguageKey.INTEGRATIONS_ITEM,
                XMaterial.CRAFTING_TABLE,
                languageManager.getList(LanguageKey.INTEGRATIONS_LORE).toArray(new String[]{}));
        ClickableItem clickableIntegrationsItem = ClickableItem.of(integrationsItem,
                e -> new MainIntegrationsMenu().INVENTORY.open(player));
        contents.set(1, 2, clickableIntegrationsItem);

        ItemStack scheduledExecutionsItem = Items.generateItem(LanguageKey.SCHEDULED_EXECUTIONS_ITEM,
                XMaterial.CLOCK,
                languageManager.getList(LanguageKey.SCHEDULED_EXECUTIONS_LORE).toArray(new String[]{}));
        ClickableItem scheduledExecutionsClickableItem = ClickableItem.of(scheduledExecutionsItem,
                e -> new ScheduledExecutionsMenu().INVENTORY.open(player));
        contents.set(1, 3, scheduledExecutionsClickableItem);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        ItemStack infoItem = Items.generateItem(LanguageKey.GENERAL_INFORMATION_ITEM,
                XMaterial.REDSTONE_TORCH,
                new String[]{"",
                        languageManager.get(LanguageKey.VERSION,
                                CommandTimerPlugin.getPlugin().getDescription().getVersion()),
                        languageManager.get(LanguageKey.TIME, currentTime)
                });
        contents.set(1, 7, ClickableItem.empty(infoItem));
    }

    public void update(Player player, InventoryContents contents) {
    }
}

