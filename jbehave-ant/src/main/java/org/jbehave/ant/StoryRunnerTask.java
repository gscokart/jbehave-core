package org.jbehave.ant;

import static org.apache.tools.ant.Project.MSG_INFO;
import static org.apache.tools.ant.Project.MSG_WARN;

import org.apache.tools.ant.BuildException;
import org.jbehave.core.StoryRunnerMode;
import org.jbehave.core.StoryRunnerMonitor;
import org.jbehave.core.StoriesRunner;

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
        // TODO the runner class should be configurable and instantiated at runtime
        StoriesRunner runner = new StoriesRunner();
        runner.useRunnerMonitor(new AntRunnerMonitor());
        runner.useRunnerMode(new StoryRunnerMode(batch, skipStories(), ignoreFailure()));
        runner.run(stories());
    }

    private class AntRunnerMonitor implements StoryRunnerMonitor {
        public void storiesBatchFailed(String failedStories) {
            log("Failed to run stories batch: "+failedStories, MSG_WARN);
        }

        public void storyFailed(String storyName, Throwable e) {
            log("Failed to run story "+storyName, e, MSG_WARN);
        }

        public void runningStory(String storyName) {
            log("Running story "+storyName,  MSG_INFO);
        }

        public void storiesNotRun() {
            log("Stories not run");
        }
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }
}
