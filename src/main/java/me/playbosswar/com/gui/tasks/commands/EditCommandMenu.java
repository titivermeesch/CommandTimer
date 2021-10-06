package me.playbosswar.com.gui.tasks.commands;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.WorldWeather;
import me.playbosswar.com.gui.tasks.weather.WeatherMenu;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.utils.Items;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

        String[] editCommandLore = new String[]{ "",
                "§7Edit the actual command",
                "",
                "§7Current: §e" + taskCommand.getCommand()
        };
        ItemStack editCommandItem = Items.generateItem("§bChange command", XMaterial.PAPER, editCommandLore);
        ClickableItem clickableCommandItem = ClickableItem.of(editCommandItem, e -> new EditCommandNameMenu(player, taskCommand));
        contents.set(1, 1, clickableCommandItem);

        String[] genderLore = new String[]{ "",
                "§7Genders are one of the core concepts of",
                "§7CommandTimer. They allow you to specify",
                "§7how your task is executed.",
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
                "",
                "§7Current: §e" + taskCommand.getGender().toString()
        };
        ItemStack genderItem = Items.generateItem("§bGender", XMaterial.CHAINMAIL_HELMET, genderLore);
        ClickableItem clickableGenderItem = ClickableItem.of(genderItem, e -> {
            taskCommand.toggleGender();
            this.INVENTORY.open(player);
        });
        contents.set(1, 2, clickableGenderItem);

        List<String> weatherLore = new ArrayList<>();
        weatherLore.add("");
        weatherLore.add("§7Set a limit for the weather condition on");
        weatherLore.add("§7this command. This limit will only work");
        weatherLore.add("§7when the gender is §cNOT§7 set to §eCONSOLE");
        weatherLore.add("");

        boolean clear = taskCommand.getWeatherConditions().contains(WorldWeather.CLEAR);
        boolean rain = taskCommand.getWeatherConditions().contains(WorldWeather.RAINING);
        boolean thunder = taskCommand.getWeatherConditions().contains(WorldWeather.THUNDER);

        if (clear || rain || thunder) {
            weatherLore.add("§7Current: §e" + StringUtils.join(taskCommand.getWeatherConditions(), ", "));
        }
        String[] weatherLoreArray = weatherLore.toArray(new String[0]);
        ItemStack weatherItem = Items.generateItem("§bWeather conditions", XMaterial.WATER_BUCKET, weatherLoreArray);
        ClickableItem clickableWeatherItem = ClickableItem.of(weatherItem,
                                                              e -> new WeatherMenu(taskCommand).INVENTORY.open(player));
        contents.set(1, 3, clickableWeatherItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(),
                                            e -> new AllCommandsMenu(taskCommand.getTask()).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
