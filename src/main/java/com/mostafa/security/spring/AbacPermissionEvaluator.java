package com.mostafa.security.spring;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mostafa.security.policy.PolicyEnforcement;

@Component
public class AbacPermissionEvaluator implements PermissionEvaluator {
	private static Logger logger = LoggerFactory.getLogger(AbacPermissionEvaluator.class);
	@Autowired
	PolicyEnforcement policy;

	@Override
	public boolean hasPermission(Authentication authentication , Object targetDomainObject, Object permission) {
		logger.debug("hasPersmission({}, {}, {})", authentication, targetDomainObject, permission);
		return policy.check(authentication, targetDomainObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
