package org.jbehave.core.parser;

import org.jbehave.core.model.Story;

import java.io.File;

/**
 * Defines stories by loading the story content and parsing it
 * via the {@link StoryParser}.
 */
public class ParsingStoryDefiner implements StoryDefiner {

    private final StoryParser parser;
    private final StoryContentLoader contentLoader;

    public ParsingStoryDefiner() {
        this(new PatternStoryParser());
    }

    public ParsingStoryDefiner(StoryParser parser) {
        this(parser, new ClasspathStoryContentLoader());
    }

    public ParsingStoryDefiner(StoryParser parser, StoryContentLoader contentLoader) {
        this.parser = parser;
        this.contentLoader = contentLoader;
    }

    public Story defineStory(String storyPath) {
        String storyAsString = contentLoader.loadStoryContent(storyPath);
        Story story = parser.parseStory(storyAsString, storyPath);
        story.namedAs(new File(storyPath).getName());
        return story;
    }

}