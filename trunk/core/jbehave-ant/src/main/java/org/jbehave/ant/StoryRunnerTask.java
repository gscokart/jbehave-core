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

        Map<String, Throwable> failedStories = new HashMap<String, Throwable>();
        for (RunnableStory story : stories()) {
            String name = story.getClass().getName();
            try {
                log("Running story " + name);
                story.runStory();
            } catch (Throwable e) {
                String message = "Failure in running story " + name;
                if (batch) {
                    // collect and postpone decision to throw exception
                    failedStories.put(name, e);
                } else {
                    if (ignoreFailure()) {
                        log(message, e, MSG_WARN);
                    } else {
                        throw new BuildException(message, e);
                    }
                }
            }
        }
        if (batch && failedStories.size() > 0) {
            String message = "Failure in running stories: " + format(failedStories);
            if (ignoreFailure()) {
                log(message, MSG_WARN);
            } else {
                throw new BuildException(message);
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

    public void setBatch(boolean batch) {
        this.batch = batch;
    }
}
