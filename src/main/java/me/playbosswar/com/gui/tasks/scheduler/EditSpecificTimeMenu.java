package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.worlds.WorldSelector;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EditSpecificTimeMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final TaskTime taskTime;

    public EditSpecificTimeMenu(TaskTime taskTime) {
        this.taskTime = taskTime;
        INVENTORY = SmartInventory.builder()
                .id("task-times-specific")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.SPECIFIC_TIME_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] time1Lore = new String[]{"",
                languageManager.get(LanguageKey.GUI_CURRENT, taskTime.getTime1() == null ?
                        languageManager.get(LanguageKey.NOT_SET) :
                        taskTime.getTime1().toString()),
                "",
                languageManager.get(LanguageKey.LEFT_CLICK_EDIT),
        };
        ItemStack time1Item = Items.generateItem(LanguageKey.TIME_ONE_TITLE, XMaterial.CLOCK, time1Lore);
        ClickableItem clickableTime1Item = ClickableItem.of(time1Item,
                e -> new EditHourMenu(taskTime, false).INVENTORY.open(player));
        contents.set(1, 1, clickableTime1Item);

        List<String> time2Lore = languageManager.getList(LanguageKey.TIME_TWO_LORE, (taskTime.getTime2() == null ?
                "Not set" : taskTime.getTime2().toString()));
        time2Lore.add("");
        time2Lore.add(languageManager.get(LanguageKey.LEFT_CLICK_EDIT));
        time2Lore.add(languageManager.get(LanguageKey.RIGHT_CLICK_DELETE));

        ItemStack time2Item = Items.generateItem(LanguageKey.TIME_TWO_TITLE, XMaterial.CLOCK,
                time2Lore.toArray(new String[]{}));
        ClickableItem clickableTime2Item = ClickableItem.of(time2Item, e -> {
            if(e.isLeftClick()) {
                new EditHourMenu(taskTime, true).INVENTORY.open(player);
            }

            if(e.isRightClick()) {
                taskTime.setTime2(null);
                this.INVENTORY.open(player);
            }
        });
        contents.set(1, 2, clickableTime2Item);

        String[] minecraftTimeLore = languageManager.getList(LanguageKey.MINECRAFT_TIME_LORE).toArray(new String[]{});
        ItemStack minecraftTimeItem = Items.getToggleItem(LanguageKey.MINECRAFT_TIME_TITLE, minecraftTimeLore,
                taskTime.isMinecraftTime());
        contents.set(1, 3, ClickableItem.of(minecraftTimeItem, e -> {
            taskTime.toggleMinecraftTime();
            this.INVENTORY.open(player);
        }));

        if(taskTime.isMinecraftTime()) {
            String[] worldLore = languageManager.getList(
                    LanguageKey.MINECRAFT_TIME_LORE,
                    (taskTime.getWorld() == null ? languageManager.get(LanguageKey.NOT_SET) : taskTime.getWorld())).toArray(new String[]{});
            ItemStack worldItem = Items.generateItem(LanguageKey.USED_MINECRAFT_WORLD, XMaterial.MAP, worldLore);
            Callback<List<String>> worldCallback = worlds -> {
                taskTime.setWorld(worlds.get(0));
                new EditSpecificTimeMenu(taskTime).INVENTORY.open(player);
            };

            ArrayList<String> worlds = new ArrayList<>();
            worlds.add(taskTime.getWorld());
            contents.set(1, 4, ClickableItem.of(worldItem, e -> new WorldSelector(worldCallback,
                    worlds, false).INVENTORY.open(player)));
        }

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(),
                e -> new EditTimesMenu(taskTime.getTask()).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
