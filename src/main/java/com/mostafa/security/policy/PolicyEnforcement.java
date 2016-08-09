package com.mostafa.security.policy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationException;
import org.springframework.stereotype.Component;

@Component
public class PolicyEnforcement {
	@Autowired
	private PolicyManager policyManager;
	
	public boolean check(Object subject, Object resource, Object action) {
		List<PolicyRule> matchedRules = new ArrayList<>();
		List<PolicyRule> allRules = policyManager.getAllPolicies();
		
		SecurityAccessContext cxt = new SecurityAccessContext(subject, resource, action, null);
		for(PolicyRule rule : allRules) {
			try {
				if(rule.getTarget().getValue(cxt, Boolean.class)) {
					matchedRules.add(rule);
				}
			} catch(EvaluationException ex) {
				//just ignore
			}
		}
		
		for(PolicyRule rule : matchedRules) {
			try {
				if(rule.getCondition().getValue(cxt, Boolean.class)) {
					return true;
				}
			} catch(EvaluationException ex) {
				//just ignore
			}
		}
		return false;
	}
}
