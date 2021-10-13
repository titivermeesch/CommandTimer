package me.playbosswar.com.gui.tasks;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.commands.AllCommandsMenu;
import me.playbosswar.com.gui.tasks.general.GeneralLimitsMenu;
import me.playbosswar.com.gui.tasks.scheduler.MainScheduleMenu;
import me.playbosswar.com.gui.conditions.ConditionMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditTaskMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;

    public EditTaskMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("edit-task")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title("§9§lEdit " + task.getName())
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] nameLore = new String[]{ "", "§7Change the display name of this task" };
        ItemStack nameItem = Items.generateItem("§bTask name", XMaterial.PAPER, nameLore);
        ClickableItem clickableNameItem = ClickableItem.of(nameItem, e -> {
            TaskNameMenu taskNameMenu = new TaskNameMenu(player, task);
            taskNameMenu.INVENTORY.open(player);
        });
        contents.set(1, 1, clickableNameItem);

        String[] scheduleLore = new String[]{ "", "§7Choose when this task will be executed" };
        ItemStack scheduleItem = Items.generateItem("§bSchedule settings", XMaterial.CLOCK, scheduleLore);
        ClickableItem clickableScheduleItem = ClickableItem.of(scheduleItem,
                                                               e -> new MainScheduleMenu(task).INVENTORY.open(player));
        contents.set(1, 2, clickableScheduleItem);

        String[] commandsLore = new String[]{ "", "§7Choose which commands to execute" };
        ItemStack commandsItem = Items.generateItem("§bCommands", XMaterial.COMMAND_BLOCK, commandsLore);
        ClickableItem clickableCommandsItem = ClickableItem.of(commandsItem,
                                                               e -> new AllCommandsMenu(task).INVENTORY.open(player));
        contents.set(1, 3, clickableCommandsItem);

        String[] generalLimitsLore = new String[]{ "",
                "§7Add more limits to this task to",
                "§7limit when the task can be executed.",
                "",
                "§bAvailable limits:",
                "§7  - Amount of players online",
                "§7  - Required permission",
                "§7  - Maximum executions",
                "§7  - Worlds"
        };
        ItemStack generalLimitsItem = Items.generateItem("§bGeneral limits", XMaterial.GOLD_INGOT, generalLimitsLore);
        ClickableItem clickableGeneralLimitsItem = ClickableItem.of(generalLimitsItem,
                                                                    e -> new GeneralLimitsMenu(task).INVENTORY.open(player));
        contents.set(1, 4, clickableGeneralLimitsItem);

        Callback conditionItemCallback = new Callback() {
            @Override
            public <T> void execute(T data) {
                INVENTORY.open(player);
            }
        };
        String[] conditionLore = new String[]{ "",
                "§7Conditions allow you to have an overall",
                "§7control of when your tasks should be executed",
                "§7or when not"
        };
        ItemStack conditionItem = Items.generateItem("§bCondition", XMaterial.COMPARATOR, conditionLore);
        ClickableItem clickableCondition = ClickableItem.of(conditionItem,
                                                            e -> new ConditionMenu(
                                                                    task.getCondition(),
                                                                    conditionItemCallback
                                                            ).INVENTORY.open(player));
        contents.set(1, 5, clickableCondition);

        boolean isActive = task.isActive();
        String[] activationLore = new String[]{ "",
                "§7Choose if this task should run or not",
                "",
                "§7Current: " + (isActive ? "§aActive" : "§cNot active")
        };
        contents.set(1, 6, ClickableItem.of(Items.getToggleItem("§bActivation status", activationLore, isActive), e -> {
            task.toggleActive();
            this.INVENTORY.open(player);
        }));

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(), e -> new AllTasksMenu().INVENTORY.open(player)));
    }

    public void update(Player player, InventoryContents contents) {
    }
}
