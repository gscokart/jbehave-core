package org.jbehave.examples.trader;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import org.jbehave.core.parser.*;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.examples.trader.service.TradingService;
import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.RunnableStory;
import org.jbehave.core.parser.StoryNameResolver;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

public class TraderStory extends JUnitStory {

    private static StoryNameResolver resolver = new UnderscoredCamelCaseResolver(".scenario");

    public TraderStory(final Class<? extends RunnableStory> scenarioClass) {
        super(new PropertyBasedConfiguration() {
            @Override
            public StoryDefiner forDefiningStories() {
                return new ClasspathStoryDefiner(resolver, new PatternStoryParser(keywords()));
            }

            @Override
            public StoryReporter forReportingStories() {
                return new StoryReporterBuilder(new FilePrintStreamFactory(scenarioClass, resolver))
                            .with(CONSOLE)
                            .with(TXT)
                            .with(HTML)
                            .with(XML)
                            .build();
            }

        });

        StepsConfiguration configuration = new StepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
		configuration.useParameterConverters(new ParameterConverters(
        		monitor, new TraderConverter(mockTradePersister())));  // define converter for custom type Trader
        configuration.usePatternBuilder(new PrefixCapturingPatternBuilder("%")); // use '%' instead of '$' to identify parameters
        configuration.useMonitor(monitor);
        
        addSteps(createSteps(configuration));
    }

    protected CandidateSteps[] createSteps(StepsConfiguration configuration) {
        return new StepsFactory(configuration).createCandidateSteps(new TraderSteps(new TradingService()), new BeforeAfterSteps());
    }

    private TraderPersister mockTradePersister() {
        return new TraderPersister(new Trader("Mauro", asList(new Stock("STK1", 10.d))));
    }


}
