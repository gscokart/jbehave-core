package org.jbehave.core;

import org.jbehave.core.steps.CandidateSteps;

import java.util.List;

/**
 * <p>
 * Delegates {@link RunnableStory} functionality to the delegate provided by
 * the concrete subclasses.  A default no-op implementation is given as default.
 * </p>
 * <p>
 * Note that we cannot use a constructor as some delegates, e.g. {@JUnitStory} can
 * only be provided after the super() constructor has been invoked.
 * </p>
 *
 * @see org.jbehave.core.RunnableStory
 */
public abstract class RunnableStoryDelegator implements RunnableStory {

    // Delegate must be provided by concrete subclasses
    // No-op impl will fail with InvalidRunnableStoryException
    private RunnableStory delegate = new AbstractStory(){};

    protected void delegateTo(RunnableStory delegate) {
        this.delegate = delegate;
    }

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

}