package me.playbosswar.com.gui.validations;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.utils.ArrayUtils;
import me.playbosswar.com.utils.Items;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConditionTypeItem {
    @NotNull
    public static ClickableItem get(@NotNull List<ConditionType> availableConditionTypes,
                                    @NotNull ConditionType selectedConditionType,
                                    @NotNull Consumer<ConditionType> conditionTypeChange) {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§7The condition type defines the relation");
        lore.add("§7between multiple condition parts");
        lore.add("");
        lore.add("§bAvailable options:");
        availableConditionTypes.forEach(conditionType -> lore.add("§7 - " + conditionType + ": " + conditionType.getDescription()));
        lore.add("");
        lore.add("§7Current: §e" + selectedConditionType);

        ItemStack item = Items.generateItem("§bChange condition type", XMaterial.COMPARATOR, lore.toArray(String[]::new));
        return ClickableItem.of(item, e -> {
            ConditionType nextConditionType = ArrayUtils.getNextValueInArray(availableConditionTypes, selectedConditionType);

            conditionTypeChange.accept(nextConditionType);
        });
    }
}
