package me.playbosswar.com;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.playbosswar.com.AnvilGUI.Versions.AnvilGUI;

public class GUIHandler implements Listener {
	
	public static int o = 1;
	public static int editing = 0;
	
	public static void calcCommands() { //Calculate how many commands there are loaded and what the id of the next command should be
		if(CommandTimer.getPlugin().getConfig().getString("settings.tasks.1") == null) {
			CommandTimer.getPlugin().getConfig().set("settings.tasks.1", 0);
		}
		for (final String path : CommandTimer.getPlugin().getConfig().getConfigurationSection("settings.tasks").getKeys(false)) {
			o++;
		}
	}
	
	public static void generateGUI(Player p) {
		Inventory list = Bukkit.getServer().createInventory(p, 9, "CommandTimer");
		
		//Create all ItemStacks for each item
		ItemStack listCommands = new ItemStack(Material.PAPER);
		ItemStack createCommands = new ItemStack(Material.COMMAND);
		ItemStack removeCommands = new ItemStack(Material.BARRIER);
		ItemStack editCommands = new ItemStack(Material.BOOK_AND_QUILL);
		ItemStack reloadCommands = new ItemStack(Material.NETHER_STAR);
		
		//Save ItemMeta of each ItemStack
        ItemMeta metaListCommands = listCommands.getItemMeta(); 
        ItemMeta metaCreateCommands = createCommands.getItemMeta();
        ItemMeta metaRemoveCommands = removeCommands.getItemMeta();
        ItemMeta metaEditCommands = editCommands.getItemMeta();
        ItemMeta metaReloadCommands = reloadCommands.getItemMeta();
        
        //Set the displayname of each ItemMeta
        metaListCommands.setDisplayName("§6List all timers");
        metaCreateCommands.setDisplayName("§6Create a timer");
        metaRemoveCommands.setDisplayName("§6Delete a timer");
        metaEditCommands.setDisplayName("§6Edit a timer");
        metaReloadCommands.setDisplayName("§6Reload config");

        //Link the ItemMeta and ItemStack
        listCommands.setItemMeta(metaListCommands);
        createCommands.setItemMeta(metaCreateCommands);
        removeCommands.setItemMeta(metaRemoveCommands);
        editCommands.setItemMeta(metaEditCommands);
        reloadCommands.setItemMeta(metaReloadCommands);
        
        //Set Items in inventory
        list.setItem(0, listCommands);
        list.setItem(1, createCommands);
        list.setItem(2, removeCommands);
        list.setItem(3, editCommands);
        list.setItem(8, reloadCommands);
        
        //Open the just created inventory for Player p
        p.openInventory(list);
	}
	
