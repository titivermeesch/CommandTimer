---
sidebar_position: 6
---

# JSON Schema

If you prefer to use the JSON files directly instead of configuring tasks through the GUI, you can edit/create tasks
following this JSON schema

| Field                         | Description                                                                                                                  | Type                                                                         |
|-------------------------------|------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------|
| `name`                        | Name of the task. Should only contain alphanumerical characters or underscores                                               | `string`                                                                     |
| `commands`                    | List of commands to execute                                                                                                  | [`Commands[]`](#command)                                                     |
| `interval`                    | Time between each command execution                                                                                          | [`Interval`](#interval)                                                      |
| `times`                       | List of fixed times to execute the [task](jargon#task) on                                                                    | [`Time[]`](#time)                                                            |
| `random`                      | Defines a value to calculate the execution chance. `1` is `100%`, `0.5` is `50%`, `0` is `0%` and so on                      | `number`                                                                     |
| `days`                        | List of days on which the [task](jargon#task) can be executed                                                                | `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`, `SUNDAY` |
| `executionLimit`              | Maximum amount of executions. Set to `-1` to disable feature                                                                 | `number`                                                                     |
| `timesExecuted`               | Amount of times the [task](jargon#task) has been executed. **Do not update manually**                                        | `number`                                                                     |
| `lastExecutedCommandIndex`    | Index of the last executed command. This is used in case the execution mode is set to `INTERVAL`. **Do not update manually** | `number`                                                                     |
| `lastExecuted`                | Date of the latest [task](jargon#task) execution. **Do not update manually**                                                 | `Date`                                                                       |
| `commandExecutionMode`        | Execution mode for the commands                                                                                              | [`Execution Mode`](configuration/commands#execution-modes)                   |
| `commandExecutionInterval`    | Defines time between command executions when using `INTERVAL` [execution mode](configuration/commands#execution-modes)       | [`Interval`](#interval)                                                      |
| `active`                      | Defines if the [task](jargon#task) is active or not                                                                          | `boolean`                                                                    |
| `resetExecutionsAfterRestart` | Defines if the value `executionLimit` needs to be reset when plugin restarts                                                 | `boolean`                                                                    |
| `condition`                   | Configuration for the [Conditions Engine](configuration/conditions)                                                          | [`Condition`](#condition)                                                    |
| `event`                       | Configuration for the [Events Engine](events)                                                                                | [`Event[]`](#event)                                                          |

## Command

| Field     | Description                                                    | Type                                     |
|-----------|----------------------------------------------------------------|------------------------------------------|
| `command` | Command to execute. Do not include `/` in front of the command | `string`                                 |
| `gender`  | Gender of the command                                          | [Gender](configuration/commands#genders) |

## Interval

| Field     | Description       | Type     |
|-----------|-------------------|----------|
| `days`    | Amount of days    | `number` |
| `hours`   | Amount of hours   | `number` |
| `minutes` | Amount of minutes | `number` |
| `seconds` | Amount of seconds | `number` |

## Time

| Field             | Description                                                                                                                                                | Type                          |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------|
| `time1`           | Value of the [fixed time](configuration/schedules#normal-fixed-times) or start of [range](configuration/schedules#ranged-fixed-times) if `time2` is filled | `string` in `HH:mm:ss` format |
| `time2`           | Value of the end time for a [range](configuration/schedules#ranged-fixed-times)                                                                            | `string` in `HH:mm:ss` format |
| `isMinecraftTime` | Defines if the filled in time is Minecraft time or normal time                                                                                             | `boolean`                     |

## Condition

More information about all these settings can be found [here](configuration/conditions).

As you can see, the condition object has a recursive field called `conditions`. This is because conditions can be nested
multiple times to handle complex use-cases.

| Field             | Description                                                                                                           | Type                                   |
|-------------------|-----------------------------------------------------------------------------------------------------------------------|----------------------------------------|
| `conditionType`   | Type of the condition                                                                                                 | `SIMPLE`, `NOT`, `AND`, `OR`           |
| `simpleCondition` | A condition configuration in its most simple form. Is only taken into account if `conditionType` is `SIMPLE` or `NOT` | [`SimpleCondition`](#simple-condition) |
| `conditions`      | List of conditions. Is only taken into account if `conditionType` is `AND` or `OR`                                    | [`Condition[]`](#condition)            |

### Simple condition

| Field                  | Description                                                        | Type                                                     |
|------------------------|--------------------------------------------------------------------|----------------------------------------------------------|
| `conditionGroup`       | Name of the extension this condition belongs to                    | `string`                                                 |
| `rule`                 | Specific condition rule contained in the selected `conditionGroup` | `string`                                                 |
| `conditionParamFields` | Extra values required by the selected `rule`                       | [`ConditionParamterField[]`](#condition-parameter-field) |

### Condition parameter field

| Field   | Description                              | Type     |
|---------|------------------------------------------|----------|
| `name`  | Name of the parameter                    | `string` |
| `value` | Given value for the configured parameter | `any`    |

## Event

Documentation about the Events Engine can be found [here](events)

| Field            | Description                                                                               | Type                                 |
|------------------|-------------------------------------------------------------------------------------------|--------------------------------------|
| `active`         | Defines if specific event is enabled                                                      | `boolean`                            |
| `conditionGroup` | Name of the extension this event belongs to                                               | `string`                             |
| `event`          | Specific event name contained in the selected `conditionGroup`                            | `string`                             |
| `condition`      | Configured condition for this event to execute. This is not the same as normal conditions | [`EventCondition`](#event-condition) |

### Event condition

| Field             | Description                                                                                                           | Type                                              |
|-------------------|-----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|
| `conditionType`   | Type of the condition                                                                                                 | `SIMPLE`, `NOT`, `AND`, `OR`                      |
| `simpleCondition` | A condition configuration in its most simple form. Is only taken into account if `conditionType` is `SIMPLE` or `NOT` | [`EventSimpleCondition`](#event-simple-condition) |
| `conditions`      | List of conditions. Is only taken into account if `conditionType` is `AND` or `OR`                                    | [`Condition[]`](#event-condition)                 |

### Event simple condition

| Field       | Description                                          | Type                                                                                |
|-------------|------------------------------------------------------|-------------------------------------------------------------------------------------|
| `fieldName` | Name of the parameter                                | `string`                                                                            |
| `value`     | Given value for the configured parameter             | `any`                                                                               |
| `compare`   | Compare value between event value and passed `value` | `EQUAL`, `GREATER_THAN`, `LESS_THAN`, `GREATER_OR_EQUAL_THAN`, `LESS_OR_EQUAL_THEN` |

## Example

<details>
  <summary>Complete example</summary>

```json
{
  "name": "alert_job_levelup",
  "commands": [
    {
      "command": "say test",
      "gender": "CONSOLE"
    }
  ],
  "interval": {
    "days": 1,
    "hours": 0,
    "minutes": 0,
    "seconds": 5
  },
  "times": [
    {
      "time1": "14:00:00",
      "time2": "14:00:00",
      "isMinecraftTime": false
    }
  ],
  "random": 1.0,
  "days": [
    "MONDAY",
    "TUESDAY",
    "WEDNESDAY",
    "THURSDAY",
    "FRIDAY",
    "SATURDAY",
    "SUNDAY"
  ],
  "executionLimit": -1,
  "timesExecuted": 7,
  "lastExecutedCommandIndex": 0,
  "lastExecuted": "Feb 8, 2023, 9:19:51 PM",
  "commandExecutionMode": "INTERVAL",
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
      "conditionGroup": "JOBSREBORN",
      "rule": "HAS_SPECIFIC_JOB",
      "conditionParamFields": [
        {
          "name": "required_job",
          "value": ""
        },
        {
          "name": "LEVEL",
          "value": 0
        }
      ]
    }
  },
  "events": [
    {
      "active": true,
      "conditionGroup": "JOBSREBORN",
      "event": "LEVEL_UP",
      "condition": {
        "conditionType": "SIMPLE",
        "conditions": [],
        "simpleCondition": {
          "fieldName": "LEVEL",
          "value": 12.0,
          "compare": "EQUAL"
        }
      }
    }
  ]
}
```

</details>
