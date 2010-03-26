package org.jbehave.core;

import java.util.List;

import junit.framework.TestCase;

import org.jbehave.core.steps.CandidateSteps;
import org.junit.Test;

/**
 * <p>
 * Story decorator that add supports for running stories as <a
 * href="http://junit.org">JUnit</a> tests. Both JUnit 4.x (via @Test
 * annotation) and JUnit 3.8.x (via TestCase inheritance) are supported.
 * </p>
 * <p>
 * Users requiring JUnit support will extends this class instead of
 * {@link AbstractStory}, while providing the same dependencies and following
 * the same specification logic as described in
 * {@link AbstractStory}. The only difference in the dependencies provided is
 * that the Story class is automatically set to the one being implemented by
 * the user, ie the concrete decorator class.
 * </p>
 * 
 * @see AbstractStory
 */
public abstract class JUnitStory extends TestCase implements RunnableStory {

    private final Class<? extends JUnitStory> decoratorClass = this.getClass();
    private final RunnableStory delegate;

    public JUnitStory(CandidateSteps... candidateSteps) {
        this.delegate = new JUnitStoryDelegate(decoratorClass, candidateSteps);
    }

    public JUnitStory(Configuration configuration, CandidateSteps... candidateSteps) {
        this.delegate = new JUnitStoryDelegate(decoratorClass, configuration, candidateSteps);
    }

    public JUnitStory(StoryRunner storyRunner, CandidateSteps... candidateSteps) {
        this.delegate = new JUnitStoryDelegate(decoratorClass, storyRunner, candidateSteps);
    }

    public JUnitStory(StoryRunner storyRunner, Configuration configuration, CandidateSteps... candidateSteps) {
        this.delegate = new JUnitStoryDelegate(decoratorClass, storyRunner, configuration, candidateSteps);
    }

    public JUnitStory(RunnableStory delegate) {
        this.delegate = delegate;
    }

    @Test
    public void runStory() throws Throwable {
        this.delegate.runStory();
    }
    
    public void useConfiguration(Configuration configuration) {
        this.delegate.useConfiguration(configuration);
    }
    
    public Configuration getConfiguration() {
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
     * A JUnit 3-compatibile runnable method which simply delegates
     * {@link RunnableStory#runStory()}
     * 
     * @throws Throwable
     */
    public void testStory() throws Throwable {
        runStory();
    }

    public static class JUnitStoryDelegate extends AbstractStory {

        public JUnitStoryDelegate(Class<? extends RunnableStory> decoratorClass, CandidateSteps... candidateSteps) {
            super(decoratorClass, candidateSteps);
        }

        public JUnitStoryDelegate(Class<? extends RunnableStory> decoratorClass, Configuration configuration,
                CandidateSteps... candidateSteps) {
            super(decoratorClass, configuration, candidateSteps);
        }

        public JUnitStoryDelegate(Class<? extends RunnableStory> decoratorClass, StoryRunner storyRunner,
                CandidateSteps... candidateSteps) {
            super(decoratorClass, storyRunner, candidateSteps);
        }

        public JUnitStoryDelegate(Class<? extends RunnableStory> decoratorClass, StoryRunner storyRunner,
                Configuration configuration, CandidateSteps... candidateSteps) {
            super(decoratorClass, storyRunner, configuration, candidateSteps);
        }

    }

}
