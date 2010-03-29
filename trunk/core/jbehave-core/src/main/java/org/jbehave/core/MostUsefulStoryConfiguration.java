package org.jbehave.core;

import org.jbehave.core.model.KeyWords;
import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.ErrorStrategyInWhichWeTrustTheReporter;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.StoryDefiner;
import org.jbehave.core.reporters.*;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.steps.DefaultStepdocGenerator;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepdocGenerator;
import org.jbehave.core.steps.UnmatchedToPendingStepCreator;

/**
 * The default configuration used by {@link StoryRunner}. Works for most
 * situations that users are likely to encounter. The default elements
 * configured are:
 * <ul>
 *   <li>{@link StepCreator}: new UnmatchedToPendingStepCreator()</li>
 *   <li>{@link org.jbehave.core.parser.StoryDefiner}: new ClasspathStoryDefiner(new PatternStoryParser(this))</li>
 *   <li>{@link ErrorStrategy}: ErrorStrategy.RETHROW</li>
 *   <li>{@link PendingErrorStrategy}: PendingErrorStrategy.PASSING</li>
 *   <li>{@link org.jbehave.core.reporters.StoryReporter}: new PassSilentlyDecorator(new PrintStreamStoryReporter())</li>
 *   <li>{@link KeyWords}: new I18nKeyWords()</li>
 *   <li>{@link StepdocGenerator}: new DefaultStepdocGenerator()</li>
 *   <li>{@link StepdocReporter}: new PrintStreamStepdocReporter(true)</li>
 * </ul>
 */
public class MostUsefulStoryConfiguration implements StoryConfiguration {

	/**
	 * Provides pending steps where unmatched steps exist.
	 */
	public StepCreator forCreatingSteps() {
		return new UnmatchedToPendingStepCreator();
	}

	/**
	 * Defines stories by looking for a file named after the core and in
	 * the same package, using lower-case underscored name in place of the
	 * camel-cased name - so MyStory.java maps to my_story.
	 */
	public StoryDefiner forDefiningStories() {
		return new ClasspathStoryDefiner(new PatternStoryParser(keywords()));
	}

	/**
	 * Handles errors by rethrowing them.
	 * 
	 * <p>
	 * If there are multiple stories in a single story model, this could
	 * cause the story to stop after the first failing core.
	 * 
	 * <p>
	 * If you want different behaviour, you might want to look at the
	 * {@link ErrorStrategyInWhichWeTrustTheReporter}.
	 */
	public ErrorStrategy forHandlingErrors() {
		return ErrorStrategy.RETHROW;
	}

	/**
	 * Allows pending steps to pass, so that builds etc. will not fail.
	 * 
	 * <p>
	 * If you want to spot pending steps, you might want to look at
	 * {@link PendingErrorStrategy.FAILING}, or alternatively at the
	 * PropertyBasedStoryConfiguration which provides a mechanism for altering this
	 * behaviour in different environments.
	 */
	public PendingErrorStrategy forPendingSteps() {
		return PendingErrorStrategy.PASSING;
	}

	/**
	 * Reports failing or pending stories to System.out, while silently
	 * passing stories.
	 * 
	 * <p>
	 * If you want different behaviour, you might like to use the
	 * {@link org.jbehave.core.reporters.PrintStreamStoryReporter}, or look at the {@link PropertyBasedStoryConfiguration}
	 * which provides a mechanism for altering this behaviour in different
	 * environments.
	 */
	public StoryReporter forReportingStories() {
		return new PassSilentlyDecorator(new PrintStreamStoryReporter());
	}

	/**
	 * Provides the keywords in English
	 */
	public KeyWords keywords() {
		return new I18nKeyWords();
	}

	/**
	 * Generates stepdocs
	 */
	public StepdocGenerator forGeneratingStepdoc() {
		return new DefaultStepdocGenerator();
	}

	/**
	 * Reports stepdocs to {@link System.out}
	 */
	public StepdocReporter forReportingStepdoc() {
		return new PrintStreamStepdocReporter(true);
	}

}
