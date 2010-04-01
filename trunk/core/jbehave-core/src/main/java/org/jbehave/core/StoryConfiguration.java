package org.jbehave.core;

import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.StoryDefiner;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.DefaultStepdocGenerator;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepdocGenerator;
import org.jbehave.core.steps.UnmatchedToPendingStepCreator;

import java.util.Locale;

/**
 * Provides the story configuration used by the {@link StoryRunner} and the
 * in the {@link RunnableStory} implementations to customise its runtime properties.
 * <p/>
 * <p>
 * StoryConfiguration dependencies can be provided either via constructor or via
 * use* methods, which override the the default values of the
 * dependency, which is always set. The use methods allow to
 * override the dependencies one by one and play nicer with a Java hierarchical
 * structure, in that does allow the use of non-static member variables.
 * </p>
 *
 * @author Elizabeth Keogh
 * @author Mauro Talevi
 */
public abstract class StoryConfiguration {

    /**
     * Use English language for keywords
     */
    private KeyWords keywords = new I18nKeyWords(Locale.ENGLISH);
    /**
     * Provides pending steps where unmatched steps exist.
     */
    private StepCreator stepCreator = new UnmatchedToPendingStepCreator();
    /**
     * Defines stories by looking for a file named after the core and in
     * the same package, using lower-case underscored name in place of the
     * camel-cased name - so MyStory.java maps to my_story.
     */
    private StoryDefiner storyDefiner = new ClasspathStoryDefiner(new PatternStoryParser(keywords));
    /**
     * Handles errors by re-throwing them.
     * <p/>
     * If there are multiple scenarios in a single story, this could
     * cause the story to stop after the first failing scenario.
     * <p/>
     * Users wanting a different behaviour may use
     * {@link org.jbehave.core.errors.ErrorStrategyInWhichWeTrustTheReporter}.
     */
    private ErrorStrategy errorStrategy = ErrorStrategy.RETHROW;
    /**
     * Allows pending steps to pass, so that steps that to do not match any method will not
     * cause failure.
     * <p/>
     * Uses wanting a stricter behaviour for pending steps may use
     * {@link org.jbehave.core.errors.Pending   ErrorStrategy.FAILING}.
     */
    private PendingErrorStrategy pendingErrorStrategy = PendingErrorStrategy.PASSING;
    /**
     * Reports failing or pending stories to System.out, while silently
     * passing stories.
     */
    private StoryReporter storyReporter = new PassSilentlyDecorator(new PrintStreamStoryReporter());
    /**
     * Use default stepdoc generator
     */
    private StepdocGenerator stepdocGenerator = new DefaultStepdocGenerator();
    /**
     * Reports stepdocs to System.out, while reporting methods
     */
    private StepdocReporter stepdocReporter = new PrintStreamStepdocReporter(System.out, true);

    /**
     * Default no-op constructor, uses the default instances defined for member variables.
     */
    protected StoryConfiguration() {
    }

    /**
     * Constructor that allows all dependencies to be injected
     *
     * @param keywords
     * @param stepCreator
     * @param storyDefiner
     * @param errorStrategy
     * @param stepdocReporter
     * @param stepdocGenerator
     * @param storyReporter
     * @param pendingErrorStrategy
     */
    protected StoryConfiguration(KeyWords keywords, StepCreator stepCreator, StoryDefiner storyDefiner, ErrorStrategy errorStrategy, StepdocReporter stepdocReporter, StepdocGenerator stepdocGenerator, StoryReporter storyReporter, PendingErrorStrategy pendingErrorStrategy) {
        this.keywords = keywords;
        this.stepCreator = stepCreator;
        this.storyDefiner = storyDefiner;
        this.errorStrategy = errorStrategy;
        this.stepdocReporter = stepdocReporter;
        this.stepdocGenerator = stepdocGenerator;
        this.storyReporter = storyReporter;
        this.pendingErrorStrategy = pendingErrorStrategy;
    }


    public StepCreator stepCreator() {
        return stepCreator;
    }

    public StoryDefiner storyDefiner() {
        return storyDefiner;
    }


    public ErrorStrategy errorStrategy() {
        return errorStrategy;
    }

    public PendingErrorStrategy pendingErrorStrategy() {
        return pendingErrorStrategy;
    }

    public StoryReporter storyReporter() {
        return storyReporter;
    }

    public KeyWords keywords() {
        return keywords;
    }

    public StepdocGenerator stepdocGenerator() {
        return stepdocGenerator;
    }

    public StepdocReporter stepdocReporter() {
        return stepdocReporter;
    }

    public void useKeywords(KeyWords keywords) {
        this.keywords = keywords;
    }

    public void useStepCreator(StepCreator stepCreator) {
        this.stepCreator = stepCreator;
    }

    public void usePendingErrorStrategy(PendingErrorStrategy pendingErrorStrategy) {
        this.pendingErrorStrategy = pendingErrorStrategy;
    }

    public void useErrorStrategy(ErrorStrategy errorStrategy) {
        this.errorStrategy = errorStrategy;
    }

    public void useStoryDefiner(StoryDefiner storyDefiner) {
        this.storyDefiner = storyDefiner;
    }

    public void useStoryReporter(StoryReporter storyReporter) {
        this.storyReporter = storyReporter;
    }

    public void useStepdocReporter(StepdocReporter stepdocReporter) {
        this.stepdocReporter = stepdocReporter;
    }

    public void useStepdocGenerator(StepdocGenerator stepdocGenerator) {
        this.stepdocGenerator = stepdocGenerator;
    }
}
