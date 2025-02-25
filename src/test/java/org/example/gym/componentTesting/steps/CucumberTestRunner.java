package org.example.gym.componentTesting.steps;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = org.example.gym.config.TestContainerConfiguration.class)
@WebAppConfiguration
@CucumberContextConfiguration
@AutoConfigureMockMvc
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"org.example.gym.componentTesting.steps"},
        features = "src/test/resources/features",
        publish = true
)
public class CucumberTestRunner {
}
