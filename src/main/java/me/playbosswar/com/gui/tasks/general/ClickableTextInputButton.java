package me.playbosswar.com.gui.tasks.general;

import fr.minuskube.inv.ClickableItem;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.gui.TextInputManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClickableTextInputButton {
    private final ItemStack item;
    private final LanguageKey inputText;
    private final Callback<String> callback;

    public ClickableTextInputButton(ItemStack item, LanguageKey inputText, Callback<String> callback) {
        this.item = item;
        this.inputText = inputText;
        this.callback = callback;
    }

    public ClickableItem getItem() {
        return ClickableItem.of(this.item, e -> open((Player) e.getWhoClicked()));
    }

    public void open(Player player) {
        player.closeInventory();
        TextInputManager.getInstance().startTextInput(player, inputText, callback);
    }
}
