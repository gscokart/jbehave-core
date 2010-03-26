package com.lunivore.gameoflife;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.ClasspathScenarioDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporter;

import com.lunivore.gameoflife.steps.GridSteps;

public class TheGridStartsEmpty extends JUnitStory {

    public TheGridStartsEmpty() {
        super(new PropertyBasedConfiguration() {
            @Override
            public ClasspathScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(this));
            }
            @Override
            public ScenarioReporter forReportingScenarios() {
                return new PrintStreamScenarioReporter();
            }
        }, new GridSteps());
    }
}
