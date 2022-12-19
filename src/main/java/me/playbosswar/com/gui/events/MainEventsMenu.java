package me.playbosswar.com.gui.events;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.MenuUtils;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

// Menu will show all possible event extensions that you can choose from and maybe also configuration
public class MainEventsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public MainEventsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("main-events")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.EVENTS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        List<ConditionExtension> extensions = CommandTimerPlugin
                .getInstance()
                .getConditionEngineManager()
                .getConditionExtensions();
        List<ConditionExtension> extensionsWithEvents = extensions
                .stream()
                .filter(e -> e.getEvents().size() > 0)
                .collect(Collectors.toList());

        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        Pagination pagination = contents.pagination();
        pagination.setItems(extensionsWithEvents.stream().map(extension -> {
            ItemStack item = MenuUtils.getExtensionItem(extension, false, true);
            return ClickableItem.of(item,
                    e -> new SelectEventsMenu(task, extension).INVENTORY.open(player));
        }).toArray(ClickableItem[]::new));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
