package me.playbosswar.com;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandTask extends BukkitRunnable {
	public List<String> commands;

	public CommandTask(List<String> commands) {
		this.commands = commands;
	}

	public void run() {
		for (String command : this.commands) {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
		}
	}
}
