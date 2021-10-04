package me.playbosswar.com.conditionsengine.rules.players;

import me.playbosswar.com.conditionsengine.ConditionCompare;
import me.playbosswar.com.conditionsengine.conditions.ConditionHelpers;
import org.bukkit.entity.Player;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "PLAYER_HEALTH_RULE", description = "Check player health")
public class PlayerHealthRule {
    @Condition
    public boolean execute(@Fact("player") Player p,
                           @Fact("conditionCompare") ConditionCompare conditionCompare,
                           @Fact("numericValue") double numericValue) {
        return ConditionHelpers.calculateConditionCompare(conditionCompare, p.getHealth(), numericValue);
    }

    @Action
    public void action() {}
}
