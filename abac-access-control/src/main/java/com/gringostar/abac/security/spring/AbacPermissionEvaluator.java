package com.gringostar.abac.security.spring;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gringostar.abac.security.policy.PolicyEnforcer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class AbacPermissionEvaluator implements PermissionEvaluator {
	private static Logger logger = LoggerFactory.getLogger(AbacPermissionEvaluator.class);
	
	private final PolicyEnforcer policyEnforcer;

	@Override
	public boolean hasPermission(Authentication authentication , Object targetDomainObject, Object permission) {
		Object user = authentication.getPrincipal();
		Map<String, Object> environment = new HashMap<>();
		
		/*
		Object authDetails = authentication.getDetails();
		if(authDetails != null) {
			if(authDetails instanceof WebAuthenticationDetails) {
				environment.put("remoteAddress", ((WebAuthenticationDetails) authDetails).getRemoteAddress());
			}
		}
		*/
		environment.put("time", new Date());
		boolean result = policyEnforcer.check(user, targetDomainObject, permission, environment);
		logger.debug("hasPermission({}, {}, {}) = {}", user, targetDomainObject, permission, result);
		return result;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false;
	}

	public AbacPermissionEvaluator(PolicyEnforcer policyEnforcer) {
		this.policyEnforcer = policyEnforcer;
	}

}
