package org.jbehave.examples.trader.i18n;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.FilePrintStreamFactory.ResolveToSimpleName;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;
import org.jbehave.core.steps.ParameterConverters.NumberConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.examples.trader.steps.BeforeAfterSteps;

/**
 * Abstract base ConfigurableEmbedder allowing localization of multiple stories
 * via JUnit. The concrete extension need only specify:
 * <ul>
 * <li>language: e.g. "fr", "it, "pt"</li>
 * <li>story pattern to look up stories</li>
 * <li>language-specific steps instance</li>
 * </ul>
 */
public abstract class LocalizedTraderStories extends JUnitStories {

    @Override
    public Configuration configuration() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL codeLocation = CodeLocations.codeLocationFromClass(this.getClass());        
        Keywords keywords = new LocalizedKeywords(locale());
        Properties properties = new Properties();
        properties.setProperty("reports", "ftl/jbehave-reports.ftl");
        Configuration configuration = new MostUsefulConfiguration()
                .useKeywords(keywords)
                .useStoryParser(new RegexStoryParser(keywords))
                .useStoryLoader(
                        new LoadFromClasspath(classLoader))
                .useDefaultStoryReporter(new ConsoleOutput(keywords))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                    .withCodeLocation(codeLocation)
                    .withPathResolver(new ResolveToSimpleName())
                    .withDefaultFormats()
                    .withFormats(CONSOLE, TXT, HTML, XML)
                    .withFailureTrace(false)
                    .withViewResources(properties)
                    .withKeywords(keywords))
                .useParameterConverters(
                        new ParameterConverters().addConverters(customConverters(keywords)));
        return configuration;
    }
    
    private ParameterConverter[] customConverters(Keywords keywords) {
        List<ParameterConverter> converters = new ArrayList<ParameterConverter>();
        converters.add(new NumberConverter(NumberFormat.getInstance(locale())));
        converters.add(new ExamplesTableConverter(
                    keywords.examplesTableHeaderSeparator(),
                    keywords.examplesTableValueSeparator()));
        return converters.toArray(new ParameterConverter[converters.size()]);
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        return new InstanceStepsFactory(configuration(), traderSteps(), new BeforeAfterSteps())
                .createCandidateSteps();
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), storyPattern(), "");
    }

    protected abstract Locale locale();
    
    protected abstract String storyPattern();
    
    protected abstract Object traderSteps();
    
}
