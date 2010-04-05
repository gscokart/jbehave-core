package org.jbehave.core.parser;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.errors.InvalidStoryResourceException;

import java.net.URL;

/**
 * Loads story content via URLs
 */
public class URLStoryContentLoader implements StoryContentLoader {
    public String loadStoryContent(String storyPath) {
        try {
            URL url = new URL(storyPath);
            return IOUtils.toString(url.openStream());
        } catch (Exception e) {
            throw new InvalidStoryResourceException("Failed to load story content " + storyPath, e);
        }
    }

}