package org.jbehave.core;

import org.jbehave.core.steps.CandidateSteps;

import java.util.List;

/**
 * Delegates {@link RunnableStory} functionality to the delegate provided.
 *
 * @see org.jbehave.core.RunnableStory
 */
public abstract class RunnableStoryDelegator implements RunnableStory {

    private RunnableStory delegate;

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