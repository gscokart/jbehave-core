package org.jbehave.examples.trader.i18n;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.i18n.StringEncoder;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.steps.MostUsefulStepsConfiguration;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

import java.util.Locale;

public class ItTraderStory extends JUnitStory {

    public ItTraderStory() {
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();

        ClassLoader classLoader = this.getClass().getClassLoader();
        KeyWords keywords = new I18nKeyWords(new Locale("it"), new StringEncoder(), "org/jbehave/examples/trader/i18n/keywords", classLoader);
        // use Italian for keywords
        storyConfiguration.useKeywords(keywords);
        storyConfiguration.useStoryDefiner(new ClasspathStoryDefiner(
                new UnderscoredCamelCaseResolver(".story"),
                new PatternStoryParser(storyConfiguration.keywords()), classLoader));
        storyConfiguration.useStoryReporter(new PrintStreamStoryReporter(storyConfiguration.keywords()));
        useConfiguration(storyConfiguration);

        StepsConfiguration stepsConfiguration = new MostUsefulStepsConfiguration();
        // use Italian for keywords
        stepsConfiguration.useKeywords(keywords);
        addSteps(new StepsFactory(stepsConfiguration).createCandidateSteps(new ItTraderSteps()));
    }

}
