package me.playbosswar.com.gui.events;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.events.EventCondition;
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.api.events.EventSimpleCondition;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.tasks.general.TextInputConversationPrompt;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.ArrayUtils;
import me.playbosswar.com.utils.Items;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

// Menu that shows all the possible values
public class ConfigureEventValuesMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final ConditionExtension extension;
    private final EventCondition condition;
    private final Task task;
    private final EventExtension eventExtension;

    public ConfigureEventValuesMenu(Task task, ConditionExtension extension, EventExtension eventExtension,
                                    EventCondition condition) {
        this.extension = extension;
        this.task = task;
        this.condition = condition;
        this.eventExtension = eventExtension;

        INVENTORY = SmartInventory.builder()
                .id("configure-event-values")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.CONFIGURE_EVENT_VALUES_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();
        List<ClickableItem> items = eventExtension.getReturnedValues().stream().map(v -> {
            boolean isActiveValue = condition.getSimpleCondition().getFieldName().equals(v.getName());
            String[] lore;
            if(isActiveValue) {
                lore = new String[]{"", "&7Value: &e" + condition.getSimpleCondition().getValue().toString(),
                        "&7Condition: &e" + condition.getSimpleCondition().getCompare().name()};
            } else {
                lore = new String[]{"", "&7CLICK TO ACTIVATE"};
            }
            ItemStack item = Items.generateItem("&b" + v.getLabel(), isActiveValue ? XMaterial.LIME_DYE :
                    XMaterial.GRAY_DYE, lore);
            return ClickableItem.of(item, e -> {
                if(e.isLeftClick()) {
                    if(v.getType() == Double.class) {
                        ConversationFactory conversationFactory =
                                new ConversationFactory(CommandTimerPlugin.getPlugin())
                                        .withModality(true)
                                        .withFirstPrompt(new TextInputConversationPrompt(data -> {
                                            double text = Double.parseDouble(data);
                                            EventSimpleCondition<Double> simpleCondition =
                                                    new EventSimpleCondition<>(task, v.getName(), text,
                                                            ConditionCompare.EQUAL);
                                            condition.setSimpleCondition(simpleCondition);
                                            new ConfigureEventValuesMenu(task, extension, eventExtension, condition).INVENTORY.open(player);
                                        }));
                        conversationFactory.buildConversation(player).begin();
                        player.closeInventory();
                        return;
                    }

                    if(v.getType() == Integer.class) {
                        ConversationFactory conversationFactory =
                                new ConversationFactory(CommandTimerPlugin.getPlugin())
                                        .withModality(true)
                                        .withFirstPrompt(new TextInputConversationPrompt(data -> {
                                            int text = Integer.parseInt(data);
                                            EventSimpleCondition<Integer> simpleCondition =
                                                    new EventSimpleCondition<>(task, v.getName(), text,
                                                            ConditionCompare.EQUAL);
                                            condition.setSimpleCondition(simpleCondition);
                                            new ConfigureEventValuesMenu(task, extension, eventExtension, condition).INVENTORY.open(player);
                                        }));
                        conversationFactory.buildConversation(player).begin();
                        player.closeInventory();
                        return;
                    }

                    if(v.getType() == String.class) {
                        ConversationFactory conversationFactory =
                                new ConversationFactory(CommandTimerPlugin.getPlugin())
                                        .withModality(true)
                                        .withFirstPrompt(new TextInputConversationPrompt(text -> {
                                            EventSimpleCondition<String> simpleCondition =
                                                    new EventSimpleCondition<>(task, v.getName(), text,
                                                            ConditionCompare.EQUAL);
                                            condition.setSimpleCondition(simpleCondition);
                                            new ConfigureEventValuesMenu(task, extension, eventExtension, condition).INVENTORY.open(player);
                                        }));
                        conversationFactory.buildConversation(player).begin();
                        player.closeInventory();
                    }
                }

                if(e.isRightClick()) {
                    // TODO: These conditions may be different for strings
                    ConditionCompare nextConditionCompare = ArrayUtils
                            .getNextValueInArray(ConditionCompare.values(),
                                    condition.getSimpleCondition().getCompare());
                    condition.getSimpleCondition().setCompare(nextConditionCompare);
                    new ConfigureEventValuesMenu(task, extension, eventExtension, condition).INVENTORY.open(player);
                }
            });
        }).collect(Collectors.toList());
        pagination.setItems(items.toArray(new ClickableItem[0]));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(),
                e -> new ConfigureEventMenu(task, extension, eventExtension, condition).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
