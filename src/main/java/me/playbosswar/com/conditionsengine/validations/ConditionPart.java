package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.conditionsengine.ConditionParams;
import me.playbosswar.com.conditionsengine.RuleExecutor;

import java.util.List;

public class ConditionPart {
    // Simple, not, or
    private ConditionType conditionType;
    private RuleExecutor ruleExecutor;
    private ConditionParams conditionParams;
    private List<ConditionPart> conditionParts;

    public ConditionPart(ConditionType conditionType, RuleExecutor ruleExecutor, ConditionParams conditionParams,
                         List<ConditionPart> conditionParts) {
        this.conditionType = conditionType;
        this.ruleExecutor = ruleExecutor;
        this.conditionParams = conditionParams;
        this.conditionParts = conditionParts;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
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

    public void setConditionParams(ConditionParams params) {
        this.conditionParams = conditionParams;
    }

    public List<ConditionPart> getConditionParts() {
        return conditionParts;
    }

    public void setConditionParts(List<ConditionPart> conditionParts) {
        this.conditionParts = conditionParts;
    }
}
