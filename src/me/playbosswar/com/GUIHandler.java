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
	
	int o = 1;
	int editing = 0;
	int v1 = 12;
	int v2 = 30;
	int v3 = 30;
	public static ArrayList<String> commands;
	
	public void calcCommands() { //Calculate how many commands there are loaded and what the id of the next command should be
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
	
	public void listCommandsGUI(Player p) { //GUI that shows all the loaded commands
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
		
	
	public void deleteCommandsGUI(Player p) { //Open GUI that lists all commands and allows you to click on one of them to delete it
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
	

	public void createCommandsGUI(Player p, int recall) { //Open the general GUI to create commands
		
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
	
	public void daySetupGUI(Player p) { //Opens GUI to setup the days
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
	
	public void timeSetupGUI(Player p) { //Opens the GUI to setup the time
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
	
	public void secondSetupGUI(Player p) { //GUI to setup the seconds
		new AnvilGUI(CommandTimer.getPlugin(), p, "Insert seconds", (player, reply) -> {
			int sec = Integer.parseInt(reply);
			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".seconds", sec);
			CommandTimer.getPlugin().saveConfig();
	        createCommandsGUI(p, 1);
	        return "Saved";
		});
	}
	
	@EventHandler
    public void inventoryClick(InventoryClickEvent e) { //Register all inventory clicks 
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase("CommandTimer")) { //Commands linked with the General menu
        	e.setCancelled(true);
        
        	if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
        		return;
        	}
        
        	if(e.getSlot() == 0) { //List the commands
        		listCommandsGUI(p);
        		return;
        	} else if(e.getSlot() == 1) { //Open create commands
        		createCommandsGUI(p, 0);
        		return;
        	} else if(e.getSlot() == 2) { //Open delete commands
        		deleteCommandsGUI(p);
        		return;
        	} else if(e.getSlot() == 3) {
        		p.sendMessage("This isn't implemented yet");
        	} else if(e.getSlot() == 8) { //Reload button
        		CommandTimer.getPlugin().reloadConfig();
    			Tools.reloadTaks();
    			p.sendMessage("§6CommandTimer reloaded");
    			Tools.closeAllInventories();
        	}
        } else if(e.getInventory().getTitle().equalsIgnoreCase("All loaded timers")) { //Commands linked with the list GUI
        	if(e.getSlot() == 53) {
        		e.setCancelled(true);
        		generateGUI(p);
        		return;
        	} else if(e.getCurrentItem().getType().equals(Material.WOOL)) {
        		e.setCancelled(true);
        		return;
        	}
        } else if(e.getInventory().getTitle().equalsIgnoreCase("Create a timer")) { //Commands linked with the Create GUI
        	if(e.getSlot() == 0) {
        		if (editing == 0) {
            		CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".commands", "0");
            		editing = 1;
            	}
            	new AnvilGUI(CommandTimer.getPlugin(), p, "Insert command", (player, reply) -> {
            			String msg = reply.toString();
            			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".commands");
            			sl.add(msg);
            			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".commands", sl);
            			CommandTimer.getPlugin().saveConfig();
            	        createCommandsGUI(p, 1);
            	        return "Saved";
            	});
        	} else if(e.getSlot() == 1) {
        		if (editing == 0) {
            		CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".commands", "0"); //Enable editing mode
            		editing = 1;
            	}
        		if(e.getCurrentItem().getDurability() == 8) { //onhour
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".onhour", true);
        			CommandTimer.getPlugin().saveConfig();
        		} else if(e.getCurrentItem().getDurability() == 10) {
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".onhour", false);
        			CommandTimer.getPlugin().saveConfig();
        		}
        	} else if(e.getSlot() == 2) {
        		if (editing == 0) {
            		CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".commands", "0");
            		editing = 1;
            	}
        		if(e.getCurrentItem().getDurability() == 8) { //onload
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".onload", true);
        			CommandTimer.getPlugin().saveConfig();
        		} else if(e.getCurrentItem().getDurability() == 10) {
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".onload", false);
        			CommandTimer.getPlugin().saveConfig();
        		}
        	} else if(e.getSlot() == 3) { //onday
        		if (editing == 0) {
            		CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".commands", "0");
            		editing = 1;
            	}
        		if(e.getCurrentItem().getDurability() == 8) { //onday
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".onday", true);
        			CommandTimer.getPlugin().saveConfig();
        		} else if(e.getCurrentItem().getDurability() == 10) {
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".onday", false);
        			CommandTimer.getPlugin().saveConfig();
        		}
        	}
        	if(e.getSlot() == 1) { //onhour
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta onHourMeta = onHour.getItemMeta();
        			ArrayList<String> lore = new ArrayList<String>();
        			lore.add("§aEnabled");
        			
        			onHourMeta.setLore(lore);
        			onHourMeta.setDisplayName("On hour");
        			onHour.setItemMeta(onHourMeta);
        			
        			p.getOpenInventory().setItem(1, onHour);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta onHourMeta = onHour.getItemMeta();
        			ArrayList<String> lore = new ArrayList<String>();
        			lore.add("§cDisabled");
        			
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
        			ArrayList<String> lore = new ArrayList<String>();
        			lore.add("§aEnabled");
        			
        			onHourMeta.setLore(lore);
        			onHourMeta.setDisplayName("On load");
        			onHour.setItemMeta(onHourMeta);
        			
        			p.getOpenInventory().setItem(2, onHour);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta onHourMeta = onHour.getItemMeta();
        			ArrayList<String> lore = new ArrayList<String>();
        			lore.add("§cDisabled");
        			
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
        			ArrayList<String> lore = new ArrayList<String>();
        			lore.add("§aEnabled");
        			
        			onHourMeta.setLore(lore);
        			onHourMeta.setDisplayName("On day");
        			onHour.setItemMeta(onHourMeta);
        			
        			p.getOpenInventory().setItem(3, onHour);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack onHour = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta onHourMeta = onHour.getItemMeta();
        			ArrayList<String> lore = new ArrayList<String>();
        			lore.add("§cDisabled");
        			
        			onHourMeta.setLore(lore);
        			onHourMeta.setDisplayName("On day");
        			onHour.setItemMeta(onHourMeta);
        			
        			p.getOpenInventory().setItem(3, onHour);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 4) { //time
        		e.setCancelled(true);
        		timeSetupGUI(p);
        	} else if(e.getSlot() == 5) { //days
        		e.setCancelled(true);
        		daySetupGUI(p);
        	} else if(e.getSlot() == 6) { //seconds
        		e.setCancelled(true);
        		secondSetupGUI(p);
        	} else if(e.getSlot() == 17) { //Close
        		CommandTimer.getPlugin().saveConfig();
        		p.closeInventory();
        	}
        	
        } else if(e.getInventory().getTitle().equalsIgnoreCase("Delete a timer")) { //Everything with the delete timer GUI
        	if(e.getSlot() == 53) {
        		e.setCancelled(true);
        		generateGUI(p);
        		return;
        	}
        	int id = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName());
        	e.setCancelled(true);
        	CommandTimer.getPlugin().getConfig().set("settings.tasks." + id , null);
    		CommandTimer.getPlugin().saveConfig();
    		deleteCommandsGUI(p);
    		return;
        } else if(e.getInventory().getTitle().equalsIgnoreCase("Setup time")) { //Everything with the time setup GUI
        	switch(e.getSlot()) {
        		case 2:
        			e.setCancelled(true);
        			p.getOpenInventory().getItem(11).getItemMeta().getDisplayName();
        			ItemStack stick = new ItemStack(Material.WATCH);
        			ItemMeta stickMeta = stick.getItemMeta();
        			if(v1++ == 24) { //hours
        				v1 = 0;
        				stickMeta.setDisplayName(v1 +"");
        			} else {
        				v1++;
        				stickMeta.setDisplayName(v1 +"");
        			}
        			stick.setItemMeta(stickMeta);
        			
        			p.getOpenInventory().setItem(11, stick);
        			p.updateInventory();
        			break;
        		case 4:
        			e.setCancelled(true);
        			p.getOpenInventory().getItem(13).getItemMeta().getDisplayName();
        			ItemStack stick2 = new ItemStack(Material.WATCH);
        			ItemMeta stickMeta2 = stick2.getItemMeta();
        			if(v2++ == 60) { //Minutes
        				v2 = 0;
        				stickMeta2.setDisplayName(v2 + "");
        			} else {
        				v2++;
        				stickMeta2.setDisplayName(v2 +"");
        			}
        			stick2.setItemMeta(stickMeta2);
        			
        			p.getOpenInventory().setItem(13, stick2);
        			p.updateInventory();
        			break;
        		case 6:
        			e.setCancelled(true);
        			p.getOpenInventory().getItem(15).getItemMeta().getDisplayName();
        			ItemStack stick3 = new ItemStack(Material.WATCH);
        			ItemMeta stickMeta3 = stick3.getItemMeta();
        			if(v3++ == 60) { //Seconds
        				v3 = 0;
        				stickMeta3.setDisplayName(v3 +"");
        			} else {
        				v1++;
        				stickMeta3.setDisplayName(v3 + "");
        			}
        			stick3.setItemMeta(stickMeta3);
        			
        			p.getOpenInventory().setItem(15, stick3);
        			p.updateInventory();
        			break;
        		case 20:
        			e.setCancelled(true);
        			int v4 = Integer.parseInt(p.getOpenInventory().getItem(11).getItemMeta().getDisplayName());
        			p.getOpenInventory().getItem(11).getItemMeta().getDisplayName();
        			ItemStack stick4 = new ItemStack(Material.WATCH);
        			ItemMeta stickMeta4 = stick4.getItemMeta();
        			if(v4-1 == -1) {
        				stickMeta4.setDisplayName("23");
        			} else {
        			stickMeta4.setDisplayName(v4-1 + "");
        			}
        			stick4.setItemMeta(stickMeta4);
        			
        			p.getOpenInventory().setItem(11, stick4);
        			p.updateInventory();
        			break;
        		case 22:
        			e.setCancelled(true);
        			int v5 = Integer.parseInt(p.getOpenInventory().getItem(13).getItemMeta().getDisplayName());
        			p.getOpenInventory().getItem(13).getItemMeta().getDisplayName();
        			ItemStack stick5 = new ItemStack(Material.WATCH);
        			ItemMeta stickMeta5 = stick5.getItemMeta();
        			if(v5-1 == -1) {
        				stickMeta5.setDisplayName("59");
        			} else {
        			stickMeta5.setDisplayName(v5-1 + "");
        			}
        			stick5.setItemMeta(stickMeta5);
        			
        			p.getOpenInventory().setItem(13, stick5);
        			p.updateInventory();
        			break;
        		case 24:
        			e.setCancelled(true);
        			int v6 = Integer.parseInt(p.getOpenInventory().getItem(15).getItemMeta().getDisplayName());
        			p.getOpenInventory().getItem(15).getItemMeta().getDisplayName();
        			ItemStack stick6 = new ItemStack(Material.WATCH);
        			ItemMeta stickMeta6 = stick6.getItemMeta();
        			if(v6-1 == -1) {
        				stickMeta6.setDisplayName("59");
        			} else {
        			stickMeta6.setDisplayName(v6-1 + "");
        			}
        			stick6.setItemMeta(stickMeta6);
        			
        			p.getOpenInventory().setItem(15, stick6);
        			p.updateInventory();
        			break;
        		case 26:
        			e.setCancelled(true);
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".time", e.getInventory().getItem(11).getItemMeta().getDisplayName() + ":" + e.getInventory().getItem(13).getItemMeta().getDisplayName() + ":" + e.getInventory().getItem(15).getItemMeta().getDisplayName());
        			CommandTimer.getPlugin().saveConfig();
        			createCommandsGUI(p, 1);
            		return;
        			
        		default:
        			e.setCancelled(true);
        			break;
        	}
        } else if(e.getInventory().getTitle().equals("Setup days")) { //Everything with the days GUI
        	if(e.getSlot() == 10) { //Monday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Monday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("MONDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(10, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Monday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("MONDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(10, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 11) { //Tuesday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Tuesday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("TUESDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(11, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Tuesday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("TUESDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(11, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 12) { //Wednesday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Wednesday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("WEDNESDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(12, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Wednesday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("WEDNESDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(12, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 13) { //Thursday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Thursday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("THURSDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(13, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Thursday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("THURSDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(13, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 14) { //Friday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Friday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("FRIDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(14, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Friday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("FRIDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(14, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 15) { //Saturday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Saturday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("SATURDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(15, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Saturday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("SATURDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(15, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 16) { //Sunday
        		if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 8) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)10 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Sunday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.add("SUNDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(16, day);
        			p.updateInventory();
        			return;
        		} else if(e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == 10) {
        			e.setCancelled(true);
        			ItemStack day = new ItemStack( Material.INK_SACK, 1, (byte)8 );
        			ItemMeta dayMeta = day.getItemMeta();
        			
        			dayMeta.setDisplayName("Sunday");
        			day.setItemMeta(dayMeta);
        			
        			List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".days");
        			sl.remove("SUNDAY");
        			CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".days", sl);
        			CommandTimer.getPlugin().saveConfig();
        			
        			p.getOpenInventory().setItem(16, day);
        			p.updateInventory();
        		}
        	} else if(e.getSlot() == 26) {
        		e.setCancelled(true);
        		createCommandsGUI(p, 1);
        	}
        	
        }
	}	

}
