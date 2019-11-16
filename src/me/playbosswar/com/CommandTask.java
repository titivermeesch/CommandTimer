package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler.Gender;

import java.util.List;

public class CommandTask implements Runnable {
	public List<String> commands;
	public Gender gender;
	public String task;

	public CommandTask(List<String> commands, Gender gender, String task) {
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
