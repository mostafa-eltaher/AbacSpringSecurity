package edu.mostafa.abac.security.policy;

import org.springframework.expression.Expression;

public class PolicyRule {
	private String name;
	private String description;
	/*
	 * Boolean SpEL expression. If evaluated to true, then this rule is applied to the request access context.
	 */
	private Expression  target;
	
	/*
	 * Boolean SpEL expression, if evaluated to true, then access granted.
	 */
	private Expression  condition;
	
	public PolicyRule() {
		
	}

	public PolicyRule(String name, String description, Expression target, Expression condition) {
		this(target, condition);
		this.name = name;
		this.description = description;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
