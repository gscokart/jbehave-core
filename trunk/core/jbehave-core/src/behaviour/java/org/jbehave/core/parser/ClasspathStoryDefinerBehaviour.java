package org.jbehave.core.parser;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.RunnableStory;
import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.errors.StoryNotFoundException;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.model.Story;
import org.jbehave.core.parser.stories.MyPendingStory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class ClasspathStoryDefinerBehaviour {

    @Test
    public void canDefineStory() {
        // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        when(parser.defineStoryFrom("Given my step", "org/jbehave/core/parser/stories/my_pending_story")).thenReturn(story);

        // When
        Class<? extends RunnableStory> storyClass = MyPendingStory.class;
        StoryDefiner definer = new ClasspathStoryDefiner(parser);
        definer.defineStory(storyClass);

        // Then
        verify(story).namedAs(storyClass.getSimpleName());
    }

    @Test
    public void canDefineStoryWithCustomFilenameResolver() {
         // Given
        StoryParser parser = mock(StoryParser.class);
        Story story = mock(Story.class);
        when(parser.defineStoryFrom("Given my step", "org/jbehave/core/parser/stories/MyPendingStory.txt")).thenReturn(story);

        // When
        Class<? extends RunnableStory> storyClass = MyPendingStory.class;
        StoryDefiner definer =  new ClasspathStoryDefiner(new CasePreservingResolver(".txt"), parser);
        definer.defineStory(storyClass);

        // Then
        verify(story).namedAs(storyClass.getSimpleName());      
    }

    @Test(expected = StoryNotFoundException.class)
    public void cannotDefineStoryForInexistentResource() {
        StoryDefiner definer = new ClasspathStoryDefiner();
        definer.defineStory(InexistentStory.class);
    }

    @Test(expected = InvalidStoryResourceException.class)
    public void cannotDefineStoryForInvalidResource() {
        StoryDefiner definer = new ClasspathStoryDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(new I18nKeyWords()), new InvalidClassLoader());
        definer.defineStory(MyPendingStory.class);
    }

    static class InexistentStory extends JUnitStory {

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
