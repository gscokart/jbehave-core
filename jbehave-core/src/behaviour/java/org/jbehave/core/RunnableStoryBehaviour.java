package org.jbehave.core;

import org.hamcrest.Matchers;
import org.jbehave.core.errors.InvalidRunnableStoryException;
import org.jbehave.core.steps.CandidateSteps;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.Ensure.ensureThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RunnableStoryBehaviour {

    @Test
    public void shouldRunASingleStoryViaClass() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        StoryConfiguration configuration = mock(StoryConfiguration.class);
        CandidateSteps steps = mock(CandidateSteps.class);
        Class<MyStory> storyClass = MyStory.class;

        // When
        RunnableStory story = new MyStory(runner, configuration, steps);
        story.run();

        // Then
        verify(runner).run(configuration, asList(steps), storyClass);
    }


    @Test
    public void shouldRunMultipleStoriesViaPaths() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        StoryConfiguration configuration = mock(StoryConfiguration.class);
        CandidateSteps steps = mock(CandidateSteps.class);

        // When
        MyStories story = new MyStories(runner, configuration, steps);
        story.run();

        // Then
        verify(runner).run(configuration, asList(steps), story.storyPaths());
    }

    @Test(expected= InvalidRunnableStoryException.class)
    public void shouldFailIfNotAllParametersAreProvidedToStoryRunner() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        StoryConfiguration configuration = mock(StoryConfiguration.class);
        CandidateSteps steps = mock(CandidateSteps.class);

        // When
        MyInvalidStories story = new MyInvalidStories(runner, configuration, steps);
        story.run();

    }


    @Test(expected= InvalidRunnableStoryException.class)
    public void shouldFailIfUsingInvalidDelegator() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        StoryConfiguration configuration = mock(StoryConfiguration.class);
        CandidateSteps steps = mock(CandidateSteps.class);

        // When
        MyInvalidDelegator story = new MyInvalidDelegator();
        story.run();

    }

    @Test
    public void shouldAllowOverrideOfDefaultConfiguration() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        StoryConfiguration configuration = mock(StoryConfiguration.class);
        CandidateSteps steps = mock(CandidateSteps.class);
        Class<MyStory> storyClass = MyStory.class;

        // When
        RunnableStory story = new MyStory(runner, steps);
        ensureThat(story.getConfiguration(), Matchers.not(Matchers.sameInstance(configuration)));
        story.useConfiguration(configuration);
        story.run();

        // Then
        ensureThat(!(story.getConfiguration() instanceof PropertyBasedStoryConfiguration));
        verify(runner).run(configuration,  asList(steps), storyClass);
    }


    @Test
    public void shouldAllowAdditionOfSteps() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        StoryConfiguration configuration = mock(StoryConfiguration.class);
        CandidateSteps steps = mock(CandidateSteps.class);
        Class<MyStory> storyClass = MyStory.class;

        // When
        RunnableStory story = new MyStory(runner, configuration);
        story.addSteps(steps);
        story.run();

        // Then
        verify(runner).run(configuration,  asList(steps), storyClass);
    }

    private class MyStory extends JUnitStory {

        public MyStory(StoryRunner runner, CandidateSteps... steps) {
            super(runner);
            addSteps(steps);
        }

        public MyStory(StoryRunner runner, StoryConfiguration configuration, CandidateSteps... steps) {
            super(runner);
            useConfiguration(configuration);
            addSteps(steps);
        }

    }

    private class MyStories extends JUnitStories {


        public MyStories(StoryRunner runner, StoryConfiguration configuration, CandidateSteps... steps) {
            super(runner);
            useConfiguration(configuration);
            addSteps(steps);
        }

        @Override
        protected List<String> storyPaths() {
            return asList("org/jbehave/core/story1", "org/jbehave/core/story2");
        }
    }


    private class MyInvalidStories extends JUnitStories {


        public MyInvalidStories(StoryRunner runner, StoryConfiguration configuration, CandidateSteps... steps) {
            super(runner);
            useConfiguration(configuration);
            addSteps(steps);
        }

        @Override
        protected List<String> storyPaths() {
            return null;
        }
    }

    private class MyInvalidDelegator extends RunnableStoryDelegator {
        // not delegating to anything, defaults to no-op impl
    }

}
