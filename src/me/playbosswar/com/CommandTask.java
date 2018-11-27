package me.playbosswar.com;

import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandTask extends BukkitRunnable {
	public List<String> commands;
	public String gender;
	public String task;

	public CommandTask(List<String> commands, String gender, String task) {
		this.commands = commands;
		this.gender = gender;
		this.task = task;
	}

	public void run() {
		for (String command : this.commands) {
		    Tools.executeCommand(task, command, gender);
		}
	}
}
