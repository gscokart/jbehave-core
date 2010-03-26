package org.jbehave.examples.trader.scenarios;

import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.XML;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.parser.*;
import org.jbehave.examples.trader.PriorityMatchingSteps;
import org.jbehave.core.MostUsefulConfiguration;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.ScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporterBuilder;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

public class PriorityMatching extends JUnitStory {

    private static StoryNameResolver resolver = new UnderscoredCamelCaseResolver(".scenario");

    public PriorityMatching() {
        super(new MostUsefulConfiguration() {
            @Override
            public ScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(resolver, new PatternStoryParser(keywords()));
            }
            
            @Override
            public ScenarioReporter forReportingScenarios() {
                return new ScenarioReporterBuilder(new FilePrintStreamFactory(PriorityMatching.class, resolver))
                            .with(CONSOLE)
                            .with(TXT)
                            .with(HTML)
                            .with(XML)
                            .build();
            }

        });

        StepsConfiguration configuration = new StepsConfiguration();
        configuration.usePatternBuilder(new PrefixCapturingPatternBuilder("$")); 
        addSteps(new StepsFactory(configuration).createCandidateSteps(new PriorityMatchingSteps()));

    }

}
