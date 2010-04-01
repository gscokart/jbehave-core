package org.jbehave.examples.trader;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import org.jbehave.core.*;
import org.jbehave.core.parser.*;
import org.jbehave.core.steps.*;
import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.examples.trader.service.TradingService;
import org.jbehave.core.parser.StoryNameResolver;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;

public class TraderStory extends JUnitStory {

    public TraderStory(final Class<? extends RunnableStory> scenarioClass) {
        // start with default story configuration, overriding story definer and reporter
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();
        StoryNameResolver nameResolver = new UnderscoredCamelCaseResolver(".story");
        storyConfiguration.useStoryDefiner(new ClasspathStoryDefiner(nameResolver, new PatternStoryParser(storyConfiguration.keywords()), this.getClass().getClassLoader()));
        storyConfiguration.useStoryReporter(new StoryReporterBuilder(new FilePrintStreamFactory(scenarioClass, nameResolver))
                .with(CONSOLE)
                .with(TXT)
                .with(HTML)
                .with(XML)
                .build());
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


}
