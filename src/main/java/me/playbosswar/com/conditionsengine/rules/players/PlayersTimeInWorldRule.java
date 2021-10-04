package me.playbosswar.com.conditionsengine.rules.players;

import me.playbosswar.com.Main;
import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.conditions.ConditionHelpers;
import org.bukkit.entity.Player;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "PLAYER_TIME_IN_WORLD", description = "Check for how long the player is in the same world")
public class PlayersTimeInWorldRule {
    @Condition
    public boolean execute(@Fact("player") Player p,
                           @Fact("conditionCompare") ConditionCompare conditionCompare,
                           @Fact("numericValue") double numericValue) {
        int secondsInWorld = Main.getPlayerWorldTimeTracking().getSecondsInWorldForPlayer(p);

        return ConditionHelpers.calculateConditionCompare(conditionCompare, secondsInWorld, numericValue);
    }

    @Action
    public void action() {}
}
