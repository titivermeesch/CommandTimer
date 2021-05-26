package me.playbosswar.com.gui.api;

import me.playbosswar.com.Main;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class GenericTextMenu {
    /**
     * Ask user input via an anvil GUI
     *
     * @param title
     * @param defaultText
     * @param consumer
     */
    public GenericTextMenu(Player player, String title, String defaultText, Consumer<String> consumer) {
        new AnvilGUI.Builder()
                .onComplete((p, text) -> {
                    consumer.accept(text);
                    return AnvilGUI.Response.close();
                })
                .text(defaultText)
                .title(title)
                .plugin(Main.getPlugin())
                .open(player);
    }

    /**
     * Ask user input via an anvil GUI
     *
     * @param title    Title of the GUI
     * @param consumer Returns entered text by user
     */
    public GenericTextMenu(Player player, String title, Consumer<String> consumer) {
        this(player, title, "", consumer);
    }
}
