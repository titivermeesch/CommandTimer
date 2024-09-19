package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.general.ClickableTextInputButton;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;

import java.time.LocalTime;

public class EditHourMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;
    private final TaskTime taskTime;
    private final boolean isTime2;
    private final String[] clockLore = {"", languageManager.get(LanguageKey.LEFT_CLICK_EDIT)};

    public EditHourMenu(Task task, TaskTime taskTime, boolean isTime2) {
        this.task = task;
        this.taskTime = taskTime;
        this.isTime2 = isTime2;
        INVENTORY = SmartInventory.builder()
                .id("edit-hour")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.EDIT_HOURS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        LocalTime usedTime = isTime2 ? taskTime.getTime2() : taskTime.getTime1();

        if(usedTime == null) {
            if(isTime2) {
                taskTime.setTime2(LocalTime.of(14, 0, 0));
                usedTime = taskTime.getTime2();
            } else {
                taskTime.setTime1(LocalTime.of(14, 0, 0));
                usedTime = taskTime.getTime1();
            }
            task.storeInstance();
        }

        LocalTime finalUsedTime = usedTime;
        // Hours
        contents.set(0, 2, ClickableItem.of(Items.getAddItem(), e -> {
            setTime(task, finalUsedTime.plusHours(1));
            refresh(player);
        }));
        ClickableTextInputButton hoursClock = new ClickableTextInputButton(
                Items.generateItem(
                        languageManager.get(LanguageKey.HOURS_LABEL, String.valueOf(usedTime.getHour())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int hours = Integer.parseInt(data);
                    setTime(task, finalUsedTime.withHour(hours));
                    refresh(player);
                }
        );
        contents.set(1, 2, hoursClock.getItem());
        contents.set(2, 2, ClickableItem.of(Items.getSubstractItem(), e -> {
            setTime(task, finalUsedTime.minusHours(1));
            refresh(player);
        }));

        // Minutes
        contents.set(0, 4, ClickableItem.of(Items.getAddItem(), e -> {
            setTime(task, finalUsedTime.plusMinutes(1));
            refresh(player);
        }));
        ClickableTextInputButton minutesClock = new ClickableTextInputButton(
                Items.generateItem(
                        languageManager.get(LanguageKey.MINUTES_LABEL, String.valueOf(usedTime.getMinute())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int minutes = Integer.parseInt(data);
                    setTime(task, finalUsedTime.withMinute(minutes));
                    refresh(player);
                }
        );
        contents.set(1, 4, minutesClock.getItem());
        contents.set(2, 4, ClickableItem.of(Items.getSubstractItem(), e -> {
            setTime(task, finalUsedTime.minusMinutes(1));
            refresh(player);
        }));

        // Seconds
        contents.set(0, 6, ClickableItem.of(Items.getAddItem(), e -> {
            setTime(task, finalUsedTime.plusSeconds(1));
            refresh(player);
        }));
        ClickableTextInputButton secondsClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.SECONDS_LABEL, String.valueOf(usedTime.getSecond())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int seconds = Integer.parseInt(data);
                    setTime(task, finalUsedTime.withSecond(seconds));
                    refresh(player);
                }
        );
        contents.set(1, 6, secondsClock.getItem());
        contents.set(2, 6, ClickableItem.of(Items.getSubstractItem(), e -> {
            setTime(task, finalUsedTime.minusSeconds(1));
            refresh(player);
        }));

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(),
                e -> new EditSpecificTimeMenu(task, taskTime).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void refresh(Player player) {
        this.INVENTORY.open(player);
    }

    private void setTime(Task task, LocalTime time) {
        if(isTime2) {
            taskTime.setTime2(time);
        } else {
            taskTime.setTime1(time);
        }

        task.storeInstance();
    }
}
