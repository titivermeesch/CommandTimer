---
title: Storage
sidebar_position: 3
---

# Storage

There are currently 2 options to store tasks. Local JSON files is the default option.

## Local JSON files

It's the most straightforward way to store tasks and does not require any additional setup.

Each time a task is created, a new file will be created in `plugins/CommandTimer/timers` with a unique ID associated
with that task. The ID is visible in the global tasks list (`/cmt` command).

## SQL Database (BETA)

**Before trying to set this up, be aware that this feature is not be production ready. Certain features could be missing or feel buggy. Please request support on Discord if you are experiencing issues**

CommandTimer supports SQL databases since v8.9.0. To use this feature you need to set up the following section in
`config.yml`:

```yaml
database:
  enabled: true
  url: jdbc:mysql://localhost:3306/commandtimer?user=root&password=admin
```

The `url` needs to point to the correct database instance database itself. The plugin will create the necessary tables.
The value `commandtimer` in the example above can be changed to anything you like as long as the database exists.

Using SQL as storage option will still create local JSON files for each task, but this is only to keep track of specific
metadata that are unique to each server. All other data will be stored in the SQL database.

**If you already have tasks saved in JSON files, you can use the command `/cmt migrateToDatabase` to move your JSON
files inside the database**