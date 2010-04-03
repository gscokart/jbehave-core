package org.jbehave.core.parser;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;

import java.io.File;
import java.net.URL;

/**
 * Defines stories by loading the content from URL resources and passing it
 * to the {@link org.jbehave.core.parser.StoryParser}.
 */
public class URLStoryDefiner implements StoryDefiner {

    private final StoryParser parser;
    private final URLStoryLoader storyLoader;

    public URLStoryDefiner() {
        this(new PatternStoryParser());
    }

    public URLStoryDefiner(StoryParser parser) {
        this(parser, new URLStoryLoader());
    }

    public URLStoryDefiner(StoryParser parser, URLStoryLoader storyLoader) {
        this.parser = parser;
        this.storyLoader = storyLoader;
    }

    public Story defineStory(String storyPath) {
        String storyAsString = storyLoader.loadStoryAsString(storyPath);
        Story story = parser.defineStoryFrom(storyAsString, storyPath);
        story.namedAs(new File(storyPath).getName());
        return story;
    }


    public static class URLStoryLoader {
        public String loadStoryAsString(String storyPath) {
            try {
                URL url = new URL(storyPath);
                return IOUtils.toString(url.openStream());
            } catch (Exception e) {
                throw new InvalidStoryResourceException("Failed to load story " + storyPath, e);
            }
        }

    }

}