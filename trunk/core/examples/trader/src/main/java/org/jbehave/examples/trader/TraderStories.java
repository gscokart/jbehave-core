package org.jbehave.examples.trader;

import org.jbehave.core.JUnitStories;
import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.parser.*;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.*;
import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.examples.trader.service.TradingService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.*;

public class TraderStories extends JUnitStories {

    public TraderStories() {
        // start with default story configuration, overriding story definer and reporter
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();
        StoryNameResolver nameResolver = new UnderscoredCamelCaseResolver(".story");
        storyConfiguration.useStoryDefiner(new ClasspathStoryDefiner(nameResolver, new PatternStoryParser(storyConfiguration.keywords()), this.getClass().getClassLoader()));
        List<String> storyPaths = storyPaths();
        for ( String storyPath : storyPaths ){
            StoryReporter storyReporter = new StoryReporterBuilder(new FilePrintStreamFactory(storyPath))
                            .with(CONSOLE)
                            .with(TXT)
                            .with(HTML)
                            .with(XML)
                            .build();
            storyConfiguration.addStoryReporter(storyPath, storyReporter);

        }
        useConfiguration(storyConfiguration);

        // start with default steps configuration, overriding parameter converters, pattern builder and monitor
        StepsConfiguration stepsConfiguration = new MostUsefulStepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
        stepsConfiguration.useParameterConverters(new ParameterConverters(
                monitor, new TraderConverter(mockTradePersister())));  // define converter for custom type Trader
        stepsConfiguration.usePatternBuilder(new PrefixCapturingPatternBuilder("%")); // use '%' instead of '$' to identify parameters
        stepsConfiguration.useMonitor(monitor);
        addSteps(createSteps(stepsConfiguration));
    }

    protected CandidateSteps[] createSteps(StepsConfiguration configuration) {
        return new StepsFactory(configuration).createCandidateSteps(new TraderSteps(new TradingService()), new BeforeAfterSteps());
    }

    private TraderPersister mockTradePersister() {
        return new TraderPersister(new Trader("Mauro", asList(new Stock("STK1", 10.d))));
    }


    @Override
    protected List<String> storyPaths() {
        return asList("org/jbehave/examples/trader/stories/trader_is_alerted_of_status.story",
                      "org/jbehave/examples/trader/stories/trader_sells_all_stocks.story");
    }
}