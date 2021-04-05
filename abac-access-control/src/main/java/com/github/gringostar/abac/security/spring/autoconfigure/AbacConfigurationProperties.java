package com.github.gringostar.abac.security.spring.autoconfigure;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.abac")
public class AbacConfigurationProperties {

    private List<String> policyFiles = List.of("/abac/security/policy/json/default-policy.json");

    public List<String> getPolicyFiles() {
        return policyFiles;
    }

    public void setPolicyFiles(List<String> policyFiles) {
        this.policyFiles = policyFiles;
    }

}
