package com.lunivore.gameoflife;

import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.JUnitScenario;
import org.jbehave.core.parser.PatternScenarioParser;
import org.jbehave.core.parser.ClasspathScenarioDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporter;

import com.lunivore.gameoflife.steps.GridSteps;

public class TheGridStartsEmpty extends JUnitScenario {

    public TheGridStartsEmpty() {
        super(new PropertyBasedConfiguration() {
            @Override
            public ClasspathScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternScenarioParser(this));
            }
            @Override
            public ScenarioReporter forReportingScenarios() {
                return new PrintStreamScenarioReporter();
            }
        }, new GridSteps());
    }
}
