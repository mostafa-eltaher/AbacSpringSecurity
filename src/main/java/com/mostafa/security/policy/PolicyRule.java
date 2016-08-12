package com.mostafa.security.policy;

import org.springframework.expression.Expression;

public class PolicyRule {
	/*
	 * Boolean SpEL expression. If evaluated to true, then this rule is applied to the request access context.
	 */
	private Expression  target;
	
	/*
	 * Boolean SpEL expression, if evaluated to true, then access granted.
	 */
	private Expression  condition;
	
	

	public PolicyRule(Expression  target, Expression condition) {
		super();
		this.target = target;
		this.condition = condition;
	}

	public Expression  getTarget() {
		return target;
	}

	public void setTarget(Expression  target) {
		this.target = target;
	}

	public Expression  getCondition() {
		return condition;
	}

	public void setCondition(Expression  condition) {
		this.condition = condition;
	}
}
