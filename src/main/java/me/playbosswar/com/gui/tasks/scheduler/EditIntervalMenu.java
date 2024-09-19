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
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class EditIntervalMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;
    private final TaskInterval interval;
    private final Consumer<InventoryClickEvent> consumer;
    private final String[] clockLore = {"", languageManager.get(LanguageKey.LEFT_CLICK_EDIT)};

    public EditIntervalMenu(Task task, TaskInterval interval, Consumer<InventoryClickEvent> consumer) {
        this.task = task;
        this.interval = interval;
        this.consumer = consumer;
        INVENTORY = SmartInventory.builder()
                .id("task-interval")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(5, 9)
                .title(languageManager.get(LanguageKey.TASK_INTERVAL_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        // Days
        contents.set(1, 1, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementDays();
            task.storeInstance();
            refresh(player);
        }));
        ClickableTextInputButton daysClocks = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.DAYS_LABEL, String.valueOf(interval.getDays())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int days = Integer.parseInt(data);
                    interval.setDays(days);
                    task.storeInstance();
                    refresh(player);
                }
        );
        contents.set(2, 1, daysClocks.getItem());
        contents.set(3, 1, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementDays();
            task.storeInstance();
            refresh(player);
        }));

        // Hours
        contents.set(1, 3, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementHours();
            task.storeInstance();
            refresh(player);
        }));
        ClickableTextInputButton hoursClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.HOURS_LABEL, String.valueOf(interval.getHours())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int hours = Integer.parseInt(data);
                    interval.setHours(hours);
                    task.storeInstance();
                    refresh(player);
                }
        );
        contents.set(2, 3, hoursClock.getItem());
        contents.set(3, 3, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementHours();
            task.storeInstance();
            refresh(player);
        }));

        // Minutes
        contents.set(1, 5, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementMinutes();
            task.storeInstance();
            refresh(player);
        }));
        ClickableTextInputButton minutesClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.MINUTES_LABEL,
                                String.valueOf(interval.getMinutes())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int minutes = Integer.parseInt(data);
                    interval.setMinutes(minutes);
                    task.storeInstance();
                    refresh(player);
                }
        );
        contents.set(2, 5, minutesClock.getItem());
        contents.set(3, 5, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementMinutes();
            task.storeInstance();
            refresh(player);
        }));

        // Seconds
        contents.set(1, 7, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementSeconds();
            task.storeInstance();
            refresh(player);
        }));
        ClickableTextInputButton secondsClock = new ClickableTextInputButton(
                Items.generateItem(languageManager.get(LanguageKey.SECONDS_LABEL,
                                String.valueOf(interval.getSeconds())),
                        XMaterial.CLOCK,
                        clockLore),
                data -> {
                    int seconds = Integer.parseInt(data);
                    interval.setSeconds(seconds);
                    task.storeInstance();
                    refresh(player);
                }
        );
        contents.set(2, 7, secondsClock.getItem());
        contents.set(3, 7, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementSeconds();
            task.storeInstance();
            refresh(player);
        }));

        contents.set(4, 8, ClickableItem.of(Items.getBackItem(), consumer));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void refresh(Player player) {
        this.INVENTORY.open(player);
    }
}
