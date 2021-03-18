package com.gringostar.abac.security.policy;

public interface PolicyEnforcer {

	boolean check(Object subject, Object resource, Object action, Object environment);

}