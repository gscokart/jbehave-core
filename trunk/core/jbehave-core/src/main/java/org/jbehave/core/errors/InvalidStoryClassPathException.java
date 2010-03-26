package org.jbehave.core.errors;

/**
 * Thrown when the classpath of a {@link RunnableStory} is not valid
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class InvalidStoryClassPathException extends RuntimeException {

    public InvalidStoryClassPathException(String message) {
        super(message);
    }

}
