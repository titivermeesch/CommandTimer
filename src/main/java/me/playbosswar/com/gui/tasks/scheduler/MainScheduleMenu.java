package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainScheduleMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public MainScheduleMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-scheduler")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.TASK_SCHEDULER_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ArrayList<String> intervalItemLore = languageManager.getList(LanguageKey.TASK_INTERVAL_ITEM_LORE);
        intervalItemLore.add("");
        intervalItemLore.add(languageManager.get(LanguageKey.GUI_CURRENT, task.getInterval().toString()));

        ItemStack intervalItem = Items.generateItem(LanguageKey.TASK_INTERVAL_ITEM_TITLE, XMaterial.CLOCK,
                intervalItemLore.toArray(new String[]{}));
        ClickableItem clickableSecondsItem = ClickableItem.of(
                intervalItem,
                e -> new EditIntervalMenu(
                        task,
                        task.getInterval(),
                        e2 -> new MainScheduleMenu(task).INVENTORY.open(player)).INVENTORY.open(player));
        contents.set(1, 1, clickableSecondsItem);

        String[] hoursLore = languageManager.getList(LanguageKey.TASK_INTERVAL_HOURS_LORE).toArray(new String[]{});
        ItemStack timesItem = Items.generateItem(LanguageKey.TASK_INTERVAL_HOURS_TITLE, XMaterial.CLOCK, hoursLore);
        ClickableItem clickableTimesItem = ClickableItem.of(timesItem,
                e -> new EditTimesMenu(task).INVENTORY.open(player));
        contents.set(1, 2, clickableTimesItem);

        String[] daysLore = languageManager.getList(LanguageKey.TASK_INTERVAL_DAYS_LORE).toArray(new String[]{});
        ItemStack daysItem = Items.generateItem(LanguageKey.TASK_INTERVAL_DAYS_TITLE, XMaterial.CLOCK, daysLore);
        ClickableItem clickableDaysItem = ClickableItem.of(daysItem,
                e -> new EditDaysMenu(task).INVENTORY.open(player));
        contents.set(1, 3, clickableDaysItem);

        LocalTime startTime = task.getIntervalStartTime();
        String startTimeDisplay = startTime != null
                ? startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                : languageManager.get(LanguageKey.NOT_SET);
        ArrayList<String> startTimeLore = languageManager.getList(LanguageKey.INTERVAL_START_TIME_LORE, startTimeDisplay);
        ItemStack startTimeItem = Items.generateItem(LanguageKey.INTERVAL_START_TIME_TITLE, XMaterial.COMPASS,
                startTimeLore.toArray(new String[]{}));
        ClickableItem clickableStartTimeItem = ClickableItem.of(startTimeItem,
                e -> new EditIntervalStartTimeMenu(task).INVENTORY.open(player));
        contents.set(1, 4, clickableStartTimeItem);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
