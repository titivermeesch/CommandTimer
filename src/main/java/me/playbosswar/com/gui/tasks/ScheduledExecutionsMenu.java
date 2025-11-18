package me.playbosswar.com.gui.tasks;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.AdHocCommand;
import me.playbosswar.com.tasks.ScheduledTask;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import me.playbosswar.com.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduledExecutionsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FilterType filterType = FilterType.ALL;
    
    private enum FilterType {
        ALL,
        TASKS_ONLY,
        ADHOC_ONLY
    }

    public ScheduledExecutionsMenu() {
        INVENTORY = SmartInventory.builder()
                .id("scheduled-executions")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.SCHEDULED_EXECUTIONS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();

        pagination.setItems(getAllScheduledExecutionItems(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        ItemStack refreshItem = Items.generateItem(LanguageKey.REFRESH_BUTTON_TITLE, XMaterial.REPEATER,
                languageManager.getList(LanguageKey.REFRESH_BUTTON_LORE).toArray(new String[]{}));
        contents.set(0, 8, ClickableItem.of(refreshItem, e -> {
            player.closeInventory();
            ScheduledExecutionsMenu newMenu = new ScheduledExecutionsMenu();
            newMenu.filterType = this.filterType;
            newMenu.INVENTORY.open(player);
        }));

        String allLabel = languageManager.get(LanguageKey.FILTER_ALL);
        String tasksLabel = languageManager.get(LanguageKey.FILTER_TASKS_ONLY);
        String adhocLabel = languageManager.get(LanguageKey.FILTER_ADHOC_ONLY);
        
        String[] filterLore = new String[]{
            filterType == FilterType.ALL ? "§f» " + allLabel + " §f«" : "§8  " + allLabel,
            filterType == FilterType.TASKS_ONLY ? "§f» " + tasksLabel + " §f«" : "§8  " + tasksLabel,
            filterType == FilterType.ADHOC_ONLY ? "§f» " + adhocLabel + " §f«" : "§8  " + adhocLabel,
            "",
            "§7Click to cycle"
        };
        
        ItemStack filterItem = Items.generateItem(
                LanguageKey.FILTER_BUTTON_TITLE, 
                XMaterial.HOPPER, 
                filterLore);
        contents.set(0, 0, ClickableItem.of(filterItem, e -> {
            filterType = filterType == FilterType.ALL ? FilterType.TASKS_ONLY : 
                        filterType == FilterType.TASKS_ONLY ? FilterType.ADHOC_ONLY : 
                        FilterType.ALL;
            pagination.setItems(getAllScheduledExecutionItems(player));
            INVENTORY.open(player, 0);
        }));

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> new MainMenu().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem[] getAllScheduledExecutionItems(Player p) {
        final FilterType currentFilter = this.filterType;
        List<ScheduledTask> scheduledTasks = new ArrayList<>();
        if (currentFilter == FilterType.ALL || currentFilter == FilterType.TASKS_ONLY) {
            scheduledTasks = CommandTimerPlugin.getInstance()
                    .getTasksManager()
                    .getScheduledTasks()
                    .stream()
                    .filter(scheduledTask -> scheduledTask.getDate().toInstant().toEpochMilli() >= System.currentTimeMillis())
                    .collect(Collectors.toList());
        }

        List<AdHocCommand> adHocCommands = new ArrayList<>();
        if (currentFilter == FilterType.ALL || currentFilter == FilterType.ADHOC_ONLY) {
            adHocCommands = CommandTimerPlugin.getInstance()
                    .getAdHocCommandsManager()
                    .getPendingCommands()
                    .stream()
                    .filter(cmd -> cmd.getScheduledTime().toInstant().toEpochMilli() >= System.currentTimeMillis())
                    .collect(Collectors.toList());
        }

        List<ExecutionItem> allItems = new ArrayList<>();
        
        for (ScheduledTask scheduledTask : scheduledTasks) {
            allItems.add(new ExecutionItem(scheduledTask.getDate(), scheduledTask, null));
        }
        
        for (AdHocCommand adHocCommand : adHocCommands) {
            allItems.add(new ExecutionItem(adHocCommand.getScheduledTime(), null, adHocCommand));
        }
        
        allItems.sort(Comparator.comparing(item -> item.date));

        ClickableItem[] items = new ClickableItem[allItems.size()];

        for(int i = 0; i < items.length; i++) {
            ExecutionItem executionItem = allItems.get(i);
            ZonedDateTime date = executionItem.date;
            String formattedDate = date.format(DATE_FORMATTER);
            
            if (executionItem.scheduledTask != null) {
                String[] lore = new String[]{
                        "§7Task: §e" + executionItem.scheduledTask.getTask().getName(),
                        "§7Scheduled: §e" + formattedDate,
                        "",
                        languageManager.get(LanguageKey.LEFT_CLICK_EDIT)
                };

                ItemStack item = Items.generateItem("§b" + executionItem.scheduledTask.getTask().getName(), XMaterial.CLOCK, lore);
                items[i] = ClickableItem.of(item, e -> {
                    Callback<?> callback = data -> INVENTORY.open(p);
                    new EditTaskMenu(executionItem.scheduledTask.getTask(), callback).INVENTORY.open(p);
                });
            } else if (executionItem.adHocCommand != null) {
                String[] lore = new String[]{
                        "§7Command: §e" + executionItem.adHocCommand.getCommand(),
                        "§7Gender: §e" + executionItem.adHocCommand.getGender().name(),
                        "§7Scheduled: §e" + formattedDate,
                        "",
                        languageManager.get(LanguageKey.RIGHT_CLICK_DELETE)
                };

                ItemStack item = Items.generateItem("§bAd-Hoc Command", XMaterial.COMMAND_BLOCK, lore);
                final AdHocCommand adHocCmd = executionItem.adHocCommand;
                items[i] = ClickableItem.of(item, e -> {
                    if (e.getClick().equals(ClickType.RIGHT)) {
                        try {
                            CommandTimerPlugin.getInstance().getAdHocCommandsManager().removeCommand(adHocCmd);
                            p.closeInventory();
                            ScheduledExecutionsMenu newMenu = new ScheduledExecutionsMenu();
                            newMenu.filterType = currentFilter;
                            newMenu.INVENTORY.open(p);
                        } catch (IOException ioException) {
                            Messages.sendFailedIO(p);
                            ioException.printStackTrace();
                        }
                    }
                });
            }
        }

        return items;
    }
    
    private static class ExecutionItem {
        final ZonedDateTime date;
        final ScheduledTask scheduledTask;
        final AdHocCommand adHocCommand;
        
        ExecutionItem(ZonedDateTime date, ScheduledTask scheduledTask, AdHocCommand adHocCommand) {
            this.date = date;
            this.scheduledTask = scheduledTask;
            this.adHocCommand = adHocCommand;
        }
    }
}

