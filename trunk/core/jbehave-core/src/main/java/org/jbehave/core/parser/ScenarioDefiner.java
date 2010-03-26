package org.jbehave.core.parser;

import org.jbehave.core.RunnableScenario;
import org.jbehave.core.definition.StoryDefinition;

/**
 * <p>
 * Loads scenarios contained in a story from a given core class.
 * </p>
 */
public interface ScenarioDefiner {

    StoryDefinition loadScenarioDefinitionsFor(Class<? extends RunnableScenario> scenarioClass);

    StoryDefinition loadScenarioDefinitionsFor(String scenarioPath);

}
