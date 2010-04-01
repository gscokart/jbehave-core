package org.jbehave.examples.trader.i18n;

import java.util.Locale;

import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedStoryConfiguration;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.i18n.StringEncoder;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.StoryDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

public class ItTraderStory extends JUnitStory {

    public ItTraderStory() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ItTraderStory(final ClassLoader classLoader) {
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration();
        // use Italian for keywords
        KeyWords keywords = keywordsFor(new Locale("it"), classLoader);
        storyConfiguration.useKeywords(keywords);
        storyConfiguration.useStoryDefiner(new ClasspathStoryDefiner(
                new UnderscoredCamelCaseResolver(".story"),
                new PatternStoryParser(storyConfiguration.keywords()), classLoader));
        storyConfiguration.useStoryReporter(new PrintStreamStoryReporter(storyConfiguration.keywords()));
        useConfiguration(storyConfiguration);

        StepsConfiguration stepsConfiguration = new StepsConfiguration();
        stepsConfiguration.useKeyWords(keywords);
        addSteps(new StepsFactory(stepsConfiguration).createCandidateSteps(new ItTraderSteps()));
    }

    protected static KeyWords keywordsFor(Locale locale, ClassLoader classLoader) {
        return new I18nKeyWords(locale, new StringEncoder(), "org/jbehave/examples/trader/i18n/keywords", classLoader);
    }

}
