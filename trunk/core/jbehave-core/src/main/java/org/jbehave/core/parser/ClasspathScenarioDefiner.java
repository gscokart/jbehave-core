package org.jbehave.core.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jbehave.core.RunnableScenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.InvalidScenarioResourceException;
import org.jbehave.core.errors.ScenarioNotFoundException;

/**
 * Loads core model from classpath resources, which are handled by the
 * {@link ScenarioParser}. Names of resources are resolved via the
 * {@link ScenarioNameResolver}.
 */
public class ClasspathScenarioDefiner implements ScenarioDefiner {

    private final ScenarioNameResolver resolver;
    private final ScenarioParser parser;
    private final ClassLoader classLoader;

    public ClasspathScenarioDefiner() {
        this(new UnderscoredCamelCaseResolver(), new PatternScenarioParser(), Thread.currentThread()
                .getContextClassLoader());
    }

    public ClasspathScenarioDefiner(ScenarioParser parser) {
        this(new UnderscoredCamelCaseResolver(), parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathScenarioDefiner(ScenarioNameResolver converter, ScenarioParser parser) {
        this(converter, parser, Thread.currentThread().getContextClassLoader());
    }

    public ClasspathScenarioDefiner(ScenarioNameResolver converter, ClassLoader classLoader) {
        this(converter, new PatternScenarioParser(), classLoader);
    }

    public ClasspathScenarioDefiner(ScenarioNameResolver resolver, ScenarioParser parser, ClassLoader classLoader) {
        this.resolver = resolver;
        this.parser = parser;
        this.classLoader = classLoader;
    }

    public Story loadScenarioDefinitionsFor(Class<? extends RunnableScenario> scenarioClass) {
        String storyPath = resolver.resolve(scenarioClass);
        String wholeStoryAsString = asString(loadInputStreamFor(storyPath));
        return parser.defineStoryFrom(wholeStoryAsString, storyPath);
    }

	public Story loadScenarioDefinitionsFor(String storyPath) {
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
