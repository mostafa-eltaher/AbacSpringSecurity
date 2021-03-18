package com.gringostar.abac.security.spring;

import com.gringostar.abac.security.policy.PolicyEnforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this class in any of the Spring Beans to evaluate security policy.
 *
 * @author <a href="mailto:mostafa.mahmoud.eltaher@gmail.com">Mostafa Eltaher</a>
 */
@Component
public class ContextAwarePolicyEnforcement {
    @Autowired
    protected PolicyEnforcer policy;

    public void checkPermission(Object resource, String permission) {
        if (!hasAccess(resource, permission))
            throw new AccessDeniedException("Access is denied");
    }

    public boolean hasAccess(Object resource, String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> environment = new HashMap<>();

        environment.put("time", new Date());

        return policy.check(auth.getPrincipal(), resource, permission, environment);
    }
}
