package org.jbehave.examples.trader;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import java.text.MessageFormat;
import java.util.List;

import org.jbehave.core.JUnitStories;
import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.parser.LoadFromURL;
import org.jbehave.core.parser.PrefixCapturingPatternBuilder;
import org.jbehave.core.parser.StoryLocation;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.MostUsefulStepsConfiguration;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;
import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.examples.trader.service.TradingService;

public class TraderStories extends JUnitStories {

    public TraderStories() {
        // start with default story configuration, overriding story definer and reporter
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();
        storyConfiguration.useStoryPathResolver(new UnderscoredCamelCaseResolver(".story"));
        // Using URLs to define stories
        storyConfiguration.useStoryLoader(new LoadFromURL());
        List<String> storyPaths = storyPaths();
        Class<?> codeLocationClass = this.getClass();
        for ( String storyPath : storyPaths ){
			StoryLocation storyLocation = new StoryLocation(storyPath, codeLocationClass);
			StoryReporter storyReporter = new StoryReporterBuilder(new FilePrintStreamFactory(storyLocation))
            				.withDefaultFormats()
                            .withFormats(CONSOLE, TXT, HTML, XML)
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
        // Defining story paths via URLs
        return asList(storyURL("trader_is_alerted_of_status.story"),
                      storyURL("traders_can_be_subset.story"));
    }

    private String storyURL(String name){
        String codeLocation = new StoryLocation("", this.getClass()).getCodeLocation().getFile();
        String urlPattern = "file:"+ codeLocation +"org/jbehave/examples/trader/stories/{0}";
        return MessageFormat.format(urlPattern, name);

    }
}