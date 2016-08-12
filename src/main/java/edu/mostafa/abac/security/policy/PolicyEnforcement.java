package edu.mostafa.abac.security.policy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationException;
import org.springframework.stereotype.Component;

@Component
public class PolicyEnforcement {
	private static Logger logger = LoggerFactory.getLogger(PolicyEnforcement.class);
	
	@Autowired
	private PolicyDefinition policyDefinition;
	
	public boolean check(Object subject, Object resource, Object action) {
		List<PolicyRule> matchedRules = new ArrayList<>();
		List<PolicyRule> allRules = policyDefinition.getAllPolicyRules();
		
		SecurityAccessContext cxt = new SecurityAccessContext(subject, resource, action, null);
		for(PolicyRule rule : allRules) {
			try {
				if(rule.getTarget().getValue(cxt, Boolean.class)) {
					matchedRules.add(rule);
				}
			} catch(EvaluationException ex) {
				logger.info("An error occurred while evaluating PolicyRule.", ex);
			}
		}
		
		for(PolicyRule rule : matchedRules) {
			try {
				if(rule.getCondition().getValue(cxt, Boolean.class)) {
					return true;
				}
			} catch(EvaluationException ex) {
				logger.info("An error occurred while evaluating PolicyRule.", ex);
			}
		}
		return false;
	}
}
