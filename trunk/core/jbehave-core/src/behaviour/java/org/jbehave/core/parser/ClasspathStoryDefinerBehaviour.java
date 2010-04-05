package org.jbehave.core.parser;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.errors.StoryNotFoundException;
import org.jbehave.core.model.Story;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class ClasspathStoryDefinerBehaviour {

    @Test
    public void canDefineStory() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        String storyPath = "org/jbehave/core/parser/stories/my_pending_story";
        String storyAsString = "Given my step";
        when(parser.parseStory(storyAsString, storyPath)).thenReturn(story);

        // When
        StoryDefiner definer = new ClasspathStoryDefiner(parser);
        definer.defineStory(storyPath);

        // Then
        verify(story).namedAs(new File(storyPath).getName());
    }

    @Test(expected = StoryNotFoundException.class)
    public void cannotDefineStoryForInexistentResource() {
        StoryDefiner definer = new ClasspathStoryDefiner();
        definer.defineStory("inexistent.story");
    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryForInvalidResource() {
        StoryDefiner definer = new ClasspathStoryDefiner(new InvalidClassLoader());
        definer.defineStory("inexistent.story");
    }

    static class InvalidClassLoader extends ClassLoader {

        @Override
        public InputStream getResourceAsStream(String name) {
            return new InputStream() {

                public int available() throws IOException {
                    return 1;
                }

                @Override
                public int read() throws IOException {
                    throw new IOException("invalid");
                }

            };
        }
    }
}
