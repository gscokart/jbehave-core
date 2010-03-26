package org.jbehave.examples.trader.i18n;

import java.util.Locale;

import org.jbehave.core.JUnitScenario;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.i18n.StringEncoder;
import org.jbehave.core.parser.ClasspathScenarioDefiner;
import org.jbehave.core.parser.PatternScenarioParser;
import org.jbehave.core.parser.ScenarioDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporter;

public class ItTraderScenario extends JUnitScenario {

	public ItTraderScenario() {
		this(Thread.currentThread().getContextClassLoader());
	}

	public ItTraderScenario(final ClassLoader classLoader) {
		super(new PropertyBasedConfiguration() {
			@Override
			public ScenarioDefiner forDefiningScenarios() {
				// use underscored camel case core files with extension ".scenario"
				return new ClasspathScenarioDefiner(
						new UnderscoredCamelCaseResolver(".scenario"),
						new PatternScenarioParser(keywords()), classLoader);
			}

			@Override
			public ScenarioReporter forReportingScenarios() {
				// report outcome in Italian (to System.out)
				return new PrintStreamScenarioReporter(keywordsFor(new Locale("it"), classLoader));
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
