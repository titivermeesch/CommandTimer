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

## Days

In the days menu, you can select on which days the [task](../jargon#task) will be executed. By default all the days are selected.

This also takes into account possible configured fixed times.
