package com.github.gringostar.abac.security.policy;

import java.util.List;

public interface PolicyDefinition {
	List<PolicyRule> getAllPolicyRules();
}
