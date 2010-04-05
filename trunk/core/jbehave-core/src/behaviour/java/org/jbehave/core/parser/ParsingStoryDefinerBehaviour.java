package org.jbehave.core.parser;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.errors.StoryNotFoundException;
import org.jbehave.core.model.Story;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

public class ParsingStoryDefinerBehaviour {


    @Test
    public void canDefineStoryWithClasspathLoading() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        String storyPath = "org/jbehave/core/parser/stories/my_pending_story";
        String storyAsString = "Given my step";
        when(parser.parseStory(storyAsString, storyPath)).thenReturn(story);

        // When
        StoryDefiner definer = new ParsingStoryDefiner(parser, new ClasspathLoading());
        Story definedStory = definer.defineStory(storyPath);
        assertSame(story, definedStory);

        // Then
        verify(story).namedAs(new File(storyPath).getName());
    }

    @Test(expected = StoryNotFoundException.class)
    public void cannotDefineStoryWithClasspathLoadingForInexistentResource() {
        StoryParser parser = mock(StoryParser.class);
        StoryDefiner definer = new ParsingStoryDefiner(parser, new ClasspathLoading());

        definer.defineStory("inexistent.story");
    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryWithClasspathLoadingForInvalidResource() {
        StoryParser parser = mock(StoryParser.class);
        StoryDefiner definer = new ParsingStoryDefiner(parser, new ClasspathLoading(new InvalidClassLoader()));
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


    @Test
    public void canDefineStoryWithURLLoading() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        String codeLocation = new StoryLocation("", this.getClass()).getCodeLocation();
        String storyPath = "file:" + codeLocation + "org/jbehave/core/parser/stories/my_pending_story";
        String storyAsString = "Given my step";
        when(parser.parseStory(storyAsString, storyPath)).thenReturn(story);

        // When
        StoryDefiner definer = new ParsingStoryDefiner(parser, new URLLoading());
        definer.defineStory(storyPath);

        // Then       
        verify(story).namedAs(new File(storyPath).getName());
    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryWithURLLoadingForInexistentResource() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        String codeLocation = new StoryLocation("", this.getClass()).getCodeLocation();
        String storyPath = "file:" + codeLocation + "inexistent_story";

        // When
        StoryDefiner definer = new ParsingStoryDefiner(parser, new URLLoading());
        definer.defineStory(storyPath);

        // Then
        // fail as expected

    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryWithURLLoadingForInvalidURL() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        String storyPath = "story_url_with_no_protocol";

        // When
        StoryDefiner definer = new ParsingStoryDefiner(parser, new URLLoading());
        definer.defineStory(storyPath);

        // Then
        // fail as expected
    }

}