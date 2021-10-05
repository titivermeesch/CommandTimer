package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.conditionsengine.ConditionParams;
import me.playbosswar.com.conditionsengine.RuleExecutor;

public class SimpleCondition {
    private RuleExecutor ruleExecutor;
    private ConditionParams conditionParams;

    public SimpleCondition(RuleExecutor ruleExecutor, ConditionParams conditionParams) {
        this.ruleExecutor = ruleExecutor;
        this.conditionParams = conditionParams;
    }

    public RuleExecutor getRuleExecutor() {
        return ruleExecutor;
    }

    public void setRuleExecutor(RuleExecutor ruleExecutor) {
        this.ruleExecutor = ruleExecutor;
    }

    public ConditionParams getConditionParams() {
        return conditionParams;
    }

    public void setConditionParams(ConditionParams conditionParams) {
        this.conditionParams = conditionParams;
    }
}
