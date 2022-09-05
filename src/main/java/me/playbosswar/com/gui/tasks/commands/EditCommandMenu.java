package me.playbosswar.com.gui.tasks.commands;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.general.TextInputConversationPrompt;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.utils.Items;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditCommandMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final TaskCommand taskCommand;

    public EditCommandMenu(TaskCommand taskCommand) {
        this.taskCommand = taskCommand;
        INVENTORY = SmartInventory.builder()
                .id("edit-command")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title("§9§lEdit command")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] editCommandLore = new String[]{"",
                "§7Edit the actual command",
                "",
                "§7Current: §e" + taskCommand.getCommand()
        };

        ConversationFactory conversationFactory = new ConversationFactory(CommandTimerPlugin.getPlugin())
                .withModality(true)
                .withFirstPrompt(new TextInputConversationPrompt<String>("Enter your command:", text -> {
                    taskCommand.setCommand(text);
                    new EditCommandMenu(taskCommand).INVENTORY.open(player);
                }));

        ItemStack editCommandItem = Items.generateItem("§bChange command", XMaterial.PAPER, editCommandLore);
        ClickableItem clickableCommandItem = ClickableItem.of(editCommandItem, e -> {
            conversationFactory.buildConversation(player).begin();
            player.closeInventory();
        });
        contents.set(1, 1, clickableCommandItem);

        String[] genderLore = new String[]{"",
                "§7Genders are one of the core concepts of",
                "§7CommandTimer. They allow you to specify",
                "§7how your task is executed.",
                "",
                "§7Current: §e" + taskCommand.getGender().toString(),
                "",
                "§bAvailable genders:",
                "",
                "§7  - §eOPERATOR: §7All the commands in the task",
                "§7    will be executed for each individual player,",
                "§7    by the player. This means that CommandTimer will",
                "§7    force the player to execute the commands but ignore",
                "§7    the permissions linked to them.",
                "",
                "§7  - §ePLAYER: §7Same as above. CommandTimer will force",
                "§7    the player to execute the commands, but will take",
                "§7    into account the possible permissions the player",
                "§7    has or is lacking.",
                "",
                "§7  - §eCONSOLE: §7Execute the command in the console only",
                "",
                "§7  - §eCONSOLE PER USER: §7Execute the command in the console",
                "§7    for each individual player. This works very well with",
                "§7    placeholders",
        };
        ItemStack genderItem = Items.generateItem("§bGender", XMaterial.CHAINMAIL_HELMET, genderLore);
        ClickableItem clickableGenderItem = ClickableItem.of(genderItem, e -> {
            taskCommand.toggleGender();
            this.INVENTORY.open(player);
        });
        contents.set(1, 2, clickableGenderItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(),
                e -> new AllCommandsMenu(taskCommand.getTask()).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
