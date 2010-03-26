package org.jbehave.core.parser;

import org.jbehave.core.model.Story;

/**
 * <p>
 * Parses the stories contained in a story from a textual representation.
 * </p>
 */
public interface StoryParser {

    /**
     * Defines story from its textual representation
     * 
     * @param storyAsText the textual representation
     * @return The Story
     */
    Story defineStoryFrom(String storyAsText);
    
    /**
     * Defines story from its textual representation and (optional) story path
     * 
     * @param storyAsText the textual representation
     * @param storyPath the story path, may be <code>null</code>
     * @return The Story
     * @since 2.4
     */
    Story defineStoryFrom(String storyAsText, String storyPath);

}
