package org.jbehave.core.errors;

/**
 * Thrown when a core resource is not valid
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class InvalidScenarioResourceException extends RuntimeException {

    public InvalidScenarioResourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
