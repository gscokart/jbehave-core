package org.jbehave.ant;

import static org.apache.tools.ant.Project.MSG_INFO;
import static org.apache.tools.ant.Project.MSG_WARN;

import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.jbehave.core.RunnableStory;

/**
 * Ant task that runs stories
 * 
 * @author Mauro Talevi
 */
public class StoryRunnerTask extends AbstractStoryTask {

    /**
     * The boolean flag to run in batch mode
     */
    private boolean batch;

    public void execute() throws BuildException {
        if (skipStories()) {
            log("Skipped running stories", MSG_INFO);
            return;
        }

        Map<String, Throwable> failedScenarios = new HashMap<String, Throwable>();
        for (RunnableStory story : stories()) {
            String name = story.getClass().getName();
            try {
                log("Running story " + name);
                story.runStory();
            } catch (Throwable e) {
                String message = "Failure in running story " + name;
                if (batch) {
                    // collect and postpone decision to throw exception
                    failedScenarios.put(name, e);
                } else {
                    if (ignoreFailure()) {
                        log(message, e, MSG_WARN);
                    } else {
                        throw new BuildException(message, e);
                    }
                }
            }
        }
        if (batch && failedScenarios.size() > 0) {
            String message = "Failure in running stories: " + format(failedScenarios);
            if (ignoreFailure()) {
                log(message, MSG_WARN);
            } else {
                throw new BuildException(message);
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

    public void setBatch(boolean batch) {
        this.batch = batch;
    }
}
