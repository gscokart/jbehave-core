package org.jbehave.core.parser;

import org.jbehave.core.RunnableStory;
import org.jbehave.core.model.Story;

/**
 * <p>
 * Loads {@link Story} from a given {@link RunnableStory} class or story path.
 * </p>
 */
public interface ScenarioDefiner {

    Story loadStory(Class<? extends RunnableStory> storyClass);

    Story loadStory(String storyPath);

}
