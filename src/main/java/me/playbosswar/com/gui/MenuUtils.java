package me.playbosswar.com.gui;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Items;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuUtils {
    private static final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

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
}
