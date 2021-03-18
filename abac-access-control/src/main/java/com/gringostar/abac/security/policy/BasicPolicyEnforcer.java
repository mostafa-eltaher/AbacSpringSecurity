package com.gringostar.abac.security.policy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;

public class BasicPolicyEnforcer implements PolicyEnforcer {
	private static final Logger logger = LoggerFactory.getLogger(BasicPolicyEnforcer.class);

	private final PolicyDefinition policyDefinition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gringostar.abac.security.policy.PolicyEnforcer#check(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean check(Object subject, Object resource, Object action, Object environment) {
		// Get all policy rules
		List<PolicyRule> allRules = policyDefinition.getAllPolicyRules();
		// Wrap the context
		SecurityAccessContext cxt = new SecurityAccessContext(subject, resource, action, environment);
		// Filter the rules according to context.
		List<PolicyRule> matchedRules = filterRules(allRules, cxt);
		// finally, check if any of the rules are satisfied, otherwise return false.
		return checkRules(matchedRules, cxt);
	}

	private List<PolicyRule> filterRules(List<PolicyRule> allRules, SecurityAccessContext cxt) {
		List<PolicyRule> matchedRules = new ArrayList<>();
		for (PolicyRule rule : allRules) {
			try {
				if (Boolean.TRUE.equals(rule.getTarget().getValue(cxt, Boolean.class))) {
					matchedRules.add(rule);
				}
			} catch (EvaluationException ex) {
				logger.info("An error occurred while evaluating PolicyRule.", ex);
			}
		}
		return matchedRules;
	}

	private boolean checkRules(List<PolicyRule> matchedRules, SecurityAccessContext cxt) {
		for (PolicyRule rule : matchedRules) {
			try {
				if (Boolean.TRUE.equals(rule.getCondition().getValue(cxt, Boolean.class))) {
					return true;
				}
			} catch (EvaluationException ex) {
				logger.info("An error occurred while evaluating PolicyRule.", ex);
			}
		}
		return false;
	}

	public BasicPolicyEnforcer(PolicyDefinition policyDefinition) {
		this.policyDefinition = policyDefinition;
	}
}
