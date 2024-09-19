package me.playbosswar.com.gui.conditions;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConditionsMenu implements InventoryProvider {
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    public SmartInventory INVENTORY;
    private final Task task;
    private final Condition condition;
    private final Callback<?> onClose;

    public ConditionsMenu(Task task, Condition condition, Callback<?> onClose) {
        this.task = task;
        this.condition = condition;
        this.onClose = onClose;
        INVENTORY = SmartInventory.builder()
                .id("condition-parts")
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
            Condition newCondition = new Condition(
                    ConditionType.SIMPLE,
                    new ArrayList<>(),
                    new SimpleCondition());
            this.condition.addCondition(newCondition);
            task.storeInstance();

            new ConditionMenu(task, newCondition, internalCallback).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableCreate);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem[] getAllConditions(Player p) {
        final Callback<?> internalCallback = data -> INVENTORY.open(p);
        List<Condition> conditions = this.condition.getConditions();

        if(conditions == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[conditions.size()];

        for(int i = 0; i < items.length; i++) {
            Condition condition = conditions.get(i);
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
                SimpleCondition simpleCondition = condition.getSimpleCondition();
                String conditionGroup = simpleCondition.getConditionGroup();
                String rule = simpleCondition.getRule();

                lore = new String[]{"",
                        languageManager.get(LanguageKey.TYPE, conditionType.toString()),
                        "",
                        languageManager.get(LanguageKey.CURRENT_CONFIGURATION),
                        languageManager.get(LanguageKey.CONDITION_GROUP) + (conditionGroup == null ?
                                languageManager.get(LanguageKey.NOT_SET) :
                                "§e" + conditionGroup),
                        languageManager.get(LanguageKey.CONDITION_RULE) + (rule == null ?
                                languageManager.get(LanguageKey.NOT_SET) : "§e" + rule),
                        "",
                        languageManager.get(LanguageKey.LEFT_CLICK_EDIT),
                        languageManager.get(LanguageKey.RIGHT_CLICK_DELETE),
                };
            }


            ItemStack item = Items.generateItem(languageManager.get(LanguageKey.CONDITION) + (i + 1),
                    XMaterial.COMMAND_BLOCK, lore);
            items[i] = ClickableItem.of(item, e -> {
                if(e.getClick().equals(ClickType.LEFT)) {
                    new ConditionMenu(task, condition, internalCallback).INVENTORY.open(p);
                    return;
                }

                if(e.getClick().equals(ClickType.RIGHT)) {
                    this.condition.removeCondition(condition);
                    task.storeInstance();
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
