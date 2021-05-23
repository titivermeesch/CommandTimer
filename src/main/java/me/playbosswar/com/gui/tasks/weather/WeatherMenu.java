package me.playbosswar.com.gui.tasks.weather;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.enums.WorldWeather;
import me.playbosswar.com.gui.tasks.commands.EditCommandMenu;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WeatherMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final TaskCommand taskCommand;
    private final List<WorldWeather> enabledWeatherConditions;

    public WeatherMenu(TaskCommand taskCommand) {
        this.taskCommand = taskCommand;
        this.enabledWeatherConditions = taskCommand.getWeatherConditions();
        INVENTORY = SmartInventory.builder()
                .id("command-weather")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lWeather conditinos")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ItemStack rainingItem = Items.getToggleItem("§bRaining", new String[]{}, enabledWeatherConditions.contains(WorldWeather.RAINING));
        ClickableItem clickableRainingItem = ClickableItem.of(rainingItem, e -> {
            taskCommand.toggleWeatherCondition(WorldWeather.RAINING);
            this.INVENTORY.open(player);
        });
        contents.set(1, 1, clickableRainingItem);

        ItemStack thunderItem = Items.getToggleItem("§bThunder", new String[]{}, enabledWeatherConditions.contains(WorldWeather.THUNDER));
        ClickableItem clickableThunderItem = ClickableItem.of(thunderItem, e -> {
            taskCommand.toggleWeatherCondition(WorldWeather.THUNDER);
            this.INVENTORY.open(player);
        });
        contents.set(1, 2, clickableThunderItem);

        ItemStack clearItem = Items.getToggleItem("§bClear", new String[]{}, enabledWeatherConditions.contains(WorldWeather.CLEAR));
        ClickableItem clickableClearItem = ClickableItem.of(clearItem, e -> {
            taskCommand.toggleWeatherCondition(WorldWeather.CLEAR);
            this.INVENTORY.open(player);
        });
        contents.set(1, 3, clickableClearItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(), e -> new EditCommandMenu(taskCommand).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
