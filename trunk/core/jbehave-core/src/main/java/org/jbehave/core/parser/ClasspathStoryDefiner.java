package org.jbehave.core.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jbehave.core.RunnableStory;
import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.StoryNotFoundException;

/**
 * Defines stories from classpath resources, which are handled by the
 * {@link StoryParser}. Names of resources are resolved via the
 * {@link StoryNameResolver}.
 */
public class ClasspathStoryDefiner implements StoryDefiner {

    private final StoryNameResolver resolver;
    private final StoryParser parser;
    private final ClassLoader classLoader;

    public ClasspathStoryDefiner() {
        this(new UnderscoredCamelCaseResolver(), new PatternStoryParser(), Thread.currentThread()
                .getContextClassLoader());
    }

    public ClasspathStoryDefiner(StoryParser parser) {
        this(new UnderscoredCamelCaseResolver(), parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathStoryDefiner(StoryNameResolver converter, StoryParser parser) {
        this(converter, parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathStoryDefiner(StoryNameResolver converter, ClassLoader classLoader) {
        this(converter, new PatternStoryParser(), classLoader);
    }

    public ClasspathStoryDefiner(StoryNameResolver resolver, StoryParser parser, ClassLoader classLoader) {
        this.resolver = resolver;
        this.parser = parser;
        this.classLoader = classLoader;
    }

    public Story defineStory(Class<? extends RunnableStory> storyClass) {
        String storyPath = resolver.resolve(storyClass);
        String wholeStoryAsString = asString(loadInputStreamFor(storyPath));
        Story story = parser.defineStoryFrom(wholeStoryAsString, storyPath);
        story.namedAs(storyClass.getSimpleName());
        return story;
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
            throw new StoryNotFoundException("Path '" + path + "' could not be found by classloader "
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
            throw new InvalidStoryResourceException("Failed to convert input resource to string", e);
        }
    }

}