	public static void listCommandsGUI(Player p) { //GUI that shows all the loaded commands
		if(CommandTimer.getPlugin().getConfig().getString("settings.tasks.1") != null) {
			Inventory allCommands = Bukkit.getServer().createInventory(p, 54, "All loaded timers");
			for (final String path : CommandTimer.getPlugin().getConfig().getConfigurationSection("settings.tasks").getKeys(false)) { 
				//Generate an unique item for each command
				ItemStack genStack = new ItemStack(Material.WOOL);
		        ItemMeta genStackMeta = genStack.getItemMeta();
		        ArrayList<String> lore= new ArrayList<String>();
		        
		        lore.add("§6Commands : " + CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + path + ".commands"));
		        lore.add("§6On hour : " + CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + path + ".onhour"));
		        lore.add("§6Time : " + CommandTimer.getPlugin().getConfig().getString("settings.tasks." + path + ".time"));
		        lore.add("§6On load : " + CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + path + ".onload"));
		        lore.add("§6On day : " + CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + path + ".onday"));
		        lore.add("§6Days : " + CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + path + ".days"));
		        lore.add("§6Seconds : " + CommandTimer.getPlugin().getConfig().getInt("settings.tasks." + path + ".seconds"));
		        
		        genStackMeta.setLore(lore);
		        genStackMeta.setDisplayName(path + "");
		        
		        genStack.setItemMeta(genStackMeta);
		        
		        allCommands.setItem(Integer.parseInt(path)-1, genStack); //Set item in GUI
			}
			ItemStack close = new ItemStack(Material.BARRIER);
	        ItemMeta closeMeta = close.getItemMeta();
	        
	        closeMeta.setDisplayName("§6Return to main menu");
	        close.setItemMeta(closeMeta);
	        allCommands.setItem(53, close);
			
			p.openInventory(allCommands);
		} else {
			p.sendMessage("§cYou don't have any loaded commands");
		}
	}
		
	
	public static void deleteCommandsGUI(Player p) { //Open GUI that lists all commands and allows you to click on one of them to delete it
		if(CommandTimer.getPlugin().getConfig().getString("settings.tasks.1") != null) {
		Inventory deleteCommands = Bukkit.getServer().createInventory(p, 54, "Delete a timer");
		for (final String path : CommandTimer.getPlugin().getConfig().getConfigurationSection("settings.tasks").getKeys(false)) {
			ItemStack genStack = new ItemStack(Material.WOOL);
	        ItemMeta genStackMeta = genStack.getItemMeta();
	        ArrayList<String> lore= new ArrayList<String>();
	        
	        lore.add("§6Commands : " + CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + path + ".commands"));
	        lore.add("§6On hour : " + CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + path + ".onhour"));
	        lore.add("§6Time : " + CommandTimer.getPlugin().getConfig().getString("settings.tasks." + path + ".time"));
	        lore.add("§6On load : " + CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + path + ".onload"));
	        lore.add("§6On day : " + CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + path + ".onday"));
	        lore.add("§6Days : " + CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + path + ".days"));
	        lore.add("§6Seconds : " + CommandTimer.getPlugin().getConfig().getInt("settings.tasks." + path + ".seconds"));
	        
	        genStackMeta.setLore(lore);
	        genStackMeta.setDisplayName(path + "");
	        
	        genStack.setItemMeta(genStackMeta);
	        
	        deleteCommands.setItem(Integer.parseInt(path)-1, genStack);
		}
		ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        
        closeMeta.setDisplayName("§6Return to main menu");
        close.setItemMeta(closeMeta);
        deleteCommands.setItem(53, close);
		
		p.openInventory(deleteCommands);
		} else {
			p.sendMessage("§cYou don't have any loaded commands");
		}
	}
	

	public static void createCommandsGUI(Player p, int recall) { //Open the general GUI to create commands
		if(recall != 1) { //Calculate how many commands there are, if it's a recall you are editing the same command so you don't need to recalculate that
			calcCommands();
		}
		
		Inventory createCommands = Bukkit.getServer().createInventory(p, 18, "Create a timer");
		
		ItemStack addCommand = new ItemStack(Material.COMMAND); //This item will always there, so no if else statement needed
		ItemMeta addCommandMeta = addCommand.getItemMeta();
		
		if(CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + o + ".onhour")) { //Generate items corresponding on the boolean statement in config
			ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta onHourMeta = onHour.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§aEnabled");
			onHourMeta.setLore(lore);
			onHourMeta.setDisplayName("On hour");
			onHour.setItemMeta(onHourMeta);
			createCommands.setItem(1, onHour);
		} else {
			ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta onHourMeta = onHour.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§cDisabled");
			onHourMeta.setLore(lore);
			onHourMeta.setDisplayName("On hour");
			onHour.setItemMeta(onHourMeta);
			createCommands.setItem(1, onHour);
		}
		
		if(CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + o + ".onload")) {
			ItemStack onLoad = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta onLoadMeta = onLoad.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add("§aEnabled");
			onLoadMeta.setLore(lore2);
			onLoadMeta.setDisplayName("On load");
			onLoad.setItemMeta(onLoadMeta);
			createCommands.setItem(2, onLoad);
		} else {
			ItemStack onLoad = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta onLoadMeta = onLoad.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add("§cDisabled");
			onLoadMeta.setLore(lore2);
			onLoadMeta.setDisplayName("On load");
			onLoad.setItemMeta(onLoadMeta);
			createCommands.setItem(2, onLoad);
		}
		
		if(CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + o + ".onday")) {
			ItemStack onDay = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta onDayMeta = onDay.getItemMeta();
			ArrayList<String> lore3 = new ArrayList<String>();
			lore3.add("§aEnabled");
			onDayMeta.setLore(lore3);
			onDayMeta.setDisplayName("On Day");
			onDay.setItemMeta(onDayMeta);
			createCommands.setItem(3, onDay);
		} else {
			ItemStack onDay = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta onDayMeta = onDay.getItemMeta();
			ArrayList<String> lore3 = new ArrayList<String>();
			lore3.add("§cDisabled");
			onDayMeta.setLore(lore3);
			onDayMeta.setDisplayName("On Day");
			onDay.setItemMeta(onDayMeta);
			createCommands.setItem(3, onDay);
		}
		
		ItemStack time = new ItemStack(Material.WATCH);
		ItemStack seconds = new ItemStack(Material.COMPASS);
		ItemStack days = new ItemStack(Material.PAPER);
		ItemStack save = new ItemStack(Material.NETHER_STAR);
		
		ItemMeta timeMeta = time.getItemMeta();
		ItemMeta daysMeta = days.getItemMeta();
		ItemMeta secondsMeta = seconds.getItemMeta();
		ItemMeta saveMeta = save.getItemMeta();
		
		addCommandMeta.setDisplayName("Add a command");
		timeMeta.setDisplayName("Time");
		daysMeta.setDisplayName("Days");
		secondsMeta.setDisplayName("Seconds");
		saveMeta.setDisplayName("Save");
		
		addCommand.setItemMeta(addCommandMeta);
		time.setItemMeta(timeMeta);
		days.setItemMeta(daysMeta);
		seconds.setItemMeta(secondsMeta);
		save.setItemMeta(saveMeta);
		
		createCommands.setItem(0, addCommand);
		createCommands.setItem(4, time);
		createCommands.setItem(5, days);
		createCommands.setItem(6, seconds);
		createCommands.setItem(17, save);
		
		p.openInventory(createCommands);
	}
	
	public static void daySetupGUI(Player p) { //Opens GUI to setup the days
		Inventory setupDays = Bukkit.getServer().createInventory(p, 27, "Setup days");
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("MONDAY")) {
			ItemStack days1 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days1Meta = days1.getItemMeta();
			days1Meta.setDisplayName("Monday");
			days1.setItemMeta(days1Meta);
			setupDays.setItem(10, days1);
		} else {
			ItemStack days1 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days1Meta = days1.getItemMeta();
			days1Meta.setDisplayName("Monday");
			days1.setItemMeta(days1Meta);
			setupDays.setItem(10, days1);
		}
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("TUESDAY")) {
			ItemStack days2 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days2Meta = days2.getItemMeta();
			days2Meta.setDisplayName("Tuesday");
			days2.setItemMeta(days2Meta);
			setupDays.setItem(11, days2);
		} else {
			ItemStack days2 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days2Meta = days2.getItemMeta();
			days2Meta.setDisplayName("Tuesday");
			days2.setItemMeta(days2Meta);
			setupDays.setItem(11, days2);
		}
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("WEDNESDAY")) {
			ItemStack days3 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days3Meta = days3.getItemMeta();
			days3Meta.setDisplayName("Wednesday");
			days3.setItemMeta(days3Meta);
			setupDays.setItem(12, days3);
		} else {
			ItemStack days3 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days3Meta = days3.getItemMeta();
			days3Meta.setDisplayName("Wednesday");
			days3.setItemMeta(days3Meta);
			setupDays.setItem(12, days3);
		}
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("THURSDAY")) {
			ItemStack days4 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days4Meta = days4.getItemMeta();
			days4Meta.setDisplayName("Thursday");
			days4.setItemMeta(days4Meta);
			setupDays.setItem(13, days4);
		} else {
			ItemStack days4 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days4Meta = days4.getItemMeta();
			days4Meta.setDisplayName("Thursday");
			days4.setItemMeta(days4Meta);
			setupDays.setItem(13, days4);
		}
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("FRIDAY")) {
			ItemStack days5 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days5Meta = days5.getItemMeta();
			days5Meta.setDisplayName("Friday");
			days5.setItemMeta(days5Meta);
			setupDays.setItem(14, days5);
		} else {
			ItemStack days5 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days5Meta = days5.getItemMeta();
			days5Meta.setDisplayName("Friday");
			days5.setItemMeta(days5Meta);
			setupDays.setItem(14, days5);
		}
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("SATURDAY")) {
			ItemStack days6 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days6Meta = days6.getItemMeta();
			days6Meta.setDisplayName("Saturday");
			days6.setItemMeta(days6Meta);
			setupDays.setItem(15, days6);
		} else {
			ItemStack days6 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days6Meta = days6.getItemMeta();
			days6Meta.setDisplayName("Saturday");
			days6.setItemMeta(days6Meta);
			setupDays.setItem(15, days6);
		}
		
		if(CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days").contains("SUNDAY")) {
			ItemStack days7 = new ItemStack( Material.INK_SACK, 1, (byte)10 );
			ItemMeta days7Meta = days7.getItemMeta();
			days7Meta.setDisplayName("Sunday");
			days7.setItemMeta(days7Meta);
			setupDays.setItem(16, days7);
		} else {
			ItemStack days7 = new ItemStack( Material.INK_SACK, 1, (byte)8 );
			ItemMeta days7Meta = days7.getItemMeta();
			days7Meta.setDisplayName("Sunday");
			days7.setItemMeta(days7Meta);
			setupDays.setItem(16, days7);
		}
		
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta backMeta = back.getItemMeta();
		
		backMeta.setDisplayName("Return");
		back.setItemMeta(backMeta);
		setupDays.setItem(26, back);
		
		p.openInventory(setupDays);
	}
	
	public static void timeSetupGUI(Player p) { //Opens the GUI to setup the time
		new AnvilGUI(CommandTimer.getPlugin(), p, "Insert time", (player, reply) -> {
			String msg = reply.toString();
			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".time");
			sl.add(msg);
			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".time", sl);
			CommandTimer.getPlugin().saveConfig();
	        createCommandsGUI(p, 1);
	        return "Saved";
		});
	}
	
	public static void secondSetupGUI(Player p) { //GUI to setup the seconds
		new AnvilGUI(CommandTimer.getPlugin(), p, "Insert seconds", (player, reply) -> {
			int sec = Integer.parseInt(reply);
			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".seconds", sec);
			CommandTimer.getPlugin().saveConfig();
	        createCommandsGUI(p, 1);
	        return "Saved";
		});
	}
}