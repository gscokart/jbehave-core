package org.jbehave.core;

import org.jbehave.core.model.KeyWords;
import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.parser.*;
import org.jbehave.core.reporters.*;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.steps.DefaultStepdocGenerator;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepdocGenerator;
import org.jbehave.core.steps.UnmatchedToPendingStepCreator;

import java.util.Locale;

/**
 * The configuration that works for most situations that users are likely to encounter.
 * The elements configured are:
 * <ul>
 * <li>{@link KeyWords}: new I18nKeyWords()</li>
 * <li>{@link StepCreator}: new UnmatchedToPendingStepCreator()</li>
 * <li>{@link StoryParser}: new PatternStoryParser(keywords())</li>
 * <li>{@link StoryContentLoader}: new ClasspathLoader()</li> 
 * <li>{@link ErrorStrategy}: ErrorStrategy.RETHROW</li>
 * <li>{@link PendingErrorStrategy}: PendingErrorStrategy.PASSING</li>
 * <li>{@link StoryReporter}: new PassSilentlyDecorator(new PrintStreamStoryReporter())</li>
 * <li>{@link StepdocGenerator}: new DefaultStepdocGenerator()</li>
 * <li>{@link StepdocReporter}: new PrintStreamStepdocReporter(true)</li>
 * </ul>
 */
public class MostUsefulStoryConfiguration extends StoryConfiguration {

    public MostUsefulStoryConfiguration() {
        useKeywords(new I18nKeyWords(Locale.ENGLISH));
        useStepCreator(new UnmatchedToPendingStepCreator());
        useStoryParser(new PatternStoryParser(keywords()));
        useStoryLoader(new ClasspathLoading());
        useErrorStrategy(ErrorStrategy.RETHROW);
        usePendingErrorStrategy(PendingErrorStrategy.PASSING);
        useStoryReporter(new PassSilentlyDecorator(new PrintStreamStoryReporter()));
        useStepdocReporter(new PrintStreamStepdocReporter(true));
        useStepdocGenerator(new DefaultStepdocGenerator());
    }

}
