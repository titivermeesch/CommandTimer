package me.playbosswar.com.gui.events;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.events.EventCondition;
import me.playbosswar.com.api.events.EventConfiguration;
import me.playbosswar.com.api.events.EventSimpleCondition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Menu to select which specific events to listen to (we already selected the extension at this point)
public class SelectEventsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final ConditionExtension extension;
    private final Task task;

    public SelectEventsMenu(Task task, ConditionExtension extension) {
        this.extension = extension;
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("edit-event")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.SELECT_EVENTS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();

        List<ClickableItem> items = extension.getEvents().stream().map(event -> {
            // TODO: Show active state of event, show configured values when possible
            ItemStack item = Items.generateItem(event.getEventName(), XMaterial.BEACON, event.getEventDescription());
            return ClickableItem.of(item, e -> {
                Optional<EventConfiguration> existingConfiguration = task
                        .getEvents()
                        .stream()
                        .filter(ev ->
                                ev.getConditionGroup().equals(extension.getConditionGroupName()) &&
                                        ev.getEvent().equals(event.getEventName())).findAny();

                if(e.isLeftClick()) {
                    EventConfiguration configuration;
                    if(existingConfiguration.isPresent()) {
                        configuration = existingConfiguration.get();
                    } else {
                        EventCondition condition = new EventCondition(task, ConditionType.SIMPLE,
                                new EventSimpleCondition<>(task, "", ""), new ArrayList<>());
                        configuration = new EventConfiguration(task, true,
                                extension.getConditionGroupName()
                                , event.getEventName(), condition);
                    }

                    configuration.setActive(!configuration.isActive());
                    task.storeInstance();
                    // Refresh
                    this.INVENTORY.open(player);
                    return;
                }

                if(e.isRightClick()) {
                    if(existingConfiguration.isPresent()) {
                        new ConfigureEventMenu(task, extension, event, existingConfiguration.get().getCondition()).INVENTORY.open(player);
                        return;
                    }

                    EventCondition condition = new EventCondition(task, ConditionType.SIMPLE,
                            new EventSimpleCondition<>(task, "", ""), new ArrayList<>());
                    EventConfiguration configuration = new EventConfiguration(task, true,
                            extension.getConditionGroupName()
                            , event.getEventName(), condition);
                    task.getEvents().add(configuration);
                    task.storeInstance();

                    new ConfigureEventMenu(task, extension, event, condition).INVENTORY.open(player);
                }
            });
        }).collect(Collectors.toList());

        pagination.setItems(items.toArray(new ClickableItem[0]));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> new MainEventsMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
