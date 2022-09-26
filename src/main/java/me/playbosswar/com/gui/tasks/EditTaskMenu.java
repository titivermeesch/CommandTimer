package me.playbosswar.com.gui.tasks;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.commands.AllCommandsMenu;
import me.playbosswar.com.gui.tasks.general.GeneralLimitsMenu;
import me.playbosswar.com.gui.tasks.general.TextInputConversationPrompt;
import me.playbosswar.com.gui.tasks.scheduler.MainScheduleMenu;
import me.playbosswar.com.gui.conditions.ConditionMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditTaskMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public EditTaskMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("edit-task")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.EDIT_TASK_GUI_TITLE, task.getName()))
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ConversationFactory conversationFactory = new ConversationFactory(CommandTimerPlugin.getPlugin())
                .withModality(true)
                .withFirstPrompt(new TextInputConversationPrompt(LanguageKey.NEW_TASK_INPUT, text -> {
                    task.setName(text);
                    new EditTaskMenu(task).INVENTORY.open(player);
                }));

        String[] nameLore = languageManager.getList(LanguageKey.CHANGE_TASK_DISPLAY_NAME_LORE).toArray(new String[]{});
        ItemStack nameItem = Items.generateItem(LanguageKey.CHANGE_TASK_DISPLAY_NAME_TITLE, XMaterial.PAPER, nameLore);
        ClickableItem clickableNameItem = ClickableItem.of(nameItem, e -> {
            player.closeInventory();
            conversationFactory.buildConversation(player).begin();
        });
        contents.set(1, 1, clickableNameItem);

        String[] scheduleLore =
                languageManager.getList(LanguageKey.CHANGE_TASK_SCHEDULE_SETTINGS_LORE).toArray(new String[]{});
        ItemStack scheduleItem = Items.generateItem(LanguageKey.CHANGE_TASK_SCHEDULE_SETTINGS_TITLE, XMaterial.CLOCK,
                scheduleLore);
        ClickableItem clickableScheduleItem = ClickableItem.of(scheduleItem,
                e -> new MainScheduleMenu(task).INVENTORY.open(player));
        contents.set(1, 2, clickableScheduleItem);

        String[] commandsLore = languageManager.getList(LanguageKey.CHANGE_TASK_COMMANDS_LORE).toArray(new String[]{});
        ItemStack commandsItem = Items.generateItem(LanguageKey.CHANGE_TASK_COMMANDS_TITLE, XMaterial.COMMAND_BLOCK,
                commandsLore);
        ClickableItem clickableCommandsItem = ClickableItem.of(commandsItem,
                e -> new AllCommandsMenu(task).INVENTORY.open(player));
        contents.set(1, 3, clickableCommandsItem);

        String[] generalLimitsLore =
                languageManager.getList(LanguageKey.CHANGE_TASK_LIMITS_LORE).toArray(new String[]{});
        ItemStack generalLimitsItem = Items.generateItem(LanguageKey.CHANGE_TASK_LIMITS_TITLE, XMaterial.GOLD_INGOT,
                generalLimitsLore);
        ClickableItem clickableGeneralLimitsItem = ClickableItem.of(generalLimitsItem,
                e -> new GeneralLimitsMenu(task).INVENTORY.open(player));
        contents.set(1, 4, clickableGeneralLimitsItem);

        Callback<String> conditionItemCallback = data -> INVENTORY.open(player);
        String[] conditionLore =
                languageManager.getList(LanguageKey.CHANGE_TASK_CONDITIONS_LORE).toArray(new String[]{});
        ItemStack conditionItem = Items.generateItem(LanguageKey.CHANGE_TASK_CONDITIONS_TITLE, XMaterial.COMPARATOR,
                conditionLore);
        ClickableItem clickableCondition = ClickableItem.of(conditionItem,
                e -> new ConditionMenu(
                        task.getCondition(),
                        conditionItemCallback
                ).INVENTORY.open(player));
        contents.set(1, 5, clickableCondition);

        boolean isActive = task.isActive();
        String[] activationLore = new String[]{"",
                languageManager.get(LanguageKey.TASK_ACTIVATION_LORE),
                "",
                languageManager.get(LanguageKey.GUI_CURRENT, (isActive ?
                        languageManager.get(LanguageKey.STATUS_ACTIVE) :
                        languageManager.get(LanguageKey.STATUS_NOT_ACTIVE)))
        };
        contents.set(1, 6, ClickableItem.of(Items.getToggleItem(LanguageKey.TASK_ACTIVATION_TITLE, activationLore,
                isActive), e -> {
            task.toggleActive();
            this.INVENTORY.open(player);
        }));

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(), e -> new AllTasksMenu().INVENTORY.open(player)));
    }

    public void update(Player player, InventoryContents contents) {
    }
}
