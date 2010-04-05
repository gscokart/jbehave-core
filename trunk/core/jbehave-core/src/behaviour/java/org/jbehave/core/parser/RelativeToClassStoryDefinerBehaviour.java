package org.jbehave.core.parser;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;
import org.jbehave.core.parser.stories.MyPendingStory;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RelativeToClassStoryDefinerBehaviour {

    @Test
    public void canDefineStory() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        String storyPath = "org/jbehave/core/parser/stories/MyPendingStory.txt";
        String storyAsString = "Given my step";
        when(parser.parseStory(storyAsString, storyPath)).thenReturn(story);

        // When
        StoryDefiner definer = new RelativeToClassStoryDefiner(parser, MyPendingStory.class, "../../src/behaviour/java");
        Story definedStory = definer.defineStory(storyPath);
        assertSame(story, definedStory);

        // Then
        verify(story).namedAs(new File(storyPath).getName());
    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryForInexistentResource() {
        StoryDefiner definer = new RelativeToClassStoryDefiner(MyPendingStory.class);
        definer.defineStory("inexistent.story");
    }

}