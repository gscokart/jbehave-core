package org.jbehave.core.parser;

import org.jbehave.core.RunnableScenario;

/**
 * <p>
 * Resolves core names converting the Java core class to a resource
 * path.
 * </p>
 */
public interface ScenarioNameResolver {

    String resolve(Class<? extends RunnableScenario> scenarioClass);

}
