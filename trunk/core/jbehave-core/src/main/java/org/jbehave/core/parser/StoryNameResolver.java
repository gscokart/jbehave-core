package org.jbehave.core.parser;

import org.jbehave.core.RunnableStory;

/**
 * <p>
 * Resolves story names converting the Java {@link RunnableStory} class to a resource
 * path.
 * </p>
 */
public interface StoryNameResolver {

    String resolve(Class<? extends RunnableStory> storyClass);

}
