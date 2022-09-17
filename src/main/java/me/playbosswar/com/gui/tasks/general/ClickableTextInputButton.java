package me.playbosswar.com.gui.tasks.general;

import fr.minuskube.inv.ClickableItem;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Callback;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClickableTextInputButton {
    private final ItemStack item;
    private final String requestText = "Enter your new value:";
    private final Callback<String> callback;

    public ClickableTextInputButton(ItemStack item, Callback<String> callback) {
        this.item = item;
        this.callback = callback;
    }

    public ClickableItem getItem() {
        return ClickableItem.of(this.item, e -> open((Player) e.getWhoClicked()));
    }

    public void open(Player player) {
        ConversationFactory conversationFactory = new ConversationFactory(CommandTimerPlugin.getPlugin())
                .withModality(true)
                .withFirstPrompt(new TextInputConversationPrompt(this.requestText, callback));
        conversationFactory.buildConversation(player).begin();
        player.closeInventory();
    }
}
