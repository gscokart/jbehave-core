package org.jbehave.core.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jbehave.core.RunnableStory;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.InvalidScenarioResourceException;
import org.jbehave.core.errors.ScenarioNotFoundException;

/**
 * Loads core model from classpath resources, which are handled by the
 * {@link StoryParser}. Names of resources are resolved via the
 * {@link StoryNameResolver}.
 */
public class ClasspathScenarioDefiner implements ScenarioDefiner {

    private final StoryNameResolver resolver;
    private final StoryParser parser;
    private final ClassLoader classLoader;

    public ClasspathScenarioDefiner() {
        this(new UnderscoredCamelCaseResolver(), new PatternStoryParser(), Thread.currentThread()
                .getContextClassLoader());
    }

    public ClasspathScenarioDefiner(StoryParser parser) {
        this(new UnderscoredCamelCaseResolver(), parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathScenarioDefiner(StoryNameResolver converter, StoryParser parser) {
        this(converter, parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathScenarioDefiner(StoryNameResolver converter, ClassLoader classLoader) {
        this(converter, new PatternStoryParser(), classLoader);
    }

    public ClasspathScenarioDefiner(StoryNameResolver resolver, StoryParser parser, ClassLoader classLoader) {
        this.resolver = resolver;
        this.parser = parser;
        this.classLoader = classLoader;
    }

    public Story loadStory(Class<? extends RunnableStory> scenarioClass) {
        String storyPath = resolver.resolve(scenarioClass);
        String wholeStoryAsString = asString(loadInputStreamFor(storyPath));
        return parser.defineStoryFrom(wholeStoryAsString, storyPath);
    }

	public Story loadStory(String storyPath) {
        String wholeStoryAsString = asString(loadInputStreamFor(storyPath));
        return parser.defineStoryFrom(wholeStoryAsString, storyPath);
	}

	private InputStream loadInputStreamFor(String path) {
		InputStream stream = classLoader.getResourceAsStream(path);
        if (stream == null) {
            throw new ScenarioNotFoundException("Path '" + path + "' could not be found by classloader "
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
            throw new InvalidScenarioResourceException("Failed to convert input resource to string", e);
        }
    }

}
