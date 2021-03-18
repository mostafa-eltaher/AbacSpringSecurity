package com.gringostar.abac.security.spring;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Use this class in any of the Spring Beans to evaluate security policy.
 *
 */
public class ContextAwarePolicyEnforcer {

    private final PermissionEvaluator permissionEvaluator;

    public void checkPermission(Object resource, String permission) {
        if (!hasAccess(resource, permission))
            throw new AccessDeniedException("Access is denied");
    }

    public boolean hasAccess(Object resource, String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return permissionEvaluator.hasPermission(auth, resource, permission);
    }

    public ContextAwarePolicyEnforcer(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }
}
