---
sidebar_position: 3
---

# Ad-Hoc Commands

Ad-hoc commands allow you to schedule one-time command executions at a specific time in the future without creating a full [task](../jargon#task). This is perfect for temporary commands, delayed actions, or one-off scheduled executions.

## Overview

Ad-hoc commands are single-use scheduled commands that execute once at a specified time. Unlike regular tasks, they don't have intervals, conditions, or multiple commands - they're designed for simple, one-time executions.

### Key Features

- **One-time execution**: Commands execute once at the scheduled time
- **Flexible scheduling**: Schedule commands to run at any future time
- **All gender types supported**: Use any [gender](commands.md#genders) for command execution
- **Persistent storage**: Commands are saved to disk and survive server restarts
- **GUI management**: View and manage scheduled ad-hoc commands through the GUI

## Command Syntax

Schedule an ad-hoc command using the `/cmt schedule` command:

```
/cmt schedule [-after <time>] [-gender <gender>] <command>
```

### Parameters

- **`-after <time>`** (optional): Delay before execution. Format: `1h10m5s` (hours, minutes, seconds)
  - Examples: `30s`, `5m`, `1h30m`, `2h15m30s`
  
- **`-gender <gender>`** (optional): The [gender](commands.md#genders) to use for execution
  - Valid options: `CONSOLE`, `PLAYER`, `OPERATOR`, `CONSOLE_PER_USER`, `CONSOLE_PER_USER_OFFLINE`, `CONSOLE_PROXY`
  - Default: `CONSOLE`
  
- **`<command>`** (required): The command to execute (without the leading `/`)

### Examples

#### Basic Examples

```bash
# Schedule a broadcast message in 5 minutes
/cmt schedule -after 5m say Server maintenance in 5 minutes!

# Schedule a command to run in 1 hour
/cmt schedule -after 1h weather clear

# Schedule a command to run in 2 hours, 15 minutes, and 30 seconds
/cmt schedule -after 2h15m30s time set day
```

#### Using Different Genders

```bash
# Execute as console (default)
/cmt schedule -after 10m say Announcement

# Execute for each online player
/cmt schedule -after 30m -gender CONSOLE_PER_USER give %player_name% diamond 1

# Execute as if each player is OP
/cmt schedule -after 1h -gender OPERATOR gamemode creative %player_name%

# Execute for each player (respects permissions)
/cmt schedule -after 2h -gender PLAYER home
```

#### Real-World Use Cases

```bash
# Schedule a server restart announcement
/cmt schedule -after 30m say Server restarting in 30 minutes!
/cmt schedule -after 15m say Server restarting in 15 minutes!
/cmt schedule -after 5m say Server restarting in 5 minutes!
/cmt schedule -after 1m say Server restarting in 1 minute!

# Schedule a timed event start
/cmt schedule -after 1h -gender CONSOLE_PER_USER title %player_name% title {"text":"Event Starting!"}

# Schedule maintenance commands
/cmt schedule -after 2h save-all
/cmt schedule -after 2h5m say Maintenance complete!

# Schedule player rewards
/cmt schedule -after 24h -gender CONSOLE_PER_USER give %player_name% emerald 10
```

## Managing Ad-Hoc Commands

### GUI Management

You can view and manage ad-hoc commands through the **Scheduled Executions** menu:

1. Open the main menu: `/cmt`
2. Click on **Scheduled Executions**
3. Use the filter button to show only ad-hoc commands (filter: "Ad-Hoc Only")
4. Right-click on an ad-hoc command to cancel it

## Differences from Regular Tasks

| Feature | Ad-Hoc Commands | Regular Tasks |
|----------|----------------|---------------|
| **Execution** | One-time only | Recurring or event-based |
| **Commands** | Single command | Multiple commands |
| **Intervals** | Not supported | Supported |
| **Conditions** | Not supported | Supported |
| **Events** | Not supported | Supported |
| **Execution Modes** | Not applicable | ALL, ORDERED, RANDOM, INTERVAL |
| **Use Case** | Temporary, one-off commands | Permanent, recurring automation |

## Permissions

- **`commandtimer.schedule`**: Required to schedule ad-hoc commands
- **`commandtimer.manage`**: Required to view the GUI and manage commands

## Troubleshooting

### Command Not Executing

- Check that the scheduled time has passed
- Verify the command syntax is correct
- Check server logs for execution errors
- Ensure the ad-hoc command runner is active (should start automatically)

### Command Executing Too Early/Late

- Commands execute within 0.5 seconds of their scheduled time
- Server lag may cause slight delays
- For precise timing, schedule commands slightly earlier than needed

### Commands Lost After Restart

- Commands are saved to disk and should persist
- Check that the scheduled time hasn't passed yet
- Only non-executed commands are loaded on startup

