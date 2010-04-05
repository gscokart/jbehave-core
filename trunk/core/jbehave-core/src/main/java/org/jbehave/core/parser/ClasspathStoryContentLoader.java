package org.jbehave.core.parser;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.errors.StoryNotFoundException;
import org.jbehave.core.model.Story;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loades story content from classpath resources.
 */
public class ClasspathStoryContentLoader implements StoryContentLoader {

    private final ClassLoader classLoader;

    public ClasspathStoryContentLoader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ClasspathStoryContentLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public String loadStoryContent(String storyPath) {
        InputStream stream = classLoader.getResourceAsStream(storyPath);
        if (stream == null) {
            throw new StoryNotFoundException("Story path '" + storyPath + "' not found by class loader "
                    + classLoader);
        }
        try {
            return IOUtils.toString(stream);
        } catch (IOException e) {
            throw new InvalidStoryResourceException("Failed to load story content for " + storyPath + " from resource stream " + stream, e);
        }
    }


}