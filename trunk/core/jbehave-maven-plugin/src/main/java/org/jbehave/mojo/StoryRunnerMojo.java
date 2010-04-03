package org.jbehave.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jbehave.core.RunnableStory;

import java.util.HashMap;
import java.util.Map;

/**
 * Mojo to run stories
 *
 * @author Mauro Talevi
 * @goal run-stories
 */
public class StoryRunnerMojo extends AbstractStoryMojo {

    /**
     * The boolean flag to run in batch mode
     *
     * @parameter default-value="false"
     */
    private boolean batch;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipStories()) {
            getLog().info("Skipped running stories");
            return;
        }

        Map<String, Throwable> failedStories = new HashMap<String, Throwable>();
        for (RunnableStory story : stories()) {
            String storyName = story.getClass().getName();
            try {
                getLog().info("Running story " + storyName);
                story.runStory();
            } catch (Throwable e) {
                String message = "Failure in running story " + storyName;
                if (batch) {
                    // collect and postpone decision to throw exception
                    failedStories.put(storyName, e);
                } else {
                    if (ignoreFailure()) {
                        getLog().warn(message, e);
                    } else {
                        throw new MojoExecutionException(message, e);
                    }
                }
            }
        }

        if (batch && failedStories.size() > 0) {
            String message = "Failure in running stories: " + format(failedStories);
            if ( ignoreFailure() ){
                getLog().warn(message);
            } else {
                throw new MojoExecutionException(message);
            }
        }

    }

    private String format(Map<String, Throwable> failedStories) {
        StringBuffer sb = new StringBuffer();
        for (String storyName : failedStories.keySet()) {
            Throwable cause = failedStories.get(storyName);
            sb.append("\n");
            sb.append(storyName);
            sb.append(": ");
            sb.append(cause.getMessage());
        }
        return sb.toString();
    }
    
}


