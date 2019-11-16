package me.playbosswar.com;

import me.playbosswar.com.AnvilGUI.Versions.AnvilGUI;
import org.bukkit.Bukkit;
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

        if(e.getView().getTitle().equalsIgnoreCase("All loaded timers")) {
            e.setCancelled(true);
            if(e.getSlot() == 53) {
                e.setCancelled(true);
                GUIHandler.generateGUI(p);
                return;
            }
            int id = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName());
            e.setCancelled(true);
            CommandTimer.getPlugin().getConfig().set("tasks." + id , null);
            CommandTimer.getPlugin().saveConfig();
            GUIHandler.listCommandsGUI(p);
        }
    }

    @EventHandler
    public void createCommandsGUIEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if (e.getView().getTitle().equalsIgnoreCase("Create a timer")) {
            e.setCancelled(true);
            if (GUIHandler.editing == 0) {
                CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".commands", "0");
                GUIHandler.editing = 1;
            }
            if (e.getSlot() == 0) {
                new AnvilGUI(CommandTimer.getPlugin(), p, "Insert command", (player, reply) -> {
                    List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("tasks." + GUIHandler.o + ".commands");
                    sl.add(reply);
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".commands", sl);
                    CommandTimer.getPlugin().saveConfig();
                    GUIHandler.createCommandsGUI(p, 1);
                    return "Saved";
                });
            }
            else if (e.getSlot() == 1) {
                if(e.getCurrentItem().getDurability() == 8) {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".onhour", true);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)10 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On hour");
                    onHour.setItemMeta(onHourMeta);

                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(1, onHour);
                    p.updateInventory();
                }
                else {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".onhour", false);
                    ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");

                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On hour");
                    onHour.setItemMeta(onHourMeta);

                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(1, onHour);
                    p.updateInventory();
                }
            }
            else if (e.getSlot() == 2) {
                if (e.getCurrentItem().getDurability() == 8) {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".onload", true);
                    final ItemStack onHour = new ItemStack(Material.INK_SACK, 1, (byte)10);
                    final ItemMeta onHourMeta = onHour.getItemMeta();
                    final ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");
                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On load");
                    onHour.setItemMeta(onHourMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(2, onHour);
                    p.updateInventory();
                }
                else {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".onload", false);
                    ItemStack onHour = new ItemStack(Material.INK_SACK, 1, (byte)8);
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");
                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On load");
                    onHour.setItemMeta(onHourMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(2, onHour);
                    p.updateInventory();
                }
            }
            else if (e.getSlot() == 3) {
                if (e.getCurrentItem().getDurability() == 8) {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".onday", true);
                    ItemStack onHour = new ItemStack(Material.INK_SACK, 1, (byte)10);
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");
                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On day");
                    onHour.setItemMeta(onHourMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(3, onHour);
                    p.updateInventory();
                }
                else {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".onday", false);
                    ItemStack onHour = new ItemStack(Material.INK_SACK, 1, (byte)8);
                    ItemMeta onHourMeta = onHour.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");
                    onHourMeta.setLore(lore);
                    onHourMeta.setDisplayName("On day");
                    onHour.setItemMeta(onHourMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(3, onHour);
                    p.updateInventory();
                }
            }
            else if (e.getSlot() == 4) {
                if (e.getCurrentItem().getDurability() == 8) {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".useRandom", true);
                    ItemStack useRandom = new ItemStack(Material.INK_SACK, 1, (byte)10);
                    ItemMeta useRandomMeta = useRandom.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");
                    useRandomMeta.setLore(lore);
                    useRandomMeta.setDisplayName("Use Random");
                    useRandom.setItemMeta(useRandomMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(4, useRandom);
                    p.updateInventory();
                }
                else {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".useRandom", false);
                    ItemStack useRandom = new ItemStack(Material.INK_SACK, 1, (byte)8);
                    ItemMeta useRandomMeta = useRandom.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");
                    useRandomMeta.setLore(lore);
                    useRandomMeta.setDisplayName("Use Random");
                    useRandom.setItemMeta(useRandomMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(4, useRandom);
                    p.updateInventory();
                }
            }
            else if (e.getSlot() == 5) {
                if (e.getCurrentItem().getDurability() == 8) {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".bungee", true);
                    ItemStack bungee = new ItemStack(Material.INK_SACK, 1, (byte)10);
                    ItemMeta bungeeMeta = bungee.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Enabled");
                    bungeeMeta.setLore(lore);
                    bungeeMeta.setDisplayName("Bungee");
                    bungee.setItemMeta(bungeeMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(5, bungee);
                    p.updateInventory();
                }
                else {
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".bungee", false);
                    ItemStack bungee = new ItemStack(Material.INK_SACK, 1, (byte)8);
                    ItemMeta bungeeMeta = bungee.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Disabled");
                    bungeeMeta.setLore(lore);
                    bungeeMeta.setDisplayName("Bungee");
                    bungee.setItemMeta(bungeeMeta);
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(5, bungee);
                    p.updateInventory();
                }
            }
            else if (e.getSlot() == 9) {
                if (e.getCurrentItem().getType().equals(Material.LEATHER_HELMET)) {
                    e.setCancelled(true);
                    ItemStack operator = new ItemStack(Material.IRON_HELMET, 1);
                    ItemMeta operatorMeta = operator.getItemMeta();
                    operatorMeta.setDisplayName("Executed by Operator");
                    operator.setItemMeta(operatorMeta);
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".gender", "operator");
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(9, operator);
                    p.updateInventory();
                }
                else if (e.getCurrentItem().getType().equals(Material.IRON_HELMET)) {
                    e.setCancelled(true);
                    ItemStack operator = new ItemStack(Material.DIAMOND_HELMET, 1);
                    ItemMeta operatorMeta = operator.getItemMeta();
                    operatorMeta.setDisplayName("Executed by Console");
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".gender", "console");
                    CommandTimer.getPlugin().saveConfig();
                    operator.setItemMeta(operatorMeta);
                    p.getOpenInventory().setItem(9, operator);
                    p.updateInventory();
                }
                else if (e.getCurrentItem().getType().equals(Material.DIAMOND_HELMET)) {
                    e.setCancelled(true);
                    ItemStack operator = new ItemStack(Material.LEATHER_HELMET, 1);
                    ItemMeta operatorMeta = operator.getItemMeta();
                    operatorMeta.setDisplayName("Executed by Player");
                    operator.setItemMeta(operatorMeta);
                    CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".gender", "player");
                    CommandTimer.getPlugin().saveConfig();
                    p.getOpenInventory().setItem(9, operator);
                    p.updateInventory();
                }
            }
            else if (e.getSlot() == 6) {
                e.setCancelled(true);
                GUIHandler.timeSetupGUI(p);
            }
            else if (e.getSlot() == 7) {
                e.setCancelled(true);
                GUIHandler.daySetupGUI(p);
            }
            else if (e.getSlot() == 8) {
                e.setCancelled(true);
                GUIHandler.secondSetupGUI(p);
            }
            else if (e.getSlot() == 10) {
                e.setCancelled(true);
                GUIHandler.randomSetupGUI(p);
            }
            else if (e.getSlot() == 11) {
                e.setCancelled(true);
                GUIHandler.permissionSetupGUI(p);
            }
            else if (e.getSlot() == 17) {
                CommandTimer.getPlugin().saveConfig();
                p.sendMessage(ChatColor.GOLD + "Saved in config");
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void daysCommandsGUIEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("tasks." + GUIHandler.o + ".days");
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        if(e.getView().getTitle().equals("Setup days")) {
            if(e.getCurrentItem().getType().equals(Material.INK_SACK)) {
                e.setCancelled(true);
                if (e.getSlot() == 10) { //Monday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Monday");
                        day.setItemMeta(dayMeta);

                        sl.add("MONDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(10, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Monday");
                        day.setItemMeta(dayMeta);

                        sl.remove("MONDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(10, day);
                        p.updateInventory();
                    }
                } else if (e.getSlot() == 11) { //Tuesday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Tuesday");
                        day.setItemMeta(dayMeta);

                        sl.add("TUESDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(11, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Tuesday");
                        day.setItemMeta(dayMeta);

                        sl.remove("TUESDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(11, day);
                        p.updateInventory();
                    }
                } else if (e.getSlot() == 12) { //Wednesday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Wednesday");
                        day.setItemMeta(dayMeta);

                        sl.add("WEDNESDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(12, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Wednesday");
                        day.setItemMeta(dayMeta);

                        sl.remove("WEDNESDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(12, day);
                        p.updateInventory();
                    }
                } else if (e.getSlot() == 13) { //Thursday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Thursday");
                        day.setItemMeta(dayMeta);

                        sl.add("THURSDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(13, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Thursday");
                        day.setItemMeta(dayMeta);

                        sl.remove("THURSDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(13, day);
                        p.updateInventory();
                    }
                } else if (e.getSlot() == 14) { //Friday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Friday");
                        day.setItemMeta(dayMeta);

                        sl.add("FRIDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(14, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Friday");
                        day.setItemMeta(dayMeta);

                        sl.remove("FRIDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(14, day);
                        p.updateInventory();
                    }
                } else if (e.getSlot() == 15) { //Saturday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Saturday");
                        day.setItemMeta(dayMeta);

                        sl.add("SATURDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(15, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Saturday");
                        day.setItemMeta(dayMeta);

                        sl.remove("SATURDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(15, day);
                        p.updateInventory();
                    }
                } else if (e.getSlot() == 16) { //Sunday
                    if (e.getCurrentItem().getDurability() == 8) {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 10);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Sunday");
                        day.setItemMeta(dayMeta);

                        sl.add("SUNDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(16, day);
                        p.updateInventory();
                    } else {
                        ItemStack day = new ItemStack(Material.INK_SACK, 1, (byte) 8);
                        ItemMeta dayMeta = day.getItemMeta();

                        dayMeta.setDisplayName("Sunday");
                        day.setItemMeta(dayMeta);

                        sl.remove("SUNDAY");
                        CommandTimer.getPlugin().getConfig().set("tasks." + GUIHandler.o + ".days", sl);
                        CommandTimer.getPlugin().saveConfig();

                        p.getOpenInventory().setItem(16, day);
                        p.updateInventory();
                    }
                }
            } else if(e.getSlot() == 26) {
                e.setCancelled(true);
                GUIHandler.createCommandsGUI(p, 1);
                return;
            }
        }
    }

    @EventHandler
    public void mainGUI(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("CommandTimer")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals((Object)Material.AIR)) {
                return;
            }
            if (e.getSlot() == 0) {
                GUIHandler.listCommandsGUI(p);
            }
            else if (e.getSlot() == 1) {
                GUIHandler.createCommandsGUI(p, 0);
            }
            else if (e.getSlot() == 8) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "commandtimer reload");
            }
        }
    }
}
