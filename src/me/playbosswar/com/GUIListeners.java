package me.playbosswar.com;

import me.playbosswar.com.AnvilGUI.Versions.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIListeners implements Listener {

    @EventHandler
    public void listCommandsGUIEvents(InventoryClickEvent e) { //List commands GUI
        Player p = (Player) e.getWhoClicked();
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        if (e.getInventory().getTitle().equalsIgnoreCase("All loaded timers")) {
            e.setCancelled(true);
            if (e.getSlot() == 53) {
                e.setCancelled(true);
                GUIHandler.generateGUI(p);
            } else if (e.getCurrentItem().getType().equals(Material.WOOL)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void createCommandsGUIEvent(InventoryClickEvent e) { //Create commands GUI
        Player p = (Player) e.getWhoClicked();
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        if(e.getInventory().getTitle().equalsIgnoreCase("Create a timer")) {
            e.setCancelled(true);
            if(e.getSlot() == 0) {
                if (GUIHandler.editing == 0) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".commands", "0");
                    GUIHandler.editing = 1;
                }
                new AnvilGUI(CommandTimer.getPlugin(), p, "Insert command", (player, reply) -> { //Command insertion GUI
                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".commands");
                    sl.add(reply);
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".commands", sl);
                    CommandTimer.getPlugin().saveConfig();
                    GUIHandler.createCommandsGUI(p, 1);
                    return "Saved";
                });

            } else if(e.getSlot() == 1) {
                if (GUIHandler.editing == 0) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".commands", "0"); //Enable editing mode
                    GUIHandler.editing = 1;
                }
                if(e.getCurrentItem().getDurability() == 8) { //onhour
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".onhour", true);
                    CommandTimer.getPlugin().saveConfig();
                } else if(e.getCurrentItem().getDurability() == 10) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".onhour", false);
                    CommandTimer.getPlugin().saveConfig();
                }
            } else if(e.getSlot() == 2) {
                if (GUIHandler.editing == 0) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".commands", "0");
                    GUIHandler.editing = 1;
                }
                if(e.getCurrentItem().getDurability() == 8) { //onload
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".onload", true);
                    CommandTimer.getPlugin().saveConfig();
                } else if(e.getCurrentItem().getDurability() == 10) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".onload", false);
                    CommandTimer.getPlugin().saveConfig();
                }
            } else if(e.getSlot() == 3) { //onday
                if (GUIHandler.editing == 0) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".commands", "0");
                    GUIHandler.editing = 1;
                }
                if(e.getCurrentItem().getDurability() == 8) { //onday
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".onday", true);
                    CommandTimer.getPlugin().saveConfig();
                } else if(e.getCurrentItem().getDurability() == 10) {
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".onday", false);
                    CommandTimer.getPlugin().saveConfig();
                }
            }
            if(e.getSlot() == 1) { //onhour
                if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)10 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On hour");
                    onHour.setItemMeta(onHourMeta);

                    p.getOpenInventory().setItem(1, onHour);
                    p.updateInventory();
                } else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On hour");
                    onHour.setItemMeta(onHourMeta);

                    p.getOpenInventory().setItem(1, onHour);
                    p.updateInventory();
                }
            } else if(e.getSlot() == 2) { //onload
                if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)10 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On load");
                    onHour.setItemMeta(onHourMeta);

                    p.getOpenInventory().setItem(2, onHour);
                    p.updateInventory();
                } else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On load");
                    onHour.setItemMeta(onHourMeta);

                    p.getOpenInventory().setItem(2, onHour);
                    p.updateInventory();
                }
            } else if(e.getSlot() == 3) { //onday
                if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)10 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On day");
                    onHour.setItemMeta(onHourMeta);

                    p.getOpenInventory().setItem(3, onHour);
                    p.updateInventory();
                } else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On day");
                    onHour.setItemMeta(onHourMeta);

                    p.getOpenInventory().setItem(3, onHour);
                    p.updateInventory();
                }
            } else if(e.getSlot() == 7) {
                if(e.getCurrentItem().getType().equals(Material.LEATHER_HELMET)) {
                    e.setCancelled(true);
                    ItemStack operator = new ItemStack( Material.IRON_HELMET,1);
                    ItemMeta operatorMeta = operator.getItemMeta();

                    operatorMeta.setDisplayName("Executed by Operator");
                    operator.setItemMeta(operatorMeta);
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".gender", "operator");
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(7, operator);
                    p.updateInventory();
                } else if(e.getCurrentItem().getType().equals(Material.IRON_HELMET)) {
                    e.setCancelled(true);
                    ItemStack operator = new ItemStack( Material.DIAMOND_HELMET,1);
                    ItemMeta operatorMeta = operator.getItemMeta();

                    operatorMeta.setDisplayName("Executed by Console");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".gender", "console");
                    CommandTimer.getPlugin().saveConfig();
                    operator.setItemMeta(operatorMeta);

                    p.getOpenInventory().setItem(7, operator);
                    p.updateInventory();
                } else if(e.getCurrentItem().getType().equals(Material.DIAMOND_HELMET)) {
                    e.setCancelled(true);
                    ItemStack operator = new ItemStack( Material.LEATHER_HELMET,1);
                    ItemMeta operatorMeta = operator.getItemMeta();

                    operatorMeta.setDisplayName("Executed by Player");
                    operator.setItemMeta(operatorMeta);
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".gender", "player");
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(7, operator);
                    p.updateInventory();
                }

            } else if(e.getSlot() == 4) { //time
                e.setCancelled(true);
                GUIHandler.timeSetupGUI(p);
            } else if(e.getSlot() == 5) { //days
                e.setCancelled(true);
                GUIHandler.daySetupGUI(p);
            } else if(e.getSlot() == 6) { //seconds
                e.setCancelled(true);
                GUIHandler.secondSetupGUI(p);
            } else if(e.getSlot() == 17) { //Close
                CommandTimer.getPlugin().saveConfig();
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void deleteCommandsGUIEvent(InventoryClickEvent e) { //Delete commands GUI
        Player p = (Player) e.getWhoClicked();
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        if(e.getInventory().getTitle().equalsIgnoreCase("Delete a timer")) {
            e.setCancelled(true);
            if(e.getSlot() == 53) {
                e.setCancelled(true);
                GUIHandler.generateGUI(p);
                return;
            }
            int id = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName());
            e.setCancelled(true);
            CommandTimer.getPlugin().getConfig().set("settings.tasks." + id , null);
            CommandTimer.getPlugin().saveConfig();
            GUIHandler.deleteCommandsGUI(p);
        }
    }

    @EventHandler
    public void daysCommandsGUIEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        if (e.getInventory().getTitle().equals("Setup days")) { //Everything with the days GUI
            e.setCancelled(true);
            if (e.getSlot() == 10) { //Monday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Monday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("MONDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(10, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Monday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("MONDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(10, day);
                    p.updateInventory();
                }
            } else if (e.getSlot() == 11) { //Tuesday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Tuesday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("TUESDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(11, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Tuesday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("TUESDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(11, day);
                    p.updateInventory();
                }
            } else if (e.getSlot() == 12) { //Wednesday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Wednesday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("WEDNESDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(12, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Wednesday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("WEDNESDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(12, day);
                    p.updateInventory();
                }
            } else if (e.getSlot() == 13) { //Thursday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Thursday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("THURSDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(13, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Thursday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("THURSDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(13, day);
                    p.updateInventory();
                }
            } else if (e.getSlot() == 14) { //Friday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Friday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("FRIDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(14, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Friday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("FRIDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(14, day);
                    p.updateInventory();
                }
            } else if (e.getSlot() == 15) { //Saturday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Saturday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("SATURDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(15, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Saturday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("SATURDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(15, day);
                    p.updateInventory();
                }
            } else if (e.getSlot() == 16) { //Sunday
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Sunday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.add("SUNDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(16, day);
                    p.updateInventory();
                } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
                    e.setCancelled(true);
                    ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                    ItemMeta dayMeta = day.getItemMeta();

                    dayMeta.setDisplayName("Sunday");
                    day.setItemMeta(dayMeta);

                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + GUIHandler.o + ".days");
                    sl.remove("SUNDAY");
                    CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".days", sl);
                    CommandTimer.getPlugin().saveConfig();

                    p.getOpenInventory().setItem(16, day);
                    p.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void mainGUI(InventoryClickEvent e) { //Main GUI Events
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase("CommandTimer")) {
            e.setCancelled(true);

            if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }

            if (e.getSlot() == 0) { //List the commands
                GUIHandler.listCommandsGUI(p);
            } else if (e.getSlot() == 1) { //Open create commands
                GUIHandler.createCommandsGUI(p, 0);
            } else if (e.getSlot() == 2) { //Open delete commands
                GUIHandler.deleteCommandsGUI(p);
            } else if (e.getSlot() == 3) {
                p.sendMessage("This isn't implemented yet");
            } else if (e.getSlot() == 8) { //Reload button
                CommandTimer.getPlugin().reloadConfig();
                Tools.reloadTaks();
                p.sendMessage(ChatColor.GOLD + "CommandTimer reloaded");
                Tools.closeAllInventories();
            }
        }
    }
}
