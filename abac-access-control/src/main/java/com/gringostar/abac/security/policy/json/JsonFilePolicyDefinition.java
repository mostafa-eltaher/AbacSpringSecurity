package com.gringostar.abac.security.policy.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gringostar.abac.security.policy.PolicyDefinition;
import com.gringostar.abac.security.policy.PolicyRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.Expression;

public class JsonFilePolicyDefinition implements PolicyDefinition {
    private static Logger logger = LoggerFactory.getLogger(JsonFilePolicyDefinition.class);

    private List<PolicyRule> rules;

    public JsonFilePolicyDefinition(List<String> policyFilePaths) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Expression.class, new SpelDeserializer());
        mapper.registerModule(module);
        rules =  new ArrayList<>();
        policyFilePaths.forEach(policyFilePath -> {
            try {

                logger.debug("[init] Checking policy file: {}", policyFilePath);
                File file = new ClassPathResource(policyFilePath).getFile();
                logger.info("[init] Loading policy from custom file: {}", policyFilePath);
                this.rules
                        .addAll(Arrays.stream(mapper.readValue(file, PolicyRule[].class)).collect(Collectors.toList()));
                logger.debug("[init] Policy loaded successfully, {} rules loaded.", rules.size());

            } catch (IOException e) {
                logger.debug("[init] Policy file {} could not be loaded", policyFilePath, e);
            } catch (Exception e) {
                logger.error("Exception occurred trying to parse policy file: {}", policyFilePath, e);
            }
        });

    }

    @Override
    public List<PolicyRule> getAllPolicyRules() {
        return rules;
    }

}
