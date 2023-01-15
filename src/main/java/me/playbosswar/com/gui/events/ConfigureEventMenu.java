package me.playbosswar.com.gui.events;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.events.EventCondition;
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.gui.MenuUtils;
import me.playbosswar.com.gui.conditions.ConditionsMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// Condition menu for specific event where the condition can be built
public class ConfigureEventMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final ConditionExtension extension;
    private final EventExtension eventExtension;
    private final EventCondition condition;
    private final Task task;

    public ConfigureEventMenu(Task task, ConditionExtension extension, EventExtension eventExtension,
                              EventCondition condition) {
        this.task = task;
        this.extension = extension;
        this.eventExtension = eventExtension;
        this.condition = condition;

        INVENTORY = SmartInventory.builder()
                .id("configure-event")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.CONFIGURE_EVENT_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        final Callback<String> internalCallback = data -> INVENTORY.open(player);
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        // 1. Item for condition type (SIMPLE, AND, OR, NOT)
        ClickableItem conditionTypeItem = MenuUtils.getConditionTypeItem(condition, type -> {
            condition.setConditionType(type);
            new ConfigureEventMenu(task, extension, eventExtension, condition).INVENTORY.open(player);
        });
        contents.set(1, 1, conditionTypeItem);

        // 2. With simple/or, we show a second item to configure all the values
        if(condition.getConditionType().equals(ConditionType.SIMPLE) || condition.getConditionType().equals(ConditionType.NOT)) {
            String[] valuesLore =
                    languageManager.getList(LanguageKey.CONFIGURE_EVENT_VALUES_ITEM_LORE).toArray(new String[]{});
            ItemStack valuesItem = Items.generateItem(
                    LanguageKey.CONFIGURE_EVENT_VALUES_ITEM_TITLE,
                    XMaterial.PAPER,
                    valuesLore);
            contents.set(1, 2, ClickableItem.of(valuesItem, e -> new ConfigureEventValuesMenu(
                    task,
                    extension,
                    eventExtension,
                    condition
            ).INVENTORY.open(player)));
            return;
        }

        // 3. If and/or, show menu to create subconditions
        String[] conditionPartsLore =
                languageManager.getList(LanguageKey.CONDITION_TYPE_LORE).toArray(new String[0]);
        ItemStack conditionsItem = Items.generateItem(LanguageKey.CONDITION_PARTS_TITLE,
                XMaterial.CRAFTING_TABLE, conditionPartsLore);
        ClickableItem clickableConditions = ClickableItem.of(conditionsItem, e -> new EventConditionsMenu(condition,
                extension, eventExtension,
                internalCallback).INVENTORY.open(player));
        contents.set(1, 2, clickableConditions);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
