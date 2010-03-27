package com.lunivore.noughtsandcrosses;

import org.jbehave.core.MostUsefulConfiguration;
import org.jbehave.core.JUnitStory;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;

import com.lunivore.noughtsandcrosses.steps.BeforeAndAfterSteps;
import com.lunivore.noughtsandcrosses.steps.LolCatzSteps;
import com.lunivore.noughtsandcrosses.util.OAndXUniverse;

/**
 * Checks that we can support stories written in other languages,
 * eg: lolcatz
 */
public class PlayersCanHazTurns extends JUnitStory {

    public PlayersCanHazTurns() {
    	this(new OAndXUniverse());
    }
    
    public PlayersCanHazTurns(OAndXUniverse universe) {
        super(new MostUsefulConfiguration() {
            public KeyWords keywords() {
                return LolCatzSteps.lolCatzKeywords();
            }
            public ClasspathStoryDefiner forDefiningScenarios() {
                return new ClasspathStoryDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(this));
            }
            @Override
            public StoryReporter forReportingScenarios() {
                return new PrintStreamStoryReporter();
            }
        }, new LolCatzSteps(universe), new BeforeAndAfterSteps(universe));
    }
    
}

