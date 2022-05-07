## 6.2.3 - 07/05/2022

- Enhance error logging when file deletion fails
- Update the way command runners work to prevent double command executions

## 6.2.2 - 09/04/2022

- Setup Github Actions for auto building plugin files
- Add better error logging during parsing and command execution
- Add auto healing support when to tasks coming from older versions

## 6.2 - 12/02/2022

- Take into account day interval when calculating when command should be executed
- Add new command execution mode: INTERVAL

## 6.1.3 - 21/01/2022

- Correctly save task executions when plugin restarts

## 6.1.2 - 13/01/2022

- Fix bug when server has a lot of plugins and task is based on world time

## 6.1.1

- Compile against 1.18
- fix lastExecuted task bug

# 6.1

- Add permissions commandtimer.manage, execute, activate/deactivate and toggle
- Show server time in main GUI

# 6.0.3

- Fix interval not working after bugfix in 6.0.2

# 6.0.2

- Take into account interval when working with time ranges
- Add /cmt time command

# 6.0.1

CommandTimer would not take into account all fixes times that are configured. This has now been fixed.

# 6.0

- Support for 1.8-1.17
- Fix all the reported bugs from the past few months
- Introduce a brand new extensions system to enhance the different limits to prevent commands from running.

# 5.2

- Compiled against 1.16.4
- Fix bug with minecraft time
- Fix bug with normal time
- Updated to latest PAPI build
- Add secondsFormat and nextExecutionFormat to show correct information

# 5.1

- Have seconds and minutes as option instead of only seconds
- Debug mode is toggleable
- Fix double execution when gender is operator
- Don't save execution count between server restarts
- Fix reload issues
- Potential fix for wrong placeholder values

# 5.0.4

- Add missing "Worlds button"  in menu

# 5.0.3

- Fix crash on 1.8 servers

# 5.0.2

- Gender operator would execute command in console instead of player

# 5.0.1

- Fix timers directory creation error
- Fix issue with time related timers

# 5.0

- Intuitive chat menu to handle all configuration
- PaperSpigot support
- Server restart support
- Placeholders
- Instantly execute a timer
- Pick random command from list
- Several bug fixes
- Huge performance improvements

# 4.3.1

- Fix useMinecraftTime
- Potential fix for Paper

# 4.3

- Add missing commandtimer.use
- Fix delayed commands when using time
- Fix multiple command executions when using time
- Change default command message
- Allow time ranges like : [10:00:00-14:00:00]
- Add minPlayers and maxPlayers
- Configure main command alias (default /ct)

# 4.2

- Add support for in-game time (along with real life time)
- Fix issue with default config
- Fix issue with time commands being executed multiple times

#4.1

- Several bug fixes
- Add ct as command alias to commandtimer
- Check configuration file validity before running plugin
- Add a limiter to the commands

#4.0.1

- Fix default configuration file issue

# 4.0

- Fix permission system when gender is console
- Fix days limiter not affecting command execution
- Global code improvements
- PlaceholderAPI support
- Plugin will not crash with missing information in config
- Command execution with world limit