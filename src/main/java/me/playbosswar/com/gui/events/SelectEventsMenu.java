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
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
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
                .title(languageManager.get(LanguageKey.SELECT_EVENTS_GUI_TITLE).replace("$1",
                        extension.getConditionGroupName()))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();

        pagination.setItems(extension.getEvents().stream().map(event -> {
            Optional<EventConfiguration> existingConfiguration = task
                    .getEvents()
                    .stream()
                    .filter(ev ->
                            ev.getConditionGroup().equals(extension.getConditionGroupName()) &&
                                    ev.getEvent().equals(event.getEventName())).findAny();

            List<String> description = Arrays
                    .stream(event.getEventDescription()).map(d -> "&7" + d)
                    .collect(Collectors.toList());
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.addAll(description);
            lore.add("");
            if(existingConfiguration.isPresent() && existingConfiguration.get().isActive()) {
                lore.add(languageManager.get(LanguageKey.STATUS_ACTIVE));
            } else {
                lore.add(languageManager.get(LanguageKey.STATUS_NOT_ACTIVE));
            }
            lore.add("");
            lore.add(languageManager.get(LanguageKey.LEFT_CLICK_EDIT));
            lore.add(languageManager.get(LanguageKey.RIGHT_CLICK_TOGGLE));
            ItemStack item = Items.generateItem("&b" + event.getEventName(), XMaterial.BEACON,
                    lore.toArray(new String[0]));
            return ClickableItem.of(item, e -> {
                final Callback<?> internalCallback = data -> this.INVENTORY.open(player);
                if(e.isLeftClick()) {
                    if(existingConfiguration.isPresent()) {
                        new ConfigureEventMenu(task, extension, event, existingConfiguration.get().getCondition(),
                                internalCallback).INVENTORY.open(player);
                        return;
                    }

                    EventCondition condition = new EventCondition(ConditionType.SIMPLE,
                            new EventSimpleCondition<>("", ""), new ArrayList<>());
                    EventConfiguration configuration = new EventConfiguration(true,
                            extension.getConditionGroupName()
                            , event.getEventName(), condition);
                    task.getEvents().add(configuration);
                    task.storeInstance();

                    new ConfigureEventMenu(task, extension, event, condition, internalCallback).INVENTORY.open(player);
                }

                if(e.isRightClick()) {
                    EventConfiguration configuration;
                    if(existingConfiguration.isPresent()) {
                        configuration = existingConfiguration.get();
                    } else {
                        EventCondition condition = new EventCondition(ConditionType.SIMPLE,
                                new EventSimpleCondition<>("", ""), new ArrayList<>());
                        configuration = new EventConfiguration(true,
                                extension.getConditionGroupName()
                                , event.getEventName(), condition);
                    }

                    configuration.setActive(!configuration.isActive());
                    task.storeInstance();
                    this.INVENTORY.open(player);
                }
            });
        }).toArray(ClickableItem[]::new));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> new MainEventsMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
