package me.playbosswar.com.gui.conditions;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.ConditionRule;
import me.playbosswar.com.api.ConditionRules;
import me.playbosswar.com.api.NeededValue;
import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.MenuUtils;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SimpleConditionMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final SimpleCondition simpleCondition;
    private final Callback<?> onClose;
    private String selectedConditionGroup;
    private String ruleName;

    public SimpleConditionMenu(SimpleCondition simpleCondition, Callback<?> onClose) {
        this.simpleCondition = simpleCondition;
        this.onClose = onClose;
        this.selectedConditionGroup = simpleCondition.getConditionGroup();
        this.ruleName = simpleCondition.getRule();
        INVENTORY = SmartInventory.builder()
                .id("simple-condition")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.SIMPLE_CONDITION_GUI_TITLE))
                .build();
    }

    private void changeSelectedConditionGroup(String newGroup) {
        String conditionGroup = simpleCondition.getConditionGroup();
        if(conditionGroup != null && conditionGroup.equals(newGroup)) {
            simpleCondition.setConditionGroup(null);
            simpleCondition.setRule(null);
            this.selectedConditionGroup = null;
            this.ruleName = null;
            return;
        }

        simpleCondition.setConditionGroup(newGroup);
        this.selectedConditionGroup = newGroup;
    }

    private void changeSelectedRule(String rule) {
        simpleCondition.setRule(rule);
        this.ruleName = rule;

        ConditionRule conditionRule = CommandTimerPlugin
                .getInstance()
                .getConditionEngineManager()
                .getRule(this.selectedConditionGroup, rule);

        if(conditionRule.getNeededValues() == null) {
            simpleCondition.setConditionParamFields(null);
            simpleCondition.getTask().storeInstance();
            return;
        }

        ArrayList<ConditionParamField<?>> conditionParamFields = new ArrayList<>();
        ArrayList<NeededValue<?>> neededValues = conditionRule.getNeededValues();

        for(NeededValue<?> neededValue : neededValues) {
            conditionParamFields.add(new ConditionParamField<>(
                    neededValue.getName(),
                    neededValue.getDefaultValue()));
        }

        simpleCondition.setConditionParamFields(conditionParamFields);
        simpleCondition.getTask().storeInstance();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        contents.fillRow(2, ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        Pagination pagination = contents.pagination();
        pagination.setItems(getActiveRules(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY, 14, 5, 3, 1);

        int i = 1;
        for(ClickableItem clickableItem : getAllConditionGroups(player)) {
            contents.set(1, i, clickableItem);
            i++;
        }

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> onClose.execute(null)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem[] getActiveRules(Player p) {
        ConditionExtension conditionExtension = CommandTimerPlugin
                .getInstance()
                .getConditionEngineManager()
                .getConditionExtension(selectedConditionGroup);

        if(conditionExtension == null) {
            return new ClickableItem[0];
        }

        ConditionRules rules = conditionExtension.getRules();
        ClickableItem[] items = new ClickableItem[rules.size()];

        List<ConditionRule> rulesList = new ArrayList<>();
        for(ConditionRule value : rules) {
            rulesList.add(value);
        }

        for(int i = 0; i < rulesList.size(); i++) {
            ConditionRule rule = rulesList.get(i);

            ItemStack item = Items.generateItem(
                    "§b" + rule.getName(),
                    XMaterial.COMPARATOR,
                    new String[]{"", "§7" + rule.getDescription()});

            // Make item glowing when selected
            if(this.ruleName != null && this.ruleName.equals(rule.getName())) {
                item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
            }

            items[i] = ClickableItem.of(item, e -> {
                if(e.getClick().equals(ClickType.LEFT)) {
                    changeSelectedRule(rule.getName());
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }

    private ClickableItem[] getAllConditionGroups(Player p) {
        List<ConditionExtension> conditionExtensions =
                CommandTimerPlugin.getInstance().getConditionEngineManager().getConditionExtensions();
        ClickableItem[] items = new ClickableItem[conditionExtensions.size()];

        for(int i = 0; i < items.length; i++) {
            ConditionExtension conditionExtension = conditionExtensions.get(i);
            String conditionGroupName = conditionExtension.getConditionGroupName();
            ItemStack item = MenuUtils.getExtensionItem(conditionExtension);

            // Make item glowing when selected
            if(this.selectedConditionGroup != null && this.selectedConditionGroup.equals(conditionGroupName)) {
                item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
            }

            items[i] = ClickableItem.of(item, e -> {
                if(e.getClick().equals(ClickType.LEFT)) {
                    changeSelectedConditionGroup(conditionGroupName);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
