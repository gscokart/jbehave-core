package org.jbehave.core;

import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.parser.*;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.DefaultStepdocGenerator;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepdocGenerator;
import org.jbehave.core.steps.UnmatchedToPendingStepCreator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <p>
 * Provides the story configuration used by the {@link StoryRunner} and the
 * in the {@link RunnableStory} implementations to customise its runtime properties.
 * </p>
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
     * Parses the textual representation via pattern matching of keywords
     */
    private StoryParser storyParser = new PatternStoryParser(keywords);
    /**
     * Loads story content from classpath
     */
    private StoryLoader storyLoader = new LoadFromClasspath();
    /**
     * Resolves story paths from classes
     */
    private StoryPathResolver storyPathResolver = new UnderscoredCamelCaseResolver();
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
     * {@link org.jbehave.core.errors.PendingErrorStrategy.FAILING}.
     */
    private PendingErrorStrategy pendingErrorStrategy = PendingErrorStrategy.PASSING;
    /**
     * Reports failing or pending stories to System.out, while silently
     * passing stories.
     */
    private StoryReporter storyReporter = new SilentSuccessFilter(new PrintStreamStoryReporter());
    /**
     * Collects story reporters by story path 
     */
    private Map<String, StoryReporter> storyReporters = new HashMap<String,  StoryReporter>();
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
     * @param storyParser
     * @param storyLoader
     * @param storyPathResolver
     * @param errorStrategy
     * @param stepdocReporter
     * @param stepdocGenerator
     * @param storyReporter
     * @param pendingErrorStrategy
     */
    protected StoryConfiguration(KeyWords keywords, StepCreator stepCreator, StoryParser storyParser, StoryLoader storyLoader, StoryPathResolver storyPathResolver, ErrorStrategy errorStrategy, StepdocReporter stepdocReporter, StepdocGenerator stepdocGenerator, StoryReporter storyReporter, PendingErrorStrategy pendingErrorStrategy) {
        this.keywords = keywords;
        this.stepCreator = stepCreator;
        this.storyParser = storyParser;
        this.storyLoader = storyLoader;
        this.storyPathResolver = storyPathResolver;
        this.errorStrategy = errorStrategy;
        this.stepdocReporter = stepdocReporter;
        this.stepdocGenerator = stepdocGenerator;
        this.storyReporter = storyReporter;
        this.pendingErrorStrategy = pendingErrorStrategy;
    }


    public StepCreator stepCreator() {
        return stepCreator;
    }

    public StoryParser storyParser() {
        return storyParser;
    }

    public StoryLoader storyLoader(){
        return storyLoader;
    }
    
    public StoryPathResolver storyPathResolver(){
        return storyPathResolver;
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

    public StoryReporter storyReporter(String storyPath) {
        StoryReporter storyReporter = storyReporters.get(storyPath);
        if (storyReporter != null ){
            return storyReporter;
        }
        // default to configured story reporter
        // TODO consider merging the two methods
        return storyReporter();
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

    public void useStoryParser(StoryParser storyParser) {
        this.storyParser = storyParser;
    }

    public void useStoryLoader(StoryLoader storyLoader){
        this.storyLoader = storyLoader;
    }

    public void useStoryPathResolver(StoryPathResolver storyPathResolver) {
        this.storyPathResolver = storyPathResolver;
    }

    public void useStoryReporter(StoryReporter storyReporter) {
        this.storyReporter = storyReporter;
    }
    
    public void addStoryReporter(String storyPath, StoryReporter storyReporter){
        this.storyReporters.put(storyPath, storyReporter);
    }

    public void useStepdocReporter(StepdocReporter stepdocReporter) {
        this.stepdocReporter = stepdocReporter;
    }

    public void useStepdocGenerator(StepdocGenerator stepdocGenerator) {
        this.stepdocGenerator = stepdocGenerator;
    }
}
