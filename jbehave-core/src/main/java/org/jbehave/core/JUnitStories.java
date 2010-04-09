package org.jbehave.core;

import org.junit.Test;

import java.util.List;

/**
 * <p>
 * {@link RunnableStoryDelegator} that runs multiple stories via their story paths.
 * </p>
 *
 * @see RunnableStoryDelegator
 */
public abstract class JUnitStories extends RunnableStoryDelegator {

    public JUnitStories() {
        this(new StoryRunner());
    }

    public JUnitStories(StoryRunner storyRunner) {
        delegateTo(new JUnitStoriesDelegate(storyRunner, storyPaths()));
    }

    @Test
    public void run() throws Throwable {
        super.run();
    }

    protected abstract List<String> storyPaths();

    public static class JUnitStoriesDelegate extends AbstractStory {

        public JUnitStoriesDelegate(StoryRunner storyRunner, List<String> storyPaths) {
            super(storyRunner, storyPaths);
        }


    }

}