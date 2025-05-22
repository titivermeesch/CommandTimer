---
sidebar_position: 5
---

# Placeholders

## CommandTimer Placeholders

Several placeholders are provided that can be used when PlaceholderAPI is installed. For each placeholder, do not forget
to replace `task` with your actual task name.

- `%commandtimer_task_seconds%`: Get the configured [interval](configuration/schedules#intervals) in seconds
- `%commandtimer_task_secondsFormat%`: Same as previous placeholder, but formated as `HH:mm:ss`
- `%commandtimer_ALLTASKS_nextTaskName%`: Show the name of the next task that will be executed
- `%commandtimer_task_nextExecution%`: Get the next execution time in seconds
- `%commandtimer_task_nextExecutionFormat%`: Same as previous placeholder, but formatted in `HH:mm:ss`
- `%commandtimer_task_lastExecution%`: Get last execution time in seconds
- `%commandtimer_task_lastExecutionFormat%`: Same as previous placeholder, but formatted in `HH:mm:ss`
- `%commandtimer_task_timeFormat%`: Same as previous placeholders, but you can replace `timeFormat` with a format of your
  choice. You can use `DD`, `HH`, `mm` and `ss` as time selectors in your placeholder. If you want to
  escape certain characters you can use `'` around the character you want to escape. For a time format `12h34m03s` you
  will need the placeholder `%commandtimer_task_HH'h'mm'm'ss's'%`. Depending on your configuration file, you will need
  to change your outer `'` quotes with `"` to keep a valid configuration.

The `task` value can be replaced with `ALLTASKS` for `nextExection`, `nextExecutionFormat` and `timeFormat` if you want
to take into account all active tasks

## Fallback values

`nextExecution`, `nextExecutionFormat`, `timeFormat` and `nextTaskName` can be suffixed with a fallback value in
case there is no next execution anymore. For example- `%commandtimer_task_nextExecutionFormat_No next execution%`

## PAPI Placeholders

All PlaceholderAPI (PAPI) placeholders are supported in the commands. Using placeholders in commands will also give the
placeholders access to the user for whom the command is executed (for example when using the `CONSOLE_PER_USER` gender).
