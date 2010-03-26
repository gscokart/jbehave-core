package org.jbehave.core;

import static org.jbehave.Ensure.ensureThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jbehave.core.steps.CandidateSteps;
import org.junit.Test;

public class RunnableStoryBehaviour {

    @Test
    public void shouldRunUsingTheStoryRunner() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        Configuration configuration = mock(Configuration.class);
        CandidateSteps steps = mock(CandidateSteps.class);
        Class<MyStory> scenarioClass = MyStory.class;

        // When
        RunnableStory story = new MyStory(runner, configuration, steps);
        story.runStory();

        // Then
        verify(runner).run(scenarioClass, configuration, steps);
    }
    
    @Test
    public void shouldAllowOverrideOfDefaultConfiguration() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        Configuration configuration = mock(Configuration.class);
        CandidateSteps steps = mock(CandidateSteps.class);
        Class<MyStory> scenarioClass = MyStory.class;

        // When
        RunnableStory story = new MyStory(runner, steps);
        ensureThat(story.getConfiguration() instanceof PropertyBasedConfiguration);
        story.useConfiguration(configuration);
        story.runStory();

        // Then
        ensureThat(!(story.getConfiguration() instanceof PropertyBasedConfiguration));
        verify(runner).run(scenarioClass, configuration, steps);
    }

    
    @Test
    public void shouldAllowAdditionOfSteps() throws Throwable {
        // Given
        StoryRunner runner = mock(StoryRunner.class);
        Configuration configuration = mock(Configuration.class);
        CandidateSteps steps = mock(CandidateSteps.class);
        Class<MyStory> scenarioClass = MyStory.class;

        // When
        RunnableStory story = new MyStory(runner, configuration);
        story.addSteps(steps);
        story.runStory();

        // Then
        verify(runner).run(scenarioClass, configuration, steps);
    }

    private class MyStory extends JUnitStory {

        public MyStory(StoryRunner runner, CandidateSteps... steps) {
            super(runner, steps);
        }

        public MyStory(StoryRunner runner, Configuration configuration, CandidateSteps... steps) {
            super(runner, configuration, steps);
        }

    }

}
