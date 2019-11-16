package me.playbosswar.com;

import java.util.ArrayList;
import java.util.List;

import me.playbosswar.com.AnvilGUI.Versions.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		ItemStack reloadCommands = new ItemStack(Material.NETHER_STAR);
		
		//Save ItemMeta of each ItemStack
        ItemMeta metaListCommands = listCommands.getItemMeta(); 
        ItemMeta metaCreateCommands = createCommands.getItemMeta();
        ItemMeta metaReloadCommands = reloadCommands.getItemMeta();
        
        //Set the displayname of each ItemMeta
        metaListCommands.setDisplayName(ChatColor.GOLD + "List all timers");
        metaCreateCommands.setDisplayName(ChatColor.GOLD + "Create a timer");
        metaReloadCommands.setDisplayName(ChatColor.GOLD + "Reload config");

        //Link the ItemMeta and ItemStack
        listCommands.setItemMeta(metaListCommands);
        createCommands.setItemMeta(metaCreateCommands);
        reloadCommands.setItemMeta(metaReloadCommands);
        
        //Set Items in inventory
        list.setItem(0, listCommands);
        list.setItem(1, createCommands);
        list.setItem(8, reloadCommands);
        
        //Open the just created inventory for Player p
        p.openInventory(list);
	}
	
	public static void listCommandsGUI(Player p) { //GUI that shows all the loaded commands
		if(CommandTimer.getPlugin().getConfig().getString("settings.tasks.1") != null) {
			Inventory allCommands = Bukkit.getServer().createInventory(p, 54, "All loaded timers");
			FileConfiguration c = CommandTimer.getPlugin().getConfig();
			for (final String path : CommandTimer.getPlugin().getConfig().getConfigurationSection("settings.tasks").getKeys(false)) { 
				//Generate an unique item for each command
				ItemStack genStack = new ItemStack(Material.WOOL);
		        ItemMeta genStackMeta = genStack.getItemMeta();
		        ArrayList<String> lore= new ArrayList<String>();

				lore.add(ChatColor.GOLD + "Commands : " + c.getStringList("settings.tasks." + path + ".commands"));
				lore.add(ChatColor.GOLD + "On hour : " + c.getBoolean("settings.tasks." + path + ".onhour"));
				lore.add(ChatColor.GOLD + "Time : " + c.getStringList("settings.tasks." + path + ".time"));
				lore.add(ChatColor.GOLD + "On load : " + c.getBoolean("settings.tasks." + path + ".onload"));
				lore.add(ChatColor.GOLD + "On day : " + c.getBoolean("settings.tasks." + path + ".onday"));
				lore.add(ChatColor.GOLD + "Days : " + c.getStringList("settings.tasks." + path + ".days"));
				lore.add(ChatColor.GOLD + "Seconds : " + c.getInt("settings.tasks." + path + ".seconds"));
				lore.add(ChatColor.GOLD + "UseRandom : " + c.getBoolean("settings.tasks." + path + ".useRandom"));
				lore.add(ChatColor.GOLD + "Random : " + c.getDouble("settings.tasks." + path + ".random"));
				lore.add(ChatColor.GOLD + "Gender : " + c.getString("settings.tasks." + path + ".gender"));
				lore.add(ChatColor.GOLD + "Bungee : " + c.getBoolean("settings.tasks." + path + ".bungee"));
				lore.add(ChatColor.GOLD + "Permission : " + c.getString("settings.tasks." + path + ".permissoin"));
				lore.add(ChatColor.RED + "Left-Click to delete this command");
		        
		        genStackMeta.setLore(lore);
		        genStackMeta.setDisplayName(path + "");
		        
		        genStack.setItemMeta(genStackMeta);
		        
		        allCommands.setItem(Integer.parseInt(path)-1, genStack); //Set item in GUI
			}
			ItemStack close = new ItemStack(Material.BARRIER);
	        ItemMeta closeMeta = close.getItemMeta();
	        
	        closeMeta.setDisplayName(ChatColor.GOLD + "Return to main menu");
	        close.setItemMeta(closeMeta);
	        allCommands.setItem(53, close);
			
			p.openInventory(allCommands);
		} else {
			p.sendMessage(ChatColor.RED + "You don't have any loaded commands");
		}
	}

	public static void createCommandsGUI(Player p, int recall) {
		if (recall != 1) {
			calcCommands();
		}

		Inventory createCommands = Bukkit.getServer().createInventory(p, 18, "Create a timer");
		ItemStack addCommand = new ItemStack(Material.COMMAND);
		ItemMeta addCommandMeta = addCommand.getItemMeta();
		if (CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + GUIHandler.o + ".onhour")) {
			final ItemStack onHour = new ItemStack(Material.INK_SACK, 1, (byte)10);
			final ItemMeta onHourMeta = onHour.getItemMeta();
			final ArrayList<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN + "Enabled");
			onHourMeta.setLore(lore);
			onHourMeta.setDisplayName("On hour");
			onHour.setItemMeta(onHourMeta);
			createCommands.setItem(1, onHour);
		}
		else {
			ItemStack onHour = new ItemStack(Material.INK_SACK, 1, (byte)8);
			ItemMeta onHourMeta = onHour.getItemMeta();
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.RED + "Disabled");
			onHourMeta.setLore((List)lore);
			onHourMeta.setDisplayName("On hour");
			onHour.setItemMeta(onHourMeta);
			createCommands.setItem(1, onHour);
		}
		if (CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + GUIHandler.o + ".onload")) {
			ItemStack onLoad = new ItemStack(Material.INK_SACK, 1, (byte)10);
			ItemMeta onLoadMeta = onLoad.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<>();
			lore2.add(ChatColor.GREEN + "Enabled");
			onLoadMeta.setLore(lore2);
			onLoadMeta.setDisplayName("On load");
			onLoad.setItemMeta(onLoadMeta);
			createCommands.setItem(2, onLoad);
		}
		else {
			ItemStack onLoad = new ItemStack(Material.INK_SACK, 1, (byte)8);
			ItemMeta onLoadMeta = onLoad.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.RED + "Disabled");
			onLoadMeta.setLore(lore2);
			onLoadMeta.setDisplayName("On load");
			onLoad.setItemMeta(onLoadMeta);
			createCommands.setItem(2, onLoad);
		}
		if (CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + GUIHandler.o + ".onday")) {
			ItemStack onDay = new ItemStack(Material.INK_SACK, 1, (byte)10);
			ItemMeta onDayMeta = onDay.getItemMeta();
			ArrayList<String> lore3 = new ArrayList<>();
			lore3.add(ChatColor.GREEN + "Enabled");
			onDayMeta.setLore(lore3);
			onDayMeta.setDisplayName("On Day");
			onDay.setItemMeta(onDayMeta);
			createCommands.setItem(3, onDay);
		}
		else {
			ItemStack onDay = new ItemStack(Material.INK_SACK, 1, (byte)8);
			ItemMeta onDayMeta = onDay.getItemMeta();
			ArrayList<String> lore3 = new ArrayList<>();
			lore3.add(ChatColor.RED + "Disabled");
			onDayMeta.setLore(lore3);
			onDayMeta.setDisplayName("On Day");
			onDay.setItemMeta(onDayMeta);
			createCommands.setItem(3, onDay);
		}
		if (CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + GUIHandler.o + ".useRandom")) {
			ItemStack onRandom = new ItemStack(Material.INK_SACK, 1, (byte)10);
			ItemMeta onRandomMeta = onRandom.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<>();
			lore2.add(ChatColor.GREEN + "Enabled");
			onRandomMeta.setLore(lore2);
			onRandomMeta.setDisplayName("Use Random");
			onRandom.setItemMeta(onRandomMeta);
			createCommands.setItem(4, onRandom);
		}
		else {
			ItemStack onRandom = new ItemStack(Material.INK_SACK, 1, (byte)8);
			ItemMeta onRandomMeta = onRandom.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<>();
			lore2.add(ChatColor.RED + "Disabled");
			onRandomMeta.setLore(lore2);
			onRandomMeta.setDisplayName("Use Random");
			onRandom.setItemMeta(onRandomMeta);
			createCommands.setItem(4, onRandom);
		}
		if (CommandTimer.getPlugin().getConfig().getBoolean("settings.tasks." + GUIHandler.o + ".bungee")) {
			ItemStack bungee = new ItemStack(Material.INK_SACK, 1, (byte)10);
			ItemMeta bungeeMeta = bungee.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<>();
			lore2.add(ChatColor.GREEN + "Enabled");
			bungeeMeta.setLore(lore2);
			bungeeMeta.setDisplayName("Bungee");
			bungee.setItemMeta(bungeeMeta);
			createCommands.setItem(5, bungee);
		}
		else {
			ItemStack bungee = new ItemStack(Material.INK_SACK, 1, (byte)8);
			ItemMeta bungeeMeta = bungee.getItemMeta();
			ArrayList<String> lore2 = new ArrayList<>();
			lore2.add(ChatColor.RED + "Disabled");
			bungeeMeta.setLore(lore2);
			bungeeMeta.setDisplayName("Bungee");
			bungee.setItemMeta(bungeeMeta);
			createCommands.setItem(5, bungee);
		}

		String gender = CommandTimer.getPlugin().getConfig().getString("settings.tasks." + GUIHandler.o + ".gender");
		if (recall == 0) {
			CommandTimer.getPlugin().getConfig().set("settings.tasks." + GUIHandler.o + ".gender", "console");
			gender = "console";
		}

		if (gender.equals("player")) {
			ItemStack player = new ItemStack(Material.LEATHER_HELMET, 1);
			ItemMeta playerMeta = player.getItemMeta();
			playerMeta.setDisplayName("Executed by Player");
			player.setItemMeta(playerMeta);
			createCommands.setItem(9, player);
		}
		else if (gender.equals("operator")) {
			ItemStack player = new ItemStack(Material.IRON_HELMET, 1);
			ItemMeta playerMeta = player.getItemMeta();
			playerMeta.setDisplayName("Executed by Operator");
			player.setItemMeta(playerMeta);
			createCommands.setItem(9, player);
		}
		else if (gender.equals("console")) {
			ItemStack player = new ItemStack(Material.DIAMOND_HELMET, 1);
			ItemMeta playerMeta = player.getItemMeta();
			playerMeta.setDisplayName("Executed by Console");
			player.setItemMeta(playerMeta);
			createCommands.setItem(9, player);
		}
		ItemStack time = new ItemStack(Material.WATCH);
		ItemStack seconds = new ItemStack(Material.COMPASS);
		ItemStack days = new ItemStack(Material.PAPER);
		ItemStack random = new ItemStack(Material.WATCH);
		ItemStack permission = new ItemStack(Material.ANVIL);
		ItemStack save = new ItemStack(Material.NETHER_STAR);
		ItemMeta timeMeta = time.getItemMeta();
		ItemMeta daysMeta = days.getItemMeta();
		ItemMeta secondsMeta = seconds.getItemMeta();
		ItemMeta randomMeta = random.getItemMeta();
		ItemMeta permissionMeta = permission.getItemMeta();
		ItemMeta saveMeta = save.getItemMeta();
		addCommandMeta.setDisplayName("Add a command");
		timeMeta.setDisplayName("Time");
		daysMeta.setDisplayName("Days");
		secondsMeta.setDisplayName("Seconds");
		randomMeta.setDisplayName("Random");
		permissionMeta.setDisplayName("Permission");
		saveMeta.setDisplayName("Save");
		addCommand.setItemMeta(addCommandMeta);
		time.setItemMeta(timeMeta);
		days.setItemMeta(daysMeta);
		seconds.setItemMeta(secondsMeta);
		random.setItemMeta(randomMeta);
		permission.setItemMeta(permissionMeta);
		save.setItemMeta(saveMeta);
		createCommands.setItem(0, addCommand);
		createCommands.setItem(6, time);
		createCommands.setItem(7, days);
		createCommands.setItem(8, seconds);
		createCommands.setItem(10, random);
		createCommands.setItem(11, permission);
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
		new AnvilGUI(CommandTimer.getPlugin(), p, "09:00:00", (player, reply) -> {
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

    public static void randomSetupGUI(Player p) { //Opens the GUI to setup the random value
        new AnvilGUI(CommandTimer.getPlugin(), p, "Insert random (0.1 - 1)", (player, reply) -> {
            double r = Double.parseDouble(reply);
            List<String> sl = CommandTimer.getPlugin().getConfig().getStringList("settings.tasks." + o + ".time");
            CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".random", r);
            CommandTimer.getPlugin().saveConfig();
            createCommandsGUI(p, 1);
            return "Saved";
        });
    }

	public static void permissionSetupGUI(Player p) {
        new AnvilGUI(CommandTimer.getPlugin(), p, "command.use.permission", (player, reply) -> {
            CommandTimer.getPlugin().getConfig().set("settings.tasks." + o + ".permission", reply);
            CommandTimer.getPlugin().saveConfig();
            createCommandsGUI(p, 1);
            return "Saved";
        });
    }
}