package org.jbehave.core.parser;

import org.jbehave.core.model.Story;

import java.io.File;

/**
 * Defines stories by loading the content from classpath resources and passing it
 * to the {@link StoryParser}.
 */
public class ClasspathStoryDefiner implements StoryDefiner {

    private final StoryParser parser;
    private final StoryContentLoader contentLoader;

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
        this(parser, new ClasspathStoryContentLoader(classLoader));
    }

    public ClasspathStoryDefiner(StoryParser parser, StoryContentLoader contentLoader) {
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
