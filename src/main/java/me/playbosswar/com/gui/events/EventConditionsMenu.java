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
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.api.events.EventSimpleCondition;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

// List of available conditions under a parent EventCondition condition
public class EventConditionsMenu implements InventoryProvider {
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    public SmartInventory INVENTORY;
    private final EventCondition condition;
    private final ConditionExtension extension;
    private final EventExtension eventExtension;
    private final Callback<String> callback;

    public EventConditionsMenu(EventCondition condition, ConditionExtension extension, EventExtension eventExtension,
                               Callback<String> callback) {
        this.condition = condition;
        this.extension = extension;
        this.eventExtension = eventExtension;
        this.callback = callback;
        INVENTORY = SmartInventory.builder()
                .id("event-condition-parts")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.CONDITION_PARTS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        final Callback<?> internalCallback = data -> INVENTORY.open(player);

        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();

        pagination.setItems(getAllConditions(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        ItemStack createItem = Items.generateItem(LanguageKey.ADD_CONDITION, XMaterial.ANVIL);
        ClickableItem clickableCreate = ClickableItem.of(createItem, e -> {
            EventCondition newCondition = new EventCondition(
                    condition.getTask(),
                    ConditionType.SIMPLE,
                    new EventSimpleCondition<>(condition.getTask(), "", ""),
                    new ArrayList<>());
            this.condition.addCondition(newCondition);

            new ConfigureEventMenu(condition.getTask(), extension, eventExtension, condition, internalCallback).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableCreate);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> callback.execute(null)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem[] getAllConditions(Player p) {
        final Callback<?> internalCallback = data -> INVENTORY.open(p);
        List<EventCondition> conditions = this.condition.getConditions();

        if(conditions == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[conditions.size()];

        for(int i = 0; i < items.length; i++) {
            EventCondition condition = conditions.get(i);
            ConditionType conditionType = condition.getConditionType();

            String[] lore;
            if(conditionType.equals(ConditionType.OR) || conditionType.equals(ConditionType.AND)) {
                lore = new String[]{
                        "",
                        languageManager.get(LanguageKey.TYPE, conditionType.toString()),
                        "",
                        languageManager.get(LanguageKey.LEFT_CLICK_EDIT),
                        languageManager.get(LanguageKey.RIGHT_CLICK_DELETE)
                };
            } else {
                EventSimpleCondition<?> simpleCondition = condition.getSimpleCondition();
                String fieldName = simpleCondition.getFieldName();
                String value = (String) simpleCondition.getValue();
                ConditionCompare compare = simpleCondition.getCompare();
                // TODO: We could fetch the related NeededValue from the extension itself and get the label

                lore = new String[]{"",
                        languageManager.get(LanguageKey.TYPE, conditionType.toString()),
                        "",
                        languageManager.get(LanguageKey.CURRENT_CONFIGURATION),
                        languageManager.get(LanguageKey.CONDITION_GROUP) + (fieldName == null ?
                                languageManager.get(LanguageKey.NOT_SET) :
                                "§e" + fieldName),
                        languageManager.get(LanguageKey.CONDITION_RULE) + (value == null ?
                                languageManager.get(LanguageKey.NOT_SET) : "§e" + value),
                        "",
                        languageManager.get(LanguageKey.LEFT_CLICK_EDIT),
                        languageManager.get(LanguageKey.RIGHT_CLICK_DELETE),
                };
            }


            ItemStack item = Items.generateItem(languageManager.get(LanguageKey.CONDITION) + (i + 1),
                    XMaterial.COMMAND_BLOCK, lore);
            items[i] = ClickableItem.of(item, e -> {
                if(e.getClick().equals(ClickType.LEFT)) {
                    new ConfigureEventMenu(condition.getTask(), extension, eventExtension, condition, internalCallback).INVENTORY.open(p);
                    return;
                }

                if(e.getClick().equals(ClickType.RIGHT)) {
                    this.condition.removeCondition(condition);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
