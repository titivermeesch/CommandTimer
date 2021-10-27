package me.playbosswar.com.enums;

public enum CommandExecutionMode {
    /**
     * All commands in the task are executed at once
     */
    ALL,
    /**
     * Commands are executed one by one on each task in order
     */
    ORDERED,
    /**
     * Commands are executed one by one on each task in random order
     */
    RANDOM,
}
