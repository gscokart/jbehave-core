package org.jbehave.core;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.errors.InvalidRunnableStoryException;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.steps.CandidateSteps;

/**
 * <p>
 * Abstract implementation of RunnableStory which is primarily intended as a base
 * class for delegate implementations of RunnableStory. As such, it has no explicit
 * supports for any test framework, ie it requires the {@link RunnableStory#runStory()}
 * method to be invoked directly, and the class of the story being run needs
 * to be provided explicitly.
 * </p>
 * <p>
 * Typically, users will find it easier to extend decorator stories, such as
 * {@link JUnitStory} pr {@link JUnitStories} which also provide support for test frameworks and also
 * provide the story class pr story paths being implemented by the user.
 * </p>
 * <p/>
 * Whichever RunnableStory class one chooses to extends, the steps for running a
 * story are the same:
 * <ol>
 * <li>Extend the chosen RunnableStory class and name it after your story, eg
 * "ICanLogin.java" (note that there is no obligation to have the name of the
 * class end in "Story" although you may choose to).</li>
 * <li>The RunnableStory class should be in a matching text file in the same place,
 * eg "i_can_login" (this uses the default name resolution, although the it can
 * be configured via the {@link org.jbehave.core.parser.StoryNameResolver}).</li>
 * <li>Write some steps in your text story, starting each new step with
 * Given, When, Then or And. The keywords can be configured via the
 * {@link KeyWords} class, eg they can be translated/localized to other
 * languages.</li>
 * <li>Then move on to extending the Steps class and providing matching methods
 * for the steps defined in the text story.</li>
 * <ol>
 */
public abstract class AbstractStory implements RunnableStory {

    private final StoryRunner storyRunner;
    private StoryConfiguration configuration = new MostUsefulStoryConfiguration();
    private Class<? extends RunnableStory> storyClass;
    private List<String> storyPaths;    
    private List<CandidateSteps> candidateSteps = new ArrayList<CandidateSteps>();

    protected AbstractStory(StoryRunner storyRunner, Class<? extends RunnableStory> storyClass) {
        this.storyRunner = storyRunner;
        this.storyClass = storyClass;
    }

    protected AbstractStory(StoryRunner storyRunner, List<String> storyPaths) {
        this.storyRunner = storyRunner;
        this.storyPaths = storyPaths;
    }

    public void runStory() throws Throwable {
        if ( storyClass != null ){
            storyRunner.run(configuration, storyClass, candidateSteps());
        } else if ( storyPaths != null ){
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
