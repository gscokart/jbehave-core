package org.jbehave.core;

import java.util.List;

import junit.framework.TestCase;

import org.jbehave.core.steps.CandidateSteps;
import org.junit.Test;

/**
 * <p>
 * {@link RunnableStory} decorator that add supports for running stories as <a
 * href="http://junit.org">JUnit</a> tests. Both JUnit 4.x (via @Test
 * annotation) and JUnit 3.x (via TestCase inheritance) are supported.
 * </p>
 * <p>
 * Users requiring JUnit support will extends this class instead of
 * {@link AbstractStory}, while providing the same dependencies and following
 * the same specification logic as described in
 * {@link AbstractStory}. The only difference in the dependencies provided is
 * that the RunnableStory class is automatically set to the one being implemented by
 * the user, ie the concrete decorator class.
 * </p>
 *
 * @see AbstractStory
 */
public abstract class JUnitStory extends TestCase implements RunnableStory {

    private final Class<? extends JUnitStory> decoratorClass = this.getClass();
    private final RunnableStory delegate;

    public JUnitStory(){
        this(new StoryRunner());
    }
    
    public JUnitStory(StoryRunner storyRunner) {
        this.delegate = new JUnitStoryDelegate(decoratorClass, storyRunner);
    }

    @Test
    public void runStory() throws Throwable {
        this.delegate.runStory();
    }

    public void useConfiguration(StoryConfiguration configuration) {
        this.delegate.useConfiguration(configuration);
    }

    public StoryConfiguration getConfiguration() {
        return delegate.getConfiguration();
    }

    public void addSteps(CandidateSteps... steps) {
        this.delegate.addSteps(steps);
    }

    public List<CandidateSteps> getSteps() {
        return delegate.getSteps();
    }

    public void generateStepdoc() {
        this.delegate.generateStepdoc();
    }

    /**
     * A JUnit 3-compatible runnable method which simply delegates
     * {@link RunnableStory#runStory()}
     *
     * @throws Throwable
     */
    public void testStory() throws Throwable {
        runStory();
    }

    public static class JUnitStoryDelegate extends AbstractStory {

        public JUnitStoryDelegate(Class<? extends RunnableStory> decoratorClass, StoryRunner storyRunner) {
            super(decoratorClass, storyRunner);
        }


    }

}
