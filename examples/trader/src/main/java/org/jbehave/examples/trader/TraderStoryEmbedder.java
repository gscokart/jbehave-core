package org.jbehave.examples.trader;

import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.StoryEmbedder;
import org.jbehave.core.parser.LoadFromClasspath;
import org.jbehave.core.parser.PrefixCapturingPatternBuilder;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
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

/**
 * Specifies the StoryEmbedder for the Trader example, providing the
 * StoryConfiguration and the CandidateSteps.
 */
public class TraderStoryEmbedder extends StoryEmbedder {

    public TraderStoryEmbedder() {
    }

    @Override
    public StoryConfiguration configuration() {
        // start with default story configuration, overriding story definer and reporter
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();
        storyConfiguration.useStoryPathResolver(new UnderscoredCamelCaseResolver(".story"));
        // Using URLs to define stories
        storyConfiguration.useStoryLoader(new LoadFromClasspath(this.getClass().getClassLoader()));
        return storyConfiguration;
    }

    @Override
    protected StoryReporterBuilder.Format[] storyReporterFormats() {
        return new StoryReporterBuilder.Format[]{CONSOLE, TXT, HTML, XML};
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        // start with default steps configuration, overriding parameter converters, pattern builder and monitor
        StepsConfiguration stepsConfiguration = new MostUsefulStepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
        stepsConfiguration.useParameterConverters(new ParameterConverters(
                monitor, new TraderConverter(mockTradePersister())));  // define converter for custom type Trader
        stepsConfiguration.usePatternBuilder(new PrefixCapturingPatternBuilder("%")); // use '%' instead of '$' to identify parameters
        stepsConfiguration.useMonitor(monitor);
        return asList(new StepsFactory(stepsConfiguration).createCandidateSteps(new TraderSteps(new TradingService()), new BeforeAfterSteps()));
    }

    private TraderPersister mockTradePersister() {
        return new TraderPersister(new Trader("Mauro", asList(new Stock("STK1", 10.d))));
    }

}