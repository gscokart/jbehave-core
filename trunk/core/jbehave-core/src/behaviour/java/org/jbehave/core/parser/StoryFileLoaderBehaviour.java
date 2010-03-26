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

public class StoryFileLoaderBehaviour {

    @Test
    public void canLoadStory() {
        StoryParser parser = mock(StoryParser.class);
        ClasspathScenarioDefiner loader = new ClasspathScenarioDefiner(parser);
        loader.loadStory(MyPendingStory.class);
        verify(parser).defineStoryFrom("Given my step", "org/jbehave/core/parser/stories/my_pending_story");
    }

    @Test
    public void canLoadScenarioWithCustomFilenameResolver() {
        StoryParser parser = mock(StoryParser.class);
        ClasspathScenarioDefiner loader = new ClasspathScenarioDefiner(new CasePreservingResolver(".txt"), parser);
        loader.loadStory(MyPendingStory.class);
        verify(parser).defineStoryFrom("Given my step", "org/jbehave/core/parser/stories/MyPendingStory.txt");
    }

    @Test(expected = ScenarioNotFoundException.class)
    public void cannotLoadScenarioForInexistentResource() {
        ClasspathScenarioDefiner loader = new ClasspathScenarioDefiner();
        loader.loadStory(InexistentStory.class);
    }

    @Test(expected = InvalidScenarioResourceException.class)
    public void cannotLoadScenarioForInvalidResource() {
        ClasspathScenarioDefiner loader = new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(new I18nKeyWords()), new InvalidClassLoader());
        loader.loadStory(MyPendingStory.class);
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
