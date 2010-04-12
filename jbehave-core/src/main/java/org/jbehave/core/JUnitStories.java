package org.jbehave.core;

import org.jbehave.core.steps.CandidateSteps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * <p>
 * JUnit-runnable entry-point to run a single story specified by a {@link RunnableStory} class.
 * </p>
 */
public abstract class JUnitStories extends AbstractStory {

    @Test
    public void run() throws Throwable {
        StoryEmbedder embedder = storyEmbedder();
        embedder.runStoriesAsPaths(storyPaths());
    }

    protected abstract List<String> storyPaths();

}