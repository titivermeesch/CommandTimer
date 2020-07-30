package me.playbosswar.com.utils;

public enum CommandParam {
    NAME("name"),
    COMMAND("command"),
    GENDER("gender"),
    SECONDS("seconds"),
    TIME("time"),
    MINECRAFT_TIME("minecraft_time"),
    RANDOM("random"),
    EXECUTE_PER_USER("execute_per_user"),
    WORLD("world"),
    DAY("day"),
    EXECUTION_LIMIT("exection_limit"),
    TIMES_EXECUTED("times_executed");


    final String key;

    private CommandParam(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
