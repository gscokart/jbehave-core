package org.jbehave.core.parser;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.errors.StoryNotFoundException;
import org.jbehave.core.model.Story;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.*;

public class URLStoryDefinerBehaviour {

    @Test
    public void canDefineStory() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        String codeLocation = new StoryLocation("", this.getClass()).getCodeLocation();
        String storyPath = "file:" + codeLocation + "org/jbehave/core/parser/stories/my_pending_story";
        String storyAsString = "Given my step";
        when(parser.defineStoryFrom(storyAsString, storyPath)).thenReturn(story);

        // When
        StoryDefiner definer = new URLStoryDefiner(parser);
        definer.defineStory(storyPath);

        // Then       
        verify(story).namedAs(new File(storyPath).getName());
    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryForInexistentResource() {
        // Given
        String codeLocation = new StoryLocation("", this.getClass()).getCodeLocation();
        String storyPath = "file:" + codeLocation + "inexistent_story";

        // When
        StoryDefiner definer = new URLStoryDefiner();
        definer.defineStory(storyPath);

        // Then
        // fail as expected

    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryForInvalidURL() {
        // Given
        String storyPath = "story_url_with_no_protocol";

        // When
        StoryDefiner definer = new URLStoryDefiner();
        definer.defineStory(storyPath);

        // Then
        // fail as expected
    }

}