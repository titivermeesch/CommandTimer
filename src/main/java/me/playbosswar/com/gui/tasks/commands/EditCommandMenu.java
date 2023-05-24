package me.playbosswar.com.gui.tasks.commands;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.gui.tasks.general.TextInputConversationPrompt;
import me.playbosswar.com.gui.tasks.scheduler.EditIntervalMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.utils.Items;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditCommandMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final TaskCommand taskCommand;

    public EditCommandMenu(TaskCommand taskCommand) {
        this.taskCommand = taskCommand;
        INVENTORY = SmartInventory.builder()
                .id("edit-command")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.EDIT_COMMAND_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] editCommandLore = new String[]{"",
                languageManager.get(LanguageKey.EDIT_COMMAND_DESCRIPTION),
                "",
                languageManager.get(LanguageKey.GUI_CURRENT, taskCommand.getCommand())
        };

        ConversationFactory conversationFactory = new ConversationFactory(CommandTimerPlugin.getPlugin())
                .withModality(true)
                .withFirstPrompt(new TextInputConversationPrompt(LanguageKey.ENTER_COMMAND_INPUT, text -> {
                    taskCommand.setCommand(text);
                    new EditCommandMenu(taskCommand).INVENTORY.open(player);
                }));

        ItemStack editCommandItem = Items.generateItem(LanguageKey.CHANGE_COMMAND, XMaterial.PAPER, editCommandLore);
        ClickableItem clickableCommandItem = ClickableItem.of(editCommandItem, e -> {
            conversationFactory.buildConversation(player).begin();
            player.closeInventory();
        });
        contents.set(1, 1, clickableCommandItem);


        ItemStack genderItem = Items.generateItem(LanguageKey.GENDER_ITEM, XMaterial.CHAINMAIL_HELMET,
                languageManager.getList(LanguageKey.GENDER_SELECTOR_LORE, taskCommand.getGender().toString()).toArray(new String[]{}));
        ClickableItem clickableGenderItem = ClickableItem.of(genderItem, e -> {
            taskCommand.toggleGender();
            this.INVENTORY.open(player);
        });
        contents.set(1, 2, clickableGenderItem);

        if(!taskCommand.getGender().equals(Gender.CONSOLE)) {
            ItemStack intervalItem = Items.generateItem(LanguageKey.TASK_INTERVAL_ITEM_TITLE, XMaterial.CLOCK,
                    languageManager.getList(LanguageKey.COMMAND_INTERVAL_LORE, taskCommand.getInterval().toString()).toArray(new String[]{}));
            ClickableItem clickableIntervalItem = ClickableItem.of(intervalItem,
                    e -> new EditIntervalMenu(taskCommand.getInterval(),
                            ev -> new EditCommandMenu(taskCommand).INVENTORY.open(player)).INVENTORY.open(player));
            contents.set(1, 3, clickableIntervalItem);
        }

        if(taskCommand.getTask().getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
            ItemStack delayItem = Items.generateItem(LanguageKey.COMMAND_DELAY_TITLE, XMaterial.CLOCK,
                    languageManager.getList(LanguageKey.COMMAND_DELAY_LORE, taskCommand.getDelay().toString()).toArray(new String[]{}));
            ClickableItem clickableDelayItem = ClickableItem.of(delayItem,
                    e -> new EditIntervalMenu(taskCommand.getDelay(),
                            ev -> new EditCommandMenu(taskCommand).INVENTORY.open(player)).INVENTORY.open(player));

            // Calculate position for item
            if(!taskCommand.getGender().equals(Gender.CONSOLE)) {
                contents.set(1, 4, clickableDelayItem);
            } else {
                contents.set(1, 3, clickableDelayItem);
            }
        }

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(),
                e -> new AllCommandsMenu(taskCommand.getTask()).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
