package org.jbehave.core.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.StoryNotFoundException;

/**
 * Defines stories by loading the content from classpath resources and passing it
 * to the {@link StoryParser}.
 */
public class ClasspathStoryDefiner implements StoryDefiner {

    private final StoryParser parser;
    private final ClassLoader classLoader;

    public ClasspathStoryDefiner() {
        this(new PatternStoryParser());
    }

    public ClasspathStoryDefiner(StoryParser parser) {
        this(parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathStoryDefiner(ClassLoader classLoader) {
        this(new PatternStoryParser(), classLoader);
    }

    public ClasspathStoryDefiner(StoryParser parser, ClassLoader classLoader) {
        this.parser = parser;
        this.classLoader = classLoader;
    }

    public Story defineStory(String storyPath) {
        String storyAsString = loadStoryAsString(storyPath);
        Story story = parser.defineStoryFrom(storyAsString, storyPath);
        story.namedAs(new File(storyPath).getName());
        return story;
    }

    private String loadStoryAsString(String storyPath) {
        InputStream stream = classLoader.getResourceAsStream(storyPath);
        if (stream == null) {
            throw new StoryNotFoundException("Story path '" + storyPath + "' not found by class loader "
                    + classLoader);
        }
        try {
            return IOUtils.toString(stream);
        } catch (IOException e) {
            throw new InvalidStoryResourceException("Failed to load story " + storyPath + " from resource stream " + stream, e);
        }
    }


}
