package me.playbosswar.com.conditionsengine;


import me.playbosswar.com.conditionsengine.rules.players.PlayerHealthRule;
import me.playbosswar.com.conditionsengine.rules.players.PlayerIsOpRule;
import me.playbosswar.com.conditionsengine.rules.players.PlayersTimeInWorldRule;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;

public class ConditionEngineManager {
    private final Rules rules = new Rules();

    public ConditionEngineManager() {
        rules.register(new PlayersTimeInWorldRule(), new PlayerIsOpRule(), new PlayerHealthRule());
    }

    public Rule getRule(String ruleName) {
        for (Rule rule : rules) {
            if (rule.getName().equalsIgnoreCase(ruleName)) {
                return rule;
            }
        }

        return null;
    }

    public Rules getRules() {
        return rules;
    }

    public void onDisable() {
        rules.clear();
    }
}
