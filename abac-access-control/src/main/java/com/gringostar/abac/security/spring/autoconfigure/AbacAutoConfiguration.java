package com.gringostar.abac.security.spring.autoconfigure;

import com.gringostar.abac.security.policy.BasicPolicyEnforcer;
import com.gringostar.abac.security.policy.PolicyDefinition;
import com.gringostar.abac.security.policy.PolicyEnforcer;
import com.gringostar.abac.security.policy.json.JsonFilePolicyDefinition;
import com.gringostar.abac.security.spring.AbacPermissionEvaluator;
import com.gringostar.abac.security.spring.ContextAwarePolicyEnforcer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AbacConfigurationProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AbacAutoConfiguration extends GlobalMethodSecurityConfiguration {

    private final AbacConfigurationProperties properties;


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler result = new DefaultMethodSecurityExpressionHandler();
        result.setPermissionEvaluator(abacPermissionEvaluator());
        return result;
    }

    @Bean
    public ContextAwarePolicyEnforcer contextAwarePolicyEnforcer() {
        return new ContextAwarePolicyEnforcer(abacPermissionEvaluator());
    }

    @Bean
    public PolicyEnforcer policyEnforcer(PolicyDefinition policyDefinition) {
        return new BasicPolicyEnforcer(policyDefinition);
    }

    @Bean
    public PolicyDefinition policyDefinition() {
        return new JsonFilePolicyDefinition(properties.getPolicyFiles());
    }

    @Bean
    public AbacPermissionEvaluator abacPermissionEvaluator() {
        return new AbacPermissionEvaluator(policyEnforcer(policyDefinition()));
    }

    public AbacAutoConfiguration(AbacConfigurationProperties properties) {
        this.properties = properties;
    }

}
