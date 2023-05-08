package me.playbosswar.com.enums;

public enum Gender {
    /**
     * Commands are executed once in console
     */
    CONSOLE,
    /**
     * Commands are executed for each player,
     * providing the player scope to the command
     */
    CONSOLE_PER_USER,
    /**
     * Commands are executed by the player directly
     */
    PLAYER,
    /**
     * Commands are executed by the player as if they were OP
     */
    OPERATOR,
    /**
     * Commands are executed for each player that ever joined
     */
    CONSOLE_PER_USER_OFFLINE;
}
