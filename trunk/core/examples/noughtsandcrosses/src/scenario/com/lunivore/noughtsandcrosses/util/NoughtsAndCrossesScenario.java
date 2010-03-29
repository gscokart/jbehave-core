package com.lunivore.noughtsandcrosses.util;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedStoryConfiguration;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;

import com.lunivore.noughtsandcrosses.steps.BeforeAndAfterSteps;
import com.lunivore.noughtsandcrosses.steps.GridSteps;

public abstract class NoughtsAndCrossesScenario extends JUnitStory {

	public NoughtsAndCrossesScenario() {
		this(new OAndXUniverse());
	}
	
	public NoughtsAndCrossesScenario(OAndXUniverse universe) {
        super(new PropertyBasedStoryConfiguration() {
            @Override
            public ClasspathStoryDefiner forDefiningScenarios() {
                return new ClasspathStoryDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(this));
            }
            @Override
            public StoryReporter forReportingScenarios() {
                return new PrintStreamStoryReporter();
            }
        }, new GridSteps(universe), new BeforeAndAfterSteps(universe));
     }

}
