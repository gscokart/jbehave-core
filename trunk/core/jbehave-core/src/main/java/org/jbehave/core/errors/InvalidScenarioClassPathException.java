package org.jbehave.core.errors;

/**
 * Thrown when a core class path is not valid
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class InvalidScenarioClassPathException extends RuntimeException {

    public InvalidScenarioClassPathException(String message) {
        super(message);
    }

}
