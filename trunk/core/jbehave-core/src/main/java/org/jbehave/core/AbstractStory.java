package org.jbehave.core;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.model.KeyWords;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.Stepdoc;

/**
 * <p>
 * Abstract implementation of RunnableStory which is primarily intended as a base
 * class for delegate implementations of RunnableStory. As such, it has no explicit
 * supports for any test framework, ie it requires the {@link org.jbehave.core.RunnableStory#runStory()}
 * method to be invoked directly, and the class of the story being run needs
 * to be provided explicitly.
 * </p>
 * <p>
 * Typically, users will find it easier to extend decorator stories, such as
 * {@link JUnitStory} which also provide support for test frameworks and also
 * provide the story class as the one being implemented by the user.
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

    private StoryConfiguration configuration;
    private final StoryRunner storyRunner;
    private final List<CandidateSteps> candidateSteps = new ArrayList<CandidateSteps>();
    private final Class<? extends RunnableStory> storyClass;

    public AbstractStory(Class<? extends RunnableStory> storyClass, StoryRunner storyRunner) {
        this.storyClass = storyClass;
        this.configuration = configuration;
        this.storyRunner = storyRunner;
    }

    public void runStory() throws Throwable {
        CandidateSteps[] steps = candidateSteps.toArray(new CandidateSteps[candidateSteps.size()]);
        storyRunner.run(storyClass, configuration, steps);
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
        CandidateSteps[] steps = candidateSteps.toArray(new CandidateSteps[candidateSteps.size()]);
        List<Stepdoc> stepdocs = configuration.forGeneratingStepdoc().generate(steps);
        configuration.forReportingStepdoc().report(stepdocs);
    }

}
