---
sidebar_position: 4
---
# Others

## General limits

### Execution limits

By default, a [task](../jargon#task) can be executed an infinite amount of times. If you want the [task](../jargon#task) to only be executed e.g. a 100 times, you can set your execution limit to 100.

If you want to set the execution limit to infinite again, use the value -1.

CommandTimer will keep track of the executions between server reloads or restarts, but this can also be disabled by activating the reset execution after restart feature.

This is very useful if you only want to execute a command on server start.
