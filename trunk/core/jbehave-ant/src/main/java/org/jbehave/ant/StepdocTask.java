package org.jbehave.ant;

import static org.apache.tools.ant.Project.MSG_INFO;
import static org.apache.tools.ant.Project.MSG_WARN;

import org.apache.tools.ant.BuildException;
import org.jbehave.core.RunnableStory;

/**
 * Ant task that generate stepdocs
 * 
 * @author Mauro Talevi
 */
public class StepdocTask extends AbstractStoryTask {

    public void execute() throws BuildException {
        if (skipScenarios()) {
            log("Skipped running stories", MSG_INFO);
            return;
        }
        for (RunnableStory story : stories()) {
            String scenarioName = story.getClass().getName();
            try {
                log("Generating stepdoc for " + scenarioName);
                story.generateStepdoc();
            } catch (Throwable e) {
                String message = "Failed to generate stepdoc for " + scenarioName;
                if (ignoreFailure()) {
                    log(message, e, MSG_WARN);
                } else {
                    throw new BuildException(message, e);
                }
            }
        }
    }

}
