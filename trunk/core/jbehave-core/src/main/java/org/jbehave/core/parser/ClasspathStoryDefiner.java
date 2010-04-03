package org.jbehave.core.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.StoryNotFoundException;

/**
 * Defines stories from classpath resources, the content of which is handled by the
 * {@link StoryParser}. 
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
        String wholeStoryAsString = asString(loadInputStreamFor(storyPath));
        Story story = parser.defineStoryFrom(wholeStoryAsString, storyPath);
        story.namedAs(new File(storyPath).getName());
        return story;
	}

	private InputStream loadInputStreamFor(String path) {
		InputStream stream = classLoader.getResourceAsStream(path);
        if (stream == null) {
            throw new StoryNotFoundException("Story path '" + path + "' could not be found by classloader "
                    + classLoader);
        }
        return stream;
	}

    private String asString(InputStream stream) {
        try {
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(bytes);
            return output.toString();
        } catch (IOException e) {
            throw new InvalidStoryResourceException("Failed to convert input stream to string", e);
        }
    }

}
