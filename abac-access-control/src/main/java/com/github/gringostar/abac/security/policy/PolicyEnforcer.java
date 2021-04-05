package com.github.gringostar.abac.security.policy;

public interface PolicyEnforcer {

	boolean check(Object subject, Object resource, Object action, Object environment);

}