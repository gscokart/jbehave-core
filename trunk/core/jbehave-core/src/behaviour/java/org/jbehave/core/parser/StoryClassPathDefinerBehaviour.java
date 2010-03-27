package org.jbehave.core.parser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.errors.InvalidScenarioResourceException;
import org.jbehave.core.errors.ScenarioNotFoundException;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.parser.stories.MyPendingStory;
import org.junit.Test;

public class StoryClassPathDefinerBehaviour {

    @Test
    public void canLoadStory() {
        StoryParser parser = mock(StoryParser.class);
        ClasspathStoryDefiner loader = new ClasspathStoryDefiner(parser);
        loader.defineStory(MyPendingStory.class);
        verify(parser).defineStoryFrom("Given my step", "org/jbehave/core/parser/stories/my_pending_story");
    }

    @Test
    public void canLoadScenarioWithCustomFilenameResolver() {
        StoryParser parser = mock(StoryParser.class);
        ClasspathStoryDefiner loader = new ClasspathStoryDefiner(new CasePreservingResolver(".txt"), parser);
        loader.defineStory(MyPendingStory.class);
        verify(parser).defineStoryFrom("Given my step", "org/jbehave/core/parser/stories/MyPendingStory.txt");
    }

    @Test(expected = ScenarioNotFoundException.class)
    public void cannotLoadScenarioForInexistentResource() {
        ClasspathStoryDefiner loader = new ClasspathStoryDefiner();
        loader.defineStory(InexistentStory.class);
    }

    @Test(expected = InvalidScenarioResourceException.class)
    public void cannotLoadScenarioForInvalidResource() {
        ClasspathStoryDefiner loader = new ClasspathStoryDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(new I18nKeyWords()), new InvalidClassLoader());
        loader.defineStory(MyPendingStory.class);
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
