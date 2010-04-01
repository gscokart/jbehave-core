package org.jbehave.core;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.errors.InvalidRunnableStoryException;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.steps.CandidateSteps;

/**
 * <p>
 * Abstract implementation of RunnableStory which is intended as a base
 * class for delegate implementations of RunnableStory. As such, it has no explicit
 * supports for any test framework, i.e. it requires the {@link RunnableStory#runStory()}
 * method to be invoked directly, and the class of the story being run needs
 * to be provided explicitly.  The {@link RunnableStory#runStory()} method then
 * uses the {@link StoryRunner} to run the story or stories, using the provided
 * {@link StoryConfiguration} and the {@link CandidateSteps}.
 * </p>
 * <p>
 * Typically, users will find it easier to extend decorator stories, such as
 * {@link JUnitStory} or {@link JUnitStories} which also provide support for test frameworks
 * and also provide the story class or story paths being implemented by the user.
 * </p>
 */
public abstract class AbstractStory implements RunnableStory {

    private final StoryRunner storyRunner;
    private StoryConfiguration configuration = new MostUsefulStoryConfiguration();
    private Class<? extends RunnableStory> storyClass;
    private List<String> storyPaths;
    private List<CandidateSteps> candidateSteps = new ArrayList<CandidateSteps>();

    protected AbstractStory() {
        this.storyRunner = new StoryRunner();
    }

    protected AbstractStory(StoryRunner storyRunner, Class<? extends RunnableStory> storyClass) {
        this.storyRunner = storyRunner;
        this.storyClass = storyClass;
    }

    protected AbstractStory(StoryRunner storyRunner, List<String> storyPaths) {
        this.storyRunner = storyRunner;
        this.storyPaths = storyPaths;
    }

    public void runStory() throws Throwable {
        if (storyClass != null) {
            storyRunner.run(configuration, storyClass, candidateSteps());
        } else if (storyPaths != null) {
            storyRunner.run(configuration, storyPaths, candidateSteps());
        } else {
            throw new InvalidRunnableStoryException("Either a RunnableStory class or a list of story paths must be provided");
        }
    }

    public void useConfiguration(StoryConfiguration configuration) {
        this.configuration = configuration;
    }

    public StoryConfiguration getConfiguration() {
        return configuration;
    }

    public void addSteps(CandidateSteps... steps) {
        this.candidateSteps.addAll(asList(steps));
    }

    public List<CandidateSteps> getSteps() {
        return candidateSteps;
    }

    public void generateStepdoc() {
        configuration.stepdocReporter().report(configuration.stepdocGenerator().generate(candidateSteps()));
    }

    private CandidateSteps[] candidateSteps() {
        return candidateSteps.toArray(new CandidateSteps[candidateSteps.size()]);
    }
}
