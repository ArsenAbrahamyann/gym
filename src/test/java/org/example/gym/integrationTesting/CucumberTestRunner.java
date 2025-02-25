package org.example.gym.integrationTesting;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"org.example.gym.integrationTesting"},
        features = {"classpath:feature_Integration/gymServiceIntegration.feature"},
        plugin = {"pretty", "json:target/cucumber-report.json"}
)
@CucumberContextConfiguration
@SpringBootTest(classes = org.example.gym.config.TestContainerConfiguration.class)
public class CucumberTestRunner {
}



