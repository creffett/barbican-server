package edu.gmu.srct.barbican.server.rule;

import java.util.HashMap;

/**
 * Created by mgauto on 10/25/14.
 */
public class Rule {
    private String moduleName;
    private HashMap<String, String> ruleData = new HashMap<String, String>();

    public Rule(String moduleName, HashMap<String, String> ruleData) {
        this.moduleName = moduleName;
        this.ruleData = ruleData;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public HashMap<String, String> getRuleData() {
        return ruleData;
    }

    public void setRuleData(HashMap<String, String> ruleData) {
        this.ruleData = ruleData;
    }
}
