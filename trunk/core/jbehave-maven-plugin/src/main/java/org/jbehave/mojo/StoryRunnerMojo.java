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

        Map<String, Throwable> failedScenarios = new HashMap<String, Throwable>();
        for (RunnableStory story : stories()) {
            String scenarioName = story.getClass().getName();
            try {
                getLog().info("Running core " + scenarioName);
                story.runStory();
            } catch (Throwable e) {
                String message = "Failure in running core " + scenarioName;
                if (batch) {
                    // collect and postpone decision to throw exception
                    failedScenarios.put(scenarioName, e);
                } else {
                    if (ignoreFailure()) {
                        getLog().warn(message, e);
                    } else {
                        throw new MojoExecutionException(message, e);
                    }
                }
            }
        }

        if (batch && failedScenarios.size() > 0) {
            String message = "Failure in runing stories: " + format(failedScenarios);
            if ( ignoreFailure() ){
                getLog().warn(message);
            } else {
                throw new MojoExecutionException(message);
            }
        }

    }

    private String format(Map<String, Throwable> failedScenarios) {
        StringBuffer sb = new StringBuffer();
        for (String scenarioName : failedScenarios.keySet()) {
            Throwable cause = failedScenarios.get(scenarioName);
            sb.append("\n");
            sb.append(scenarioName);
            sb.append(": ");
            sb.append(cause.getMessage());
        }
        return sb.toString();
    }
    
}


