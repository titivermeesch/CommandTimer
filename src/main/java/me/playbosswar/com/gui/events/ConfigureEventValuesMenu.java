package me.playbosswar.com.gui.events;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.ArrayUtils;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

// Menu that shows all the possible values
public class ConfigureEventValuesMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final ConditionExtension extension;
    private final Task task;
    private final EventExtension eventExtension;

    public ConfigureEventValuesMenu(Task task, ConditionExtension extension, EventExtension eventExtension) {
        this.extension = extension;
        this.task = task;
        this.eventExtension = eventExtension;

        INVENTORY = SmartInventory.builder()
                .id("configure-event-values")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.CONFIGURE_EVENT_VALUES_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();
        List<ClickableItem> items = eventExtension.getReturnedValues().stream().map(v -> {
            ItemStack item = Items.generateItem("&7" + v.getLabel(), XMaterial.PAPER);
            return ClickableItem.of(item, e -> {
                if(e.isLeftClick()) {
                    // Prompt value
                }

                if(e.isRightClick()) {
                    ConditionCompare nextConditionCompare = ArrayUtils
                            .getNextValueInArray(ConditionCompare.values(), (ConditionCompare) v.getDefaultValue());

                }
            });
        }).collect(Collectors.toList());
        pagination.setItems(items.toArray(new ClickableItem[0]));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
