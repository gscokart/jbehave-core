package org.jbehave.core.errors;

/**
 * Thrown when a core is not found by the classloader
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class ScenarioNotFoundException extends RuntimeException {

    public ScenarioNotFoundException(String message) {
        super(message);
    }

}
