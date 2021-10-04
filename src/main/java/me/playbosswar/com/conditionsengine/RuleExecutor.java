package me.playbosswar.com.conditionsengine;

public enum RuleExecutor {
    PLAYER_TIME_IN_WORLD("PLAYER_TIME_IN_WORLD", RuleTarget.PLAYER),
    PLAYER_IS_OP("PLAYER_IS_OP", RuleTarget.PLAYER),
    PLAYER_HEALTH_RULE("PLAYER_HEALTH_RULE", RuleTarget.PLAYER);

    String ruleName;
    RuleTarget ruleTarget;
    RuleExecutor(String ruleName, RuleTarget ruleTarget) {
        this.ruleName = ruleName;
        this.ruleTarget = ruleTarget;
    }

    public String getRuleName() {
        return ruleName;
    }
}
