---
sidebar_position: 2
---
# Schedules

CommandTimer supports different types of scheduling, and can also be used together to accommodate for more complex scenarios

## Intervals

Intervals simply represent a duration that will be used to repeatedly execute a command.

## Fixed times

Fixed times can be used to schedule a command at a specific time, for example 14:30:00. These fixed times also allow you to create ranges in which commands can be executed. More on that below.

Ranges and normal times can be mixed together without any issues.

### Normal fixed times

As stated before, this simply represents a time at which a commands get executed. When using this, a possible configured [interval](#intervals) is not taken into account.

### Ranged fixed times

It is possible to set a second time when configuring a fixed time. When doing so, the [interval](#intervals) will be taken into account and the command will be executed according to the set [interval](#intervals) in between those 2 times.

### Minecraft world time

For both fixed times and ranges, Minecraft time can be used instead of the server time. This can be activated per fixed time, meaning that in one single [task](../jargon#task) server time and Minecraft time can be mixed.

When configuring Minecraft time, a world also needs to be selected to use the time from.

If you want to know the world time, go to the world you want to check and execute `/cmt time`

## Interval Start Time

By default, interval-based tasks schedule executions relative to when the task was last run. This means the exact execution times can shift depending on when your server started or when the task was activated.

The **Interval Start Time** feature lets you anchor executions to fixed clock times instead. When a start time is configured, executions will always land on a predictable grid.

### How it works

The start time acts as an anchor point. Combined with your interval, it creates a repeating grid of execution times that stays consistent regardless of server restarts.

For example, if you set:
- **Interval**: 15 minutes
- **Start Time**: 15:00

Your task will execute at: ..., 14:00, 14:15, 14:30, 14:45, 15:00, 15:15, 15:30, ...

The grid extends across the entire day based on the interval. Even if your server restarts at 14:02, the next execution will still be at 14:15 — not at 14:17.

### Examples

| Interval | Start Time | Executions |
|----------|-----------|------------|
| 15 min | 15:00 | 00:00, 00:15, 00:30, ..., 14:45, 15:00, 15:15, ... |
| 2 hours | 13:00 | 01:00, 03:00, 05:00, 07:00, 09:00, 11:00, 13:00, ... |
| 30 min | 10:15 | 00:15, 00:45, 01:15, ..., 10:15, 10:45, 11:15, ... |

### Configuring

In the task scheduler menu, click the **Interval Start Time** item (compass icon). From there you can set the hours, minutes, and seconds for the anchor time. Use the **Clear** button to remove the start time and go back to the default behavior.

:::tip
This is especially useful for tasks that need to run at predictable clock-aligned times, such as hourly announcements or scheduled restarts.
:::

:::note
The start time only affects interval-based scheduling. If your task uses [fixed times](#fixed-times), those already execute at exact clock times and don't need a start time anchor.
:::

## Days

In the days menu, you can select on which days the [task](../jargon#task) will be executed. By default all the days are selected.

This also takes into account possible configured fixed times.
