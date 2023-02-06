# JSON Schema

If you prefer to use the JSON files directly instead of configuring tasks through the GUI, you can edit/create tasks
following this JSON schema

| Field      | Description                                                                    | Type                         |
|------------|--------------------------------------------------------------------------------|------------------------------|
| `name`     | Name of the task. Should only contain alphanumerical characters or underscores | `string`                     |
| `commands` | List of commands to execute                                                    | Array<[Commands](#commands)> |
| `interval` | List of fixed times to execute commands                                        | [Interval](#commands)        |

## Commands

## Interval

## Times

## Example

```json
{
  "name": "test",
  "commands": [
    {
      "uuid": "710e6d59-88ca-4177-940e-9aaa5acfd915",
      "command": "say 1",
      "gender": "CONSOLE"
    }
  ],
  "interval": {
    "days": 0,
    "hours": 0,
    "minutes": 0,
    "seconds": 5
  },
  "times": [
    {
      "time1": "20:00:00",
      "isMinecraftTime": false
    }
  ],
  "random": 1.0,
  "days": [
    "SUNDAY",
    "SATURDAY"
  ],
  "executionLimit": -1,
  "timesExecuted": 597,
  "lastExecutedCommandIndex": 0,
  "lastExecuted": "Nov 12, 2022, 4:20:27 PM",
  "commandExecutionMode": "ALL",
  "commandExecutionInterval": {
    "days": 0,
    "hours": 0,
    "minutes": 0,
    "seconds": 1
  },
  "active": true,
  "resetExecutionsAfterRestart": false,
  "condition": {
    "conditionType": "SIMPLE",
    "conditions": [],
    "simpleCondition": {
      "conditionParamFields": []
    }
  }
}
```