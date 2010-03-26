package org.jbehave.core.parser;

import org.jbehave.core.RunnableScenario;
import org.jbehave.core.model.Story;

/**
 * <p>
 * Loads scenarios contained in a story from a given core class.
 * </p>
 */
public interface ScenarioDefiner {

    Story loadScenarioDefinitionsFor(Class<? extends RunnableScenario> scenarioClass);

    Story loadScenarioDefinitionsFor(String scenarioPath);

}
