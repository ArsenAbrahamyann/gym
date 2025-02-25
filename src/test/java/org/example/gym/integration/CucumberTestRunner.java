package org.example.gym.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"org.example.gym.integration"},
        features = {"classpath:feature_Integration/gymServiceIntegration.feature"},
        plugin = {"pretty", "json:target/cucumber-report.json"}
)
@CucumberContextConfiguration
@ContextConfiguration(classes = org.example.gym.config.TestContainerConfiguration.class)
public class CucumberTestRunner {
}



