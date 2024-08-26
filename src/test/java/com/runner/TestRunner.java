package com.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/main/resources/features",
    glue = {"stepDefinitions"},
    plugin = {
        "pretty",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "json:target/reports/cucumber-report.json",
        "html:target/reports/cucumber-report.html"
    },
    dryRun = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // This class is the entry point for running Cucumber tests with TestNG
}
