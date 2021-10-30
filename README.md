# CommandTimer

![bStats Players](https://img.shields.io/bstats/players/9657?style=for-the-badge&color=green)
![bStats Servers](https://img.shields.io/bstats/servers/9657?style=for-the-badge)
![Spiget Version](https://img.shields.io/spiget/version/24141?label=published%20version&style=for-the-badge)
![Spiget Download Size](https://img.shields.io/spiget/download-size/24141?style=for-the-badge)

CommandTimer is a Spigot plugin that allows you to schedule different commands at specific interval/times depending on
various conditions.

## Installation

- You can build the plugin with Gradle (shadowJar) or [download the latest version](https://www.spigotmc.org/resources/command-timer.24141/)
- Put the jar file in your plugins folder and reload/restart
- You will now see a CommandTimer folder
    - `timers` hold all the configuration files for your specific setup
    - `extensions` has all the external extensions that add more functions to CommandTimer
  

## Commands

- `/cmt` will open the main menu where everything can be managed
- `/cmt help` will open the help menu
- `/cmt activate <task>` will enable/activate the specified task (equals the activate button in GUI)
- `/cmt deactivate <task>` will disable/deactivate the specified task (equals the deactivate button in GUI)
- `/cmt execute <task>` will directly execute a task without taking into account timers and intervals
- `/cmt time` will show the Minecraft time of the world you are in

## Permissions

`commandtimer.use` gives access to every feature of the plugin

## Extensions

Extensions come in form of a jar file. These extensions will hook into the CommandTimer API and will bring more
conditions to use in your timers.

## For Developers

