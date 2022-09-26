package me.playbosswar.com.utils;

import com.cryptomorin.xseries.XMaterial;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Items {
    private static final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public static ItemStack generateItem(String title, XMaterial material) {
        ItemStack item = material.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack generateItem(LanguageKey languageKey, XMaterial material) {
        return generateItem(languageManager.get(languageKey), material);
    }

    public static ItemStack generateItem(String title, XMaterial material, String[] lore) {
        ItemStack item = material.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack generateItem(LanguageKey languageKey, XMaterial material, String[] lore) {
        return generateItem(languageManager.get(languageKey), material, lore);
    }

    public static ItemStack getToggleItem(String title, String[] lore, boolean value) {
        ItemStack item = value ? XMaterial.LIME_DYE.parseItem() : XMaterial.GRAY_DYE.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getToggleItem(LanguageKey languageKey, String[] lore, boolean value) {
        return getToggleItem(languageManager.get(languageKey), lore, value);
    }

    public static ItemStack getAddItem() {
        return generateItem("§b+", XMaterial.STONE_BUTTON);
    }

    public static ItemStack getSubstractItem() {
        return generateItem("§b-", XMaterial.STONE_BUTTON);
    }

    public static ItemStack getBackItem() {
        return generateItem("§cBack", XMaterial.REDSTONE);
    }
}
