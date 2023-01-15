package me.playbosswar.com.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.conditionsengine.validations.BaseCondition;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.ArrayUtils;
import me.playbosswar.com.utils.Items;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MenuUtils {
    private static final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private static final List<ConditionType> availableConditionTypes = Arrays.asList(
            ConditionType.AND,
            ConditionType.OR,
            ConditionType.SIMPLE,
            ConditionType.NOT
    );

    public static ItemStack getExtensionItem(ConditionExtension extension, boolean showRulesCount,
                                             boolean showEventsCount) {
        String conditionGroupName = extension.getConditionGroupName();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(Arrays.asList(extension.getDescription()));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.AUTHOR, extension.getAuthor()));
        lore.add(languageManager.get(LanguageKey.VERSION, extension.getVersion()));
        if(showRulesCount) {
            lore.add(languageManager.get(LanguageKey.RULES_COUNT,
                    String.valueOf(extension.getRules().size())));
        }
        if(showEventsCount) {
            lore.add(languageManager.get(LanguageKey.EVENTS_COUNT,
                    String.valueOf(extension.getEvents().size())));
        }
        lore.add("");
        lore.add(languageManager.get(LanguageKey.LEFT_CLICK_SELECT));

        return Items.generateItem("§b" + conditionGroupName, extension.getGroupIcon(),
                lore.toArray(new String[0]));
    }

    public static ClickableItem getConditionTypeItem(
            BaseCondition<?, ?> condition,
            @NotNull Consumer<ConditionType> conditionTypeChange) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.addAll(languageManager.getList(LanguageKey.CONDITION_TYPE_LORE));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.AVAILABLE_OPTIONS));
        availableConditionTypes.forEach(conditionType -> lore.add("§7 - " + conditionType + ": " + conditionType.getDescription()));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.GUI_CURRENT, condition.getConditionType().toString()));

        ItemStack item = Items.generateItem("§bChange condition type", XMaterial.COMPARATOR,
                lore.toArray(new String[0]));
        return ClickableItem.of(item, e -> {
            ConditionType nextConditionType = ArrayUtils.getNextValueInArray(
                    availableConditionTypes,
                    condition.getConditionType());
            conditionTypeChange.accept(nextConditionType);
        });
    }
}
