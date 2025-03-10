package org.example.gym.component.steps;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = org.example.gym.config.TestContainerConfiguration.class)
@CucumberContextConfiguration
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"org.example.gym.component.steps"},
        features = "src/test/resources/features",
        publish = true
)
public class CucumberTestRunner {
}
