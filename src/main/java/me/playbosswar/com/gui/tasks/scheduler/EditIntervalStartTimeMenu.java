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
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;

public class EditIntervalStartTimeMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;
    private final String[] clockLore = {"", languageManager.get(LanguageKey.LEFT_CLICK_EDIT)};

    public EditIntervalStartTimeMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-interval-start-time")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(5, 9)
                .title(languageManager.get(LanguageKey.INTERVAL_START_TIME_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        LocalTime current = task.getIntervalStartTime();
        int hours = current != null ? current.getHour() : 0;
        int minutes = current != null ? current.getMinute() : 0;
        int seconds = current != null ? current.getSecond() : 0;

        // Hours
        contents.set(1, 1, ClickableItem.of(Items.getAddItem(), e -> {
            LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
            task.setIntervalStartTime(t.withHour((t.getHour() + 1) % 24));
            refresh(player);
        }));
        ClickableTextInputButton hoursClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.HOURS_LABEL, String.valueOf(hours)),
                        XMaterial.CLOCK, clockLore),
                LanguageKey.TEXT_INPUT_DEFAULT,
                data -> {
                    int h = Integer.parseInt(data);
                    LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
                    task.setIntervalStartTime(t.withHour(h % 24));
                    refresh(player);
                }
        );
        contents.set(2, 1, hoursClock.getItem());
        contents.set(3, 1, ClickableItem.of(Items.getSubstractItem(), e -> {
            LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
            task.setIntervalStartTime(t.withHour((t.getHour() + 23) % 24));
            refresh(player);
        }));

        // Minutes
        contents.set(1, 3, ClickableItem.of(Items.getAddItem(), e -> {
            LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
            task.setIntervalStartTime(t.withMinute((t.getMinute() + 1) % 60));
            refresh(player);
        }));
        ClickableTextInputButton minutesClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.MINUTES_LABEL, String.valueOf(minutes)),
                        XMaterial.CLOCK, clockLore),
                LanguageKey.TEXT_INPUT_DEFAULT,
                data -> {
                    int m = Integer.parseInt(data);
                    LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
                    task.setIntervalStartTime(t.withMinute(m % 60));
                    refresh(player);
                }
        );
        contents.set(2, 3, minutesClock.getItem());
        contents.set(3, 3, ClickableItem.of(Items.getSubstractItem(), e -> {
            LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
            task.setIntervalStartTime(t.withMinute((t.getMinute() + 59) % 60));
            refresh(player);
        }));

        // Seconds
        contents.set(1, 5, ClickableItem.of(Items.getAddItem(), e -> {
            LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
            task.setIntervalStartTime(t.withSecond((t.getSecond() + 1) % 60));
            refresh(player);
        }));
        ClickableTextInputButton secondsClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.SECONDS_LABEL, String.valueOf(seconds)),
                        XMaterial.CLOCK, clockLore),
                LanguageKey.TEXT_INPUT_DEFAULT,
                data -> {
                    int s = Integer.parseInt(data);
                    LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
                    task.setIntervalStartTime(t.withSecond(s % 60));
                    refresh(player);
                }
        );
        contents.set(2, 5, secondsClock.getItem());
        contents.set(3, 5, ClickableItem.of(Items.getSubstractItem(), e -> {
            LocalTime t = task.getIntervalStartTime() != null ? task.getIntervalStartTime() : LocalTime.MIDNIGHT;
            task.setIntervalStartTime(t.withSecond((t.getSecond() + 59) % 60));
            refresh(player);
        }));

        // Clear button
        ItemStack clearItem = Items.generateItem(
                languageManager.get(LanguageKey.INTERVAL_START_TIME_CLEAR),
                XMaterial.BARRIER);
        contents.set(4, 0, ClickableItem.of(clearItem, e -> {
            task.setIntervalStartTime(null);
            refresh(player);
        }));

        // Back button
        contents.set(4, 8, ClickableItem.of(Items.getBackItem(),
                e -> new MainScheduleMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

    private void refresh(Player player) {
        this.INVENTORY.open(player);
    }
}
