package org.jbehave.core;

import org.jbehave.core.errors.InvalidRunnableStoryException;
import org.jbehave.core.steps.CandidateSteps;

import java.util.List;

/**
 * <p>
 * Delegates {@link RunnableStory} functionality to a delegate specified in a
 * post-instantiation call to delegateTo(..) 
 * </p>
 * <p>
 * Note that we cannot use a constructor as some delegates, e.g. {@JUnitStory} can
 * only be provided after the super() constructor has been invoked.
 * </p>
 *
 * @see org.jbehave.core.RunnableStory
 */
public abstract class RunnableStoryDelegator implements RunnableStory {


    private RunnableStory delegate;

    protected void delegateTo(RunnableStory delegate) {
        this.delegate = delegate;
    }

    private RunnableStory getDelegate() {
        if (delegate == null) {
            throw new InvalidRunnableStoryException("Use delegateTo(..) to specify a delegate Runnable Story");
        }
        return delegate;
    }

    public void runStory() throws Throwable {
        this.getDelegate().runStory();
    }

    public void useConfiguration(StoryConfiguration configuration) {
        this.getDelegate().useConfiguration(configuration);
    }

    public StoryConfiguration getConfiguration() {
        return getDelegate().getConfiguration();
    }

    public void addSteps(CandidateSteps... steps) {
        this.getDelegate().addSteps(steps);
    }

    public List<CandidateSteps> getSteps() {
        return getDelegate().getSteps();
    }

    public void generateStepdoc() {
        this.getDelegate().generateStepdoc();
    }

}