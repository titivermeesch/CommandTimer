package me.playbosswar.com.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.integrations.MainIntegrationsMenu;
import me.playbosswar.com.gui.tasks.AllTasksMenu;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public MainMenu() {
        INVENTORY = SmartInventory.builder()
                .id("main")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lCommandTimer")
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        ItemStack createTaskItem = Items.generateItem("§bCreate task",
                                                      XMaterial.ANVIL,
                                                      new String[]{ "",
                                                              "§7A task consists of:",
                                                              "§7  - A set of conditions",
                                                              "§7  - One or more commands",
                                                              "",
                                                              "§7Based on how you configure it, the commands " +
                                                                      "will be",
                                                              "§7executed when the conditions meet."
                                                      });
        ClickableItem createItem = ClickableItem.of(createTaskItem, e ->
                new EditTaskMenu(Main.getTasksManager().createTask()).INVENTORY.open(player)
        );
        contents.set(1, 1, createItem);

        ItemStack listTimersItem = Items.generateItem("§bAll tasks",
                                                      XMaterial.PAPER,
                                                      new String[]{ "",
                                                              "§7Get an overview of your currently",
                                                              "§7loaded tasks"
                                                      });
        ClickableItem listItem = ClickableItem.of(listTimersItem, e -> new AllTasksMenu().INVENTORY.open(player));
        contents.set(1, 2, listItem);

        ItemStack integrationsItem = Items.generateItem("§bIntegrations",
                                                        XMaterial.CRAFTING_TABLE,
                                                        new String[]{ "",
                                                                "§7See all possible integrations",
                                                                "§7and the ones that are currently loaded"
                                                        });
        ClickableItem clickableIntegrationsItem = ClickableItem.of(integrationsItem,
                                                                   e -> new MainIntegrationsMenu().INVENTORY.open(player));
        contents.set(1, 3, clickableIntegrationsItem);

        ItemStack infoItem = Items.generateItem("§bGeneral information",
                                                XMaterial.REDSTONE_TORCH,
                                                new String[]{ "",
                                                        "§7Version: 6.0.0-ALPHA-2" });
        contents.set(1, 7, ClickableItem.empty(infoItem));
    }

    public void update(Player player, InventoryContents contents) {
    }
}

