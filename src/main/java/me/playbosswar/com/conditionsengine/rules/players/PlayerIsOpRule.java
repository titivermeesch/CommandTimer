package me.playbosswar.com.conditionsengine.rules.players;

import org.bukkit.entity.Player;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "PLAYER_IS_OP", description = "Check if player has op")
public class PlayerIsOpRule {
    @Condition
    public boolean execute(@Fact("player") Player p) {
        return p.isOp();
    }

    @Action
    public void action() {}
}
