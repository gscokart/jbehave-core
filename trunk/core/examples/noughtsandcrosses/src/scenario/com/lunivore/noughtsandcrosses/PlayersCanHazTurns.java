package com.lunivore.noughtsandcrosses;

import org.jbehave.core.MostUsefulConfiguration;
import org.jbehave.core.JUnitScenario;
import org.jbehave.core.definition.KeyWords;
import org.jbehave.core.parser.PatternScenarioParser;
import org.jbehave.core.parser.ClasspathScenarioDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporter;

import com.lunivore.noughtsandcrosses.steps.BeforeAndAfterSteps;
import com.lunivore.noughtsandcrosses.steps.LolCatzSteps;
import com.lunivore.noughtsandcrosses.util.OAndXUniverse;

/**
 * Checks that we can support scenarios written in other languages,
 * eg: lolcatz
 */
public class PlayersCanHazTurns extends JUnitScenario {

    public PlayersCanHazTurns() {
    	this(new OAndXUniverse());
    }
    
    public PlayersCanHazTurns(OAndXUniverse universe) {
        super(new MostUsefulConfiguration() {
            public KeyWords keywords() {
                return LolCatzSteps.lolCatzKeywords();
            }
            public ClasspathScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternScenarioParser(this));
            }
            @Override
            public ScenarioReporter forReportingScenarios() {
                return new PrintStreamScenarioReporter();
            }
        }, new LolCatzSteps(universe), new BeforeAndAfterSteps(universe));
    }
    
}

