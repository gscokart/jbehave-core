package org.jbehave.core;

import org.junit.Test;

/**
 * <p>
 * {@link RunnableStoryDelegator} that runs a story via a single {@link RunnableStory} class.
 * </p>
 *
 * @see RunnableStoryDelegator
 */
public abstract class JUnitStory extends RunnableStoryDelegator {

    public JUnitStory() {
        this(new StoryRunner());
    }

    public JUnitStory(StoryRunner storyRunner) {
        delegateTo(new JUnitStoryDelegate(storyRunner, this.getClass()));
    }

    @Test
    public void runStory() throws Throwable {
        super.runStory();
    }

    public static class JUnitStoryDelegate extends AbstractStory {

        public JUnitStoryDelegate(StoryRunner storyRunner, Class<? extends RunnableStory> storyClass) {
            super(storyRunner, storyClass);
        }


    }

}
