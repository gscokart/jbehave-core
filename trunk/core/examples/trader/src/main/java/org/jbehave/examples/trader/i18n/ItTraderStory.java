package org.jbehave.examples.trader.i18n;

import java.util.Locale;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.i18n.StringEncoder;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.StoryDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;

public class ItTraderStory extends JUnitStory {

	public ItTraderStory() {
		this(Thread.currentThread().getContextClassLoader());
	}

	public ItTraderStory(final ClassLoader classLoader) {
		super(new PropertyBasedConfiguration() {
			@Override
			public StoryDefiner forDefiningStories() {
				// use underscored camel case stories with extension ".story"
				return new ClasspathStoryDefiner(
						new UnderscoredCamelCaseResolver(".story"),
						new PatternStoryParser(keywords()), classLoader);
			}

			@Override
			public StoryReporter forReportingStories() {
				// report outcome in Italian (to System.out)
				return new PrintStreamStoryReporter(keywordsFor(new Locale("it"), classLoader));
			}

			@Override
			public KeyWords keywords() {
				// use Italian for keywords
				return keywordsFor(new Locale("it"), classLoader);
			}

        }, new ItTraderSteps(classLoader));
	}

	protected static KeyWords keywordsFor(Locale locale, ClassLoader classLoader) {
		return new I18nKeyWords(locale, new StringEncoder(), "org/jbehave/examples/trader/i18n/keywords", classLoader);
	}

}
