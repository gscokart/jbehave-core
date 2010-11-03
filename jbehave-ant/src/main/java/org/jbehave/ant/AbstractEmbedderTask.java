package org.jbehave.ant;

import static java.util.Arrays.asList;
import static org.apache.tools.ant.Project.MSG_DEBUG;
import static org.apache.tools.ant.Project.MSG_INFO;
import static org.apache.tools.ant.Project.MSG_WARN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.StringInputStream;
import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderClassLoader;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.EmbedderMonitor;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.UnmodifiableEmbedderControls;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.StoryMaps;
import org.jbehave.core.reporters.ReportsCount;

/**
 * Abstract task that holds all the configuration parameters to specify and load
 * stories.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractEmbedderTask extends Task {

    private static final String TEST_SCOPE = "test";

    private String sourceDirectory = "src/main/java";

    private String testSourceDirectory = "src/test/java";

    /**
     * The scope of the source, either "compile" or "test"
     */
    private String scope = "compile";

    /**
     * Include filters, relative to the root source directory determined by the
     * scope
     */
    private List<String> includes = new ArrayList<String>();

    /**
     * Exclude filters, relative to the root source directory determined by the
     * scope
     */
    private List<String> excludes = new ArrayList<String>();

    /**
     * The boolean flag to skip running stories
     */
    private boolean skip = false;

    /**
     * The boolean flag to ignore failure in stories
     */
    private boolean ignoreFailureInStories = false;

    /**
     * The boolean flag to ignore failure in view
     */
    private boolean ignoreFailureInView = false;

    /**
     * The boolean flag to generate view after stories
     */
    private boolean generateViewAfterStories = true;

    /**
     * The boolean flag to run in batch mode
     */
    private boolean batch = false;

    /**
     * The embedder to run the stories
     */
    private String embedderClass = Embedder.class.getName();

    /**
     * The class that is injected to provide embedder to run the stories.
     */
    private String injectableEmbedderClass;

    /**
     * The annotated embedder runner class to run the stories
     */
    protected String annotatedEmbedderRunnerClass = AnnotatedEmbedderRunner.class.getName();

    /**
     * Used to find story paths and class names
     */
    private String storyFinderClass = StoryFinder.class.getName();

    /**
     * The meta filters
     */
    private List<String> metaFilters = asList();

    /**
     * The system properties
     */
    private Properties systemProperties = new Properties();

    /**
     * The classloader
     */
    private EmbedderClassLoader classLoader;

    /**
     * Determines if the scope of the source directory is "test"
     * 
     * @return A boolean <code>true</code> if test scoped
     */
    private boolean isSourceTestScope() {
        return TEST_SCOPE.equals(scope);
    }

    String searchDirectory() {
        if (isSourceTestScope()) {
            return testSourceDirectory;
        }
        return sourceDirectory;
    }

    /**
     * Creates the EmbedderClassLoader with the classpath element of the
     * selected scope
     * 
     * @return A EmbedderClassLoader
     */
    protected EmbedderClassLoader classLoader() {
        if ( classLoader == null ){
            classLoader = new EmbedderClassLoader(this.getClass().getClassLoader());
        }
        return classLoader;
    }

    protected EmbedderMonitor embedderMonitor() {
        return new AntEmbedderMonitor();
    }

    protected EmbedderControls embedderControls() {
        return new UnmodifiableEmbedderControls(new EmbedderControls().doBatch(batch).doSkip(skip)
                .doGenerateViewAfterStories(generateViewAfterStories).doIgnoreFailureInStories(ignoreFailureInStories)
                .doIgnoreFailureInView(ignoreFailureInView));
    }

    /**
     * Finds story paths, using the {@link #newStoryFinder()}, in the
     * {@link #searchDirectory()} given specified {@link #includes} and
     * {@link #excludes}.
     * 
     * @return A List of story paths found
     */
    protected List<String> storyPaths() {
        log("Searching for story paths including " + includes + " and excluding " + excludes, MSG_DEBUG);
        List<String> storyPaths = newStoryFinder().findPaths(searchDirectory(), includes, excludes);
        log("Found story paths: " + storyPaths, MSG_INFO);
        return storyPaths;
    }

    /**
     * Finds class names, using the {@link #newStoryFinder()}, in the
     * {@link #searchDirectory()} given specified {@link #includes} and
     * {@link #excludes}.
     * 
     * @return A List of class names found
     */
    protected List<String> classNames() {
        log("Searching for class names including " + includes + " and excluding " + excludes, MSG_DEBUG);
        List<String> classNames = newStoryFinder().findClassNames(searchDirectory(), includes, excludes);
        log("Found class names : " + classNames, MSG_INFO);
        return classNames;
    }

    /**
     * Creates an instance of StoryFinder, using the {@link #storyFinderClass}
     * 
     * @return A StoryFinder
     */
    protected StoryFinder newStoryFinder() {
        return classLoader().newInstance(StoryFinder.class, storyFinderClass);
    }

    /**
     * Creates an instance of Embedder, either using
     * {@link #injectableEmbedderClass} (if set) or defaulting to
     * {@link #embedderClass}.
     * 
     * @return An Embedder
     */
    protected Embedder newEmbedder() {
        Embedder embedder = null;
        EmbedderClassLoader classLoader = classLoader();
        if (injectableEmbedderClass != null) {
            embedder = classLoader.newInstance(InjectableEmbedder.class, injectableEmbedderClass).injectedEmbedder();
        } else {
            embedder = classLoader.newInstance(Embedder.class, embedderClass);
        }
        embedder.useClassLoader(classLoader);
        embedder.useSystemProperties(systemProperties);
        EmbedderMonitor embedderMonitor = embedderMonitor();
        embedder.useEmbedderMonitor(embedderMonitor);
        if ( !metaFilters.isEmpty() ) {
            embedder.useMetaFilters(metaFilters);
        }
        embedder.useEmbedderControls(embedderControls());
        return embedder;
    }

    protected class AntEmbedderMonitor implements EmbedderMonitor {
        public void batchFailed(BatchFailures failures) {
            log("Failed to run batch " + failures, MSG_WARN);
        }

        public void embeddableFailed(String name, Throwable cause) {
            log("Failed to run embeddable " + name, cause, MSG_WARN);
        }

        public void embeddablesSkipped(List<String> classNames) {
            log("Skipped embeddables " + classNames, MSG_INFO);
        }

        public void metaNotAllowed(Meta meta, MetaFilter filter) {
            log(meta + " not allowed by filter '" + filter.asString() + "'", MSG_INFO);
        }

        public void runningEmbeddable(String name) {
            log("Running embeddable " + name, MSG_INFO);
        }

        public void storiesSkipped(List<String> storyPaths) {
            log("Skipped stories " + storyPaths, MSG_INFO);
        }

        public void storyFailed(String path, Throwable cause) {
            log("Failed to run story " + path, cause, MSG_WARN);
        }

        public void runningStory(String path) {
            log("Running story " + path, MSG_INFO);
        }

        public void annotatedInstanceNotOfType(Object annotatedInstance, Class<?> type) {
            log("Annotated instance " + annotatedInstance + " not of type " + type, MSG_WARN);
        }

        public void generatingReportsView(File outputDirectory, List<String> formats, Properties viewProperties) {
            log("Generating reports view to '" + outputDirectory + "' using formats '" + formats + "'"
                    + " and view properties '" + viewProperties + "'", MSG_INFO);
        }

        public void reportsViewGenerationFailed(File outputDirectory, List<String> formats, Properties viewProperties,
                Throwable cause) {
            log("Failed to generate reports view to '" + outputDirectory + "' using formats '" + formats + "'"
                    + " and view properties '" + viewProperties + "'", cause, MSG_WARN);
        }

        public void reportsViewGenerated(ReportsCount count) {
            log("Reports view generated with " + count.getStories() + " stories containing " + count.getScenarios() + " scenarios (of which  "
                    + count.getScenariosFailed() + " failed)", MSG_INFO);
            if (count.getStoriesNotAllowed() > 0 || count.getScenariosNotAllowed() > 0) {
                log("Meta filters did not allow " + count.getStoriesNotAllowed() + " stories and  " + count.getScenariosNotAllowed()
                        + " scenarios", MSG_INFO);
            }
        }

        public void reportsViewNotGenerated() {
            log("Reports view not generated", MSG_INFO);
        }

        public void mappingStory(String storyPath, List<String> metaFilters) {
            log("Mapping story "+storyPath+" with meta filters "+metaFilters, MSG_INFO);
        }


        public void generatingMapsView(File outputDirectory, StoryMaps storyMaps, Properties viewProperties) {
            log("Generating maps view to '" + outputDirectory + "' using story maps '" + storyMaps + "'"
                    + " and view properties '" + viewProperties + "'", MSG_INFO);
        }

        public void mapsViewGenerationFailed(File outputDirectory, StoryMaps storyMaps, Properties viewProperties,
                Throwable cause) {
            log("Generating maps view to '" + outputDirectory + "' using story maps '" + storyMaps + "'"
                    + " and view properties '" + viewProperties + "'", MSG_INFO);
        }

        public void processingSystemProperties(Properties properties) {
            log("Processing system properties " + properties, MSG_INFO);
        }
        
        public void systemPropertySet(String name, String value) {
            log("System property '" + name + "' set to '"+value+"'", MSG_INFO);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    // Setters used by Task to inject dependencies

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setTestSourceDirectory(String testSourceDirectory) {
        this.testSourceDirectory = testSourceDirectory;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setIncludes(String includesCSV) {
        this.includes = asList(includesCSV.split(","));
    }

    public void setExcludes(String excludesCSV) {
        this.excludes = asList(excludesCSV.split(","));
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void setIgnoreFailureInStories(boolean ignoreFailureInStories) {
        this.ignoreFailureInStories = ignoreFailureInStories;
    }

    public void setIgnoreFailureInView(boolean ignoreFailureInView) {
        this.ignoreFailureInView = ignoreFailureInView;
    }

    public void setGenerateViewAfterStories(boolean generateViewAfterStories) {
        this.generateViewAfterStories = generateViewAfterStories;
    }

    public void setEmbedderClass(String embedderClass) {
        this.embedderClass = embedderClass;
    }

    public void setInjectableEmbedderClass(String injectableEmbedderClass) {
        this.injectableEmbedderClass = injectableEmbedderClass;
    }

    public void setAnnotatedEmbedderRunnerClass(String annotatedEmbedderRunnerClass) {
        this.annotatedEmbedderRunnerClass = annotatedEmbedderRunnerClass;
    }

    public void setStoryFinderClass(String storyFinderClass) {
        this.storyFinderClass = storyFinderClass;
    }

    public void setMetaFilters(String metaFiltersCSV) {
        this.metaFilters = asList(metaFiltersCSV.split(","));
    }
    
    public void setSystemProperties(String systemPropertiesCSV){
        this.systemProperties = loadProperties(systemPropertiesCSV);
    }

    private Properties loadProperties(String systemPropertiesCSV) {
        Properties properties = new Properties();
        try {
            properties.load(new StringInputStream(systemPropertiesCSV.replace(",", "\n")));
        } catch (IOException e) {
            // return empty map
        }
        return properties;
    }

}
