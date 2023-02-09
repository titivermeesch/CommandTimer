---
sidebar_position: 1
---
# Commands

## Execution Modes

Execution modes are set on the entire [task](../misc/jargon#task) and not on individual commands

- `ALL`: Execute all the commands at the same time
- `ORDERED`: Execute the commands one by one. This is directly linked to the [interval](intervals.md) set for this task. If you interval is set to 10 seconds for example and you have 3 commands configured, it will run the 1 command, wait 10 seconds, then execute the second command, wait 10 seconds, then execute the third command, wait 10 seconds, and execute the first one again.
- `RANDOM`: Execute a random command for each execution
- `INTERVAL`: This one is very similar to `ORDERED` but has a seconds parameter configurable in the menu directly. This seconds parameter is not linked to the general [task](../misc/jargon#task) [interval](intervals.md). Updating this value in the GUI can be achieved by right clicking on the execution mode item.

## Command Settings

- Reset last executed command index: This option allows you to start the command execution from the beginning. This only applies for execution modes `ORDERED` and `INTERVAL` which keep track of which command has already been executed before.

## Genders

Genders define for whom the command will be executed.

- `CONSOLE`: The command will be executed once in the console. There is no user data available here. If that is what you want, have a look at `CONSOLE_PER_USER`.
- `CONSOLE_PER_USER`: Execute the command in the console, but for each player. This means that if you have 20 players online, CommandTimer will execute the command 20 times (once for each player). [Placeholders](../misc/placeholders) like `%player_name%` are available here (under the condition that PAPI is installed).
- `OPERATOR`: Execute the command for each player as if they were server operators (OP). This will execute the command for each player that is online and placeholders like `%player_name%` are available here. For the player perspective, it will look like the player executed the command himself.
- `PLAYER`: Execute the command for each player. If this player does not have enough permissions to execute this command, the player will receive an error message from the command itself. Placeholders like `%player_name%` are available here. If you don't want the player to see a message related to permissions, you should use the [conditions engine](conditions.md) to check if the player has the permission before executing it.