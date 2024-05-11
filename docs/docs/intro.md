---
sidebar_position: 1
---

# Getting Started

Follow this tutorial to create your first scheduled command.

CommandTimer works with tasks. Tasks define a group of commands that follow the same execution schema. An execution
schema is a collection of settings and conditions that define when certain commands get executed.

## Creating a task

Everything starts with the `/cmt` command. This will open a GUI where everything can be managed from.

Clicking the paper will create a fresh task for you.

## Commands

| Command                  | Description                                                                                       | Permission                                          |
|--------------------------|---------------------------------------------------------------------------------------------------|-----------------------------------------------------|
| `/cmt`                   | Open main CommandTimer menu where everything is managed                                           | `commandtimer.manage`                               |
| `/cmt help`              | Get a list of all possible commands                                                               | Any CommandTimer permission                         |
| `/cmt time`              | Get the current world time the player is in                                                       | Any CommandTimer permission                         |
| `/cmt activate <task>`   | Enable the specified task                                                                         | `commandtimer.activate` or `commandtimer.toggle`    |
| `/cmt deactivate <task>` | Disable the specified task                                                                        | `commandtimer.deactivate`  or `commandtimer.toggle` |
| `/cmt execute <task>`    | Instantly execute the specified task                                                              | `commandtimer.execute`                              |
| `/cmt reload`            | Reload the plugin. Extensions will not be reloaded. **It is not recommended to use this command** | `commandtimer.manage`                               |

## Configuration file

The global configuration file allows you to change the global behaviour of CommandTimer:

- `timeonload`: Show the current server time in the console when the plugin loads
- `debug`: Enable debug mode. This will print additional information to the console
- `showUpdateMessage`: Show a message in the chat when a plugin update is available
- `timezoneOverwrite`: Overwrite the timezone of the server (**This feature does not work right now**)
- `timezoneOverwriteValue`: The timezone to overwrite the server timezone with (**This feature does not work right now**)
- `language`: The language to use for the plugin. You can add more languages under the `languages` folder
- `disablePapiPlaceholderWarnings`: Disable the warning message that appears when a PAPI placeholder is not used correctly


# Other languages

Here is a list of community maintained translations. These are not guaranteed to always be up to date

- Chinese: https://snowcutieowo.github.io/CommandTimer/#/
