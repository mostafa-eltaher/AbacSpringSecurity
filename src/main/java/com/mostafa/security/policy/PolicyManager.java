package com.mostafa.security.policy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Component
public class PolicyManager {
	private List<PolicyRule> rules;
	
	@PostConstruct
	private void init(){
		ExpressionParser exp = new SpelExpressionParser();
		rules = new ArrayList<>();
		
		String ownerRule = "subject.name == resource.owner";
		String targetRule = "true";

		rules.add(new PolicyRule(exp.parseExpression(targetRule), exp.parseExpression(ownerRule)));		
	}
	public List<PolicyRule> getAllPolicies() {
		return rules;
	}

}
