package com.lunivore.noughtsandcrosses.util;

import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.JUnitScenario;
import org.jbehave.core.parser.ClasspathScenarioDefiner;
import org.jbehave.core.parser.PatternScenarioParser;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporter;

import com.lunivore.noughtsandcrosses.steps.BeforeAndAfterSteps;
import com.lunivore.noughtsandcrosses.steps.GridSteps;

public abstract class NoughtsAndCrossesScenario extends JUnitScenario {

	public NoughtsAndCrossesScenario() {
		this(new OAndXUniverse());
	}
	
	public NoughtsAndCrossesScenario(OAndXUniverse universe) {
        super(new PropertyBasedConfiguration() {
            @Override
            public ClasspathScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternScenarioParser(this));
            }
            @Override
            public ScenarioReporter forReportingScenarios() {
                return new PrintStreamScenarioReporter();
            }
        }, new GridSteps(universe), new BeforeAndAfterSteps(universe));
     }

}
