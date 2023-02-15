---
sidebar_position: 3
---
# Conditions

**DISCLAIMER: This is one of the most complex parts of CommandTimer. Please look at the rest of the documentation first before trying out the conditions engine**

The conditions engine is used to add extra logic on top of your [fixed times](schedules#Fixed-times) and [intervals](schedules#intervals).

Conditions can have one of the following types:

- `SIMPLE`: This is the simplest form and defines one single condition. Result of this condition needs to be true (e.g. player IS operator)
- `NOT`: Same as above, but the result is reversed (e.g. player IS NOT operator)
- `AND`: Creates a group of conditions. Each of these conditions needs to be true to execute your [task](../jargon#task).
- `OR`: Creates a group of conditions. One of these conditions needs to be true to execute your [task](../jargon#task). Even if you have 50 conditions that do not match but one does, the [task](../jargon#task) will still go through

When using `AND` and `OR`, more conditions can be nested. Nesting does not have a depth limit, meaning you could have an `AND` condition in an `OR` condition in an `OR` condition in an `AND` condition which itself contains 4 different `SIMPLE` and `NOT` conditions. It is good to draw a visual map yourself before doing these nested conditions, because it can be cumberstone to debug in case there is an issue.

## Available conditions

Conditions are made availabel through [extensions](../extensions). This means that CommandTimer by default will not have any conditions, making the whole conditions engine useless if no [extensions](../extensions) are installed.

## Condition values

Certain conditions may require extra configuration values to work properly. These will be visible in the GUI
