package org.jbehave.examples.trader.stories;

import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.parser.*;
import org.jbehave.core.steps.MostUsefulStepsConfiguration;
import org.jbehave.examples.trader.PriorityMatchingSteps;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

public class PriorityMatching extends JUnitStory {

    public PriorityMatching() {
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();
        storyConfiguration.useStoryPathResolver(new UnderscoredCamelCaseResolver(".story"));        
        storyConfiguration.useStoryReporter(new StoryReporterBuilder(new FilePrintStreamFactory(storyConfiguration.storyPathResolver().resolve(PriorityMatching.class)))
                .with(CONSOLE)
                .with(TXT)
                .with(HTML)
                .with(XML)
                .build());
        useConfiguration(storyConfiguration);

        StepsConfiguration stepsConfiguration = new MostUsefulStepsConfiguration();
        stepsConfiguration.usePatternBuilder(new PrefixCapturingPatternBuilder("$"));
        addSteps(new StepsFactory(stepsConfiguration).createCandidateSteps(new PriorityMatchingSteps()));

    }

}
