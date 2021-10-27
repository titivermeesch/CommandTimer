package me.playbosswar.com.gui.conditions.inputs;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.ConditionParamField;
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
    @NotNull
    public static ClickableItem get(ConditionParamField<ConditionCompare> conditionParamField,
                                    Consumer<ConditionCompare> conditionCompareChange) {
        List<String> lore = new ArrayList<>();

        Iterator<ConditionCompare> it = Arrays.stream(ConditionCompare.values()).iterator();

        lore.add("");
        lore.add("§7The condition type defines the relation");
        lore.add("§7between multiple condition parts");
        lore.add("");
        lore.add("§bAvailable options:");
        while (it.hasNext()) {
            lore.add("§7 - " + it.next());
        }
        lore.add("");
        lore.add("§7Current: §e" + conditionParamField.getValue());
        lore.add("");
        lore.add("§aLeft-Click to switch");

        ItemStack item = Items.generateItem("§bChange condition compare", XMaterial.PAPER, lore.toArray(new String[0]));
        return ClickableItem.of(item, e -> {
            ConditionCompare nextConditionCompare = ArrayUtils.getNextValueInArray(ConditionCompare.values(),
                                                                                   conditionParamField.getValue());

            conditionCompareChange.accept(nextConditionCompare);
        });
    }
}
