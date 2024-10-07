---
sidebar_position: 4
---

# Extensions

Extensions are used to add extra functionality that is not there by default. Each extension comes in the form of a jar
file that CommandTimer will use to extend its original functionality.

## Installing extensions

Extensions should not be treated as plugins. Extensions need to be placed under the `commandtimer/extensions` folder
instead.

Once the jar file is in that folder, you should see the extension in different menus (for example the conditions menu)

## Available extensions

Here is a list of extensions that can be installed. Official extensions are made by the team behind CommandTimer, while
community extensions, as the name says, are made by the community.

### Oficial extensions

- [Time Extension](https://www.spigotmc.org/resources/time-conditions-commandtimer-extension.105591/)
- [Player Extension](https://www.spigotmc.org/resources/player-conditions-commandtimer-extension.97186/)
- [Server Extension](https://www.spigotmc.org/resources/server-conditions-commandtimer-extension.97188/)
- [GriefDefender Extension](https://www.spigotmc.org/resources/griefdefender-conditions-commandtimer-extension.106330/)
- [WorldGuard Extension](https://www.spigotmc.org/resources/worldguard-conditions-commandtimer-extension.112403/)
- [Vault Extension](https://www.spigotmc.org/resources/vault-conditions-commandtimer-extension.112471/)

### Community extensions

You made an extension and want to add it here? Open a ticket on [GitHub](https://github.com/titivermeesch/CommandTimer)

- [PlaceholderAPI Extension](https://github.com/TreemanKing/CommandTimer-PAPIConditions)

## Creating your own extension

Creating an extension is pretty straight forward. Follow this guide to get started.

1. Create a new Java project. Ideally you use a dependency manager like Maven or Gradle
2. Add CommandTimer as dependency for your project. See [Developers Documentation](../developers/index.md) for further details
3. Create a class which will be the entry point for your extension. This class **needs** to
   extend `ConditionExtension` (located at `me.playbosswar.com.api.ConditionExtension`)
4. Once that main class is set up, you can now continue reading to add true functionality. I would recommend to first
   compile your jar file and check if it appears in the condition menu in-game.

### Adding extension conditions

One of the possibilities of extensions is adding conditions for the conditions engine.

A condition is represented by a class extending `ConditionRule` (located at `me.playbosswar.com.api.ConditionRule`). You
will also need to add the [Easy Rules](https://github.com/j-easy/easy-rules) package to your project. You don't need to
shade this package though, CommandTimer already comes pre-packed with it. The version used by CommandTimer is `4.1.0` so
ideally your extension also uses the same version.

The most important part of a condition is the `ConditionRule#evaluate(Facts facts)` method. This one will be triggered
for every CommandTimer execution loop (around once a second) and check if the condition is met.

In these facts, only `player` is available by default. **Player will not be available depending on certain genders like
CONSOLE**.

To access the player that is currently being used to check the condition, one could do:

```java
Player p = facts.get("player");
```

Another important method here is `ConditionRule#getNeededValues()`. This method should return a list of `NeededValue`
specifying which external values the condition needs. You can see this as extra configuration values provided by the
player. Here is a list of supported types:

- Double
- Integer
- String
- ConditionCompare

All these types are the native Java types, except `ConditionCompare`, which is used to define a comparison between 2
values. This could be EQUAL, LESS_THEN, BIGGER_OR_EQUAL_THEN,... The different choices are available on the class
itself. A good example of this is
available [here](https://github.com/titivermeesch/CommandTimer_PlayerConditions/blob/master/src/main/java/me/playbosswar/cmtplayerconditions/conditions/PlayerTimeInWorldCondition.java)

### Examples

A few examples can be found here:

- <https://github.com/titivermeesch/CommandTimer_TimeConditions>
- <https://github.com/titivermeesch/CommandTimer_GriefDefender>
- <https://github.com/titivermeesch/CommandTimer_PlayerConditions>
