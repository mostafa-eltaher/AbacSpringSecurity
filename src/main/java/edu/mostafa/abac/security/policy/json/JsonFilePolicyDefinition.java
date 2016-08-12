package edu.mostafa.abac.security.policy.json;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.mostafa.abac.security.policy.PolicyDefinition;
import edu.mostafa.abac.security.policy.PolicyRule;

@Component
public class JsonFilePolicyDefinition implements PolicyDefinition {
	private static Logger logger = LoggerFactory.getLogger(JsonFilePolicyDefinition.class);
	
	private static String DEFAULT_POLICY_FILE_NAME = "default-policy.json";

	private List<PolicyRule> rules;
	
	@PostConstruct
	private void init(){
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Expression.class, new SpelDeserializer());
		mapper.registerModule(module);
		try {
			PolicyRule[] staff = mapper.readValue(getClass().getResourceAsStream(DEFAULT_POLICY_FILE_NAME), PolicyRule[].class);
			this.rules = Arrays.asList(staff);
		} catch (JsonMappingException e) {
			logger.error("An error occurred while parsing the policy file.", e);
		} catch (IOException e) {
			logger.error("An error occurred while reading the policy file.", e);
		}
	}
	
	@Override
	public List<PolicyRule> getAllPolicyRules() {
		return rules;
	}

}
