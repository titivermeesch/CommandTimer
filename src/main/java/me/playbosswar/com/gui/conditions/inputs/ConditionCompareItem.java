package me.playbosswar.com.gui.conditions.inputs;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.ConditionParamField;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.ArrayUtils;
import me.playbosswar.com.utils.Items;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ConditionCompareItem {
    private static final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    @NotNull
    public static ClickableItem get(ConditionParamField<ConditionCompare> conditionParamField,
                                    Consumer<ConditionCompare> conditionCompareChange) {
        List<String> lore = new ArrayList<>();

        Iterator<ConditionCompare> it = Arrays.stream(ConditionCompare.values()).iterator();

        lore.add("");
        lore.addAll(languageManager.getList(LanguageKey.CONDITION_TYPE_LORE));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.AVAILABLE_OPTIONS));
        while(it.hasNext()) {
            lore.add("§7 - " + it.next());
        }
        lore.add("");
        lore.add(languageManager.get(LanguageKey.GUI_CURRENT, String.valueOf(conditionParamField.getValue())));
        lore.add("");
        lore.add(languageManager.get(LanguageKey.LEFT_CLICK_SWITCH));

        ItemStack item = Items.generateItem(languageManager.get(LanguageKey.CHANGE_CONDITION), XMaterial.PAPER,
                lore.toArray(new String[0]));
        return ClickableItem.of(item, e -> {
            ConditionCompare nextConditionCompare = ArrayUtils.getNextValueInArray(ConditionCompare.values(),
                    conditionParamField.getValue());

            conditionCompareChange.accept(nextConditionCompare);
        });
    }
}
