package me.playbosswar.com.gui.tasks.commands;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.gui.tasks.scheduler.EditIntervalMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AllCommandsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public AllCommandsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-commands")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.TASK_COMMANDS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        Pagination pagination = contents.pagination();
        pagination.setItems(getAllCommands(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));

        String[] addItemLore = languageManager.getList(LanguageKey.ADD_COMMAND_LORE).toArray(new String[] {});
        ItemStack addItem = Items.generateItem(LanguageKey.ADD_COMMAND, XMaterial.ANVIL, addItemLore);
        ClickableItem clickableAddItem = ClickableItem.of(addItem, e -> {
            TaskCommand taskCommand = new TaskCommand("say This is my command",
                    Gender.CONSOLE);
            task.addCommand(taskCommand);
            new EditCommandMenu(task, taskCommand).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableAddItem);

        List<String> selectModeLore = languageManager.getList(LanguageKey.GENDER_LORE);
        selectModeLore.add("");
        selectModeLore.add(languageManager.get(LanguageKey.GUI_CURRENT, task.getCommandExecutionMode().toString()));
        if (task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL)) {
            selectModeLore.add(
                    languageManager.get(LanguageKey.CURRENT_INTERVAL,
                            task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL)
                                    ? task.getCommandExecutionInterval().toString()
                                    : ""));
        }

        selectModeLore.add("");
        selectModeLore.add(languageManager.get(LanguageKey.LEFT_CLICK_SWITCH));
        selectModeLore.add(task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL)
                ? languageManager.get(LanguageKey.RIGHT_CLICK_CHANGE_INTERVAL)
                : "");

        ItemStack selectModeItem = Items.generateItem(LanguageKey.EXECUTION_MODE, XMaterial.DIAMOND_SHOVEL,
                selectModeLore.toArray(new String[] {}));
        ItemMeta selectedModeItemMeta = selectModeItem.getItemMeta();
        selectedModeItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ClickableItem clickableSelectModeItem = ClickableItem.of(selectModeItem, e -> {
            if (e.getClick().equals(ClickType.LEFT)) {
                task.switchCommandExecutionMode();
                this.INVENTORY.open(player);
                return;
            }

            new EditIntervalMenu(task, task.getCommandExecutionInterval(),
                    e2 -> new AllCommandsMenu(task).INVENTORY.open(player)).INVENTORY.open(player);
        });
        contents.set(0, 8, clickableSelectModeItem);

        List<String> commandSettingsLore = languageManager.getList(LanguageKey.OPEN_COMMANDS_SETTINGS_LORE);
        commandSettingsLore.add("");
        commandSettingsLore.add(languageManager.get(LanguageKey.LEFT_CLICK_EDIT));
        ItemStack commandSettingsItem = Items.generateItem(LanguageKey.OPEN_COMMANDS_SETTINGS, XMaterial.REPEATER,
                commandSettingsLore.toArray(new String[] {}));
        ClickableItem clickableCommandSettingsItem = ClickableItem.of(commandSettingsItem,
                e -> new CommandSettingsMenu(task).INVENTORY.open(player));
        contents.set(5, 0, clickableCommandSettingsItem);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem[] getAllCommands(Player p) {
        List<TaskCommand> commands = task.getCommands();

        if (commands == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[commands.size()];

        for (int i = 0; i < items.length; i++) {
            TaskCommand taskCommand = commands.get(i);
            String command = taskCommand.getCommand();
            String[] lore = new String[] {
                    "",
                    languageManager.get(LanguageKey.GENDER, taskCommand.getGender().toString()),
                    languageManager.get(LanguageKey.ITEM_DESCRIPTION_LORE, taskCommand.getDescription()),
                    "",
                    languageManager.get(LanguageKey.LEFT_CLICK_EDIT),
                    languageManager.get(LanguageKey.RIGHT_CLICK_DELETE)
            };
            ItemStack item = Items.generateItem("§b" + command, XMaterial.COMMAND_BLOCK_MINECART, lore);

            items[i] = ClickableItem.of(item, e -> {
                if (e.getClick().equals(ClickType.LEFT)) {
                    new EditCommandMenu(task, taskCommand).INVENTORY.open(p);
                    return;
                }

                if (e.getClick().equals(ClickType.RIGHT)) {
                    task.removeCommand(taskCommand);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
