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

| Command                  | Description                                             | Permission                                          |
|--------------------------|---------------------------------------------------------|-----------------------------------------------------|
| `/cmt`                   | Open main CommandTimer menu where everything is managed | `commandtimer.manage`                               |
| `/cmt help`              | Get a list of all possible commands                     | Any CommandTimer permission                         |
| `/cmt time`              | Get the current world time the player is in             | Any CommandTimer permission                         |
| `/cmt activate <task>`   | Enable the specified task                               | `commandtimer.activate` or `commandtimer.toggle`    |
| `/cmt deactivate <task>` | Disable the specified task                              | `commandtimer.deactivate`  or `commandtimer.toggle` |
| `/cmt execute <task>`    | Instantly execute the specified task                    | `commandtimer.execute`                              |
