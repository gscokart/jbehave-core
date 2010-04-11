package org.jbehave.ant;

import org.apache.tools.ant.BuildException;
import org.jbehave.core.StoryEmbedder;
import org.jbehave.core.StoryRunnerMode;
import org.jbehave.core.StoryRunnerMonitor;

import static org.apache.tools.ant.Project.MSG_INFO;
import static org.apache.tools.ant.Project.MSG_WARN;

/**
 * Ant task that runs stories as paths
 *
 * @author Mauro Talevi
 */
public class StoryPathRunnerTask extends AbstractStoryTask {

    /**
     * The story embedder to run the stories
     *
     * @parameter default-value="org.jbehave.core.StoryEmbedder"
     */
    private String storyEmbedder;

    /**
     * The boolean flag to run in batch mode
     */
    private boolean batch;

    public void execute() throws BuildException {
        StoryEmbedder runner = newStoryEmbedder();
        runner.useRunnerMonitor(new AntRunnerMonitor());
        runner.useRunnerMode(new StoryRunnerMode(batch, skipStories(), ignoreFailure()));
        runner.runStoriesAsPaths(storyPaths());
    }

    private StoryEmbedder newStoryEmbedder() {
        try {
            return (StoryEmbedder) createStoryClassLoader().loadClass(storyEmbedder).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create story embedder " + storyEmbedder);
        }
    }


    private class AntRunnerMonitor implements StoryRunnerMonitor {
        public void storiesBatchFailed(String failedStories) {
            log("Failed to run stories batch: " + failedStories, MSG_WARN);
        }

        public void storyFailed(String storyName, Throwable e) {
            log("Failed to run story " + storyName, e, MSG_WARN);
        }

        public void runningStory(String storyName) {
            log("Running story " + storyName, MSG_INFO);
        }

        public void storiesNotRun() {
            log("Stories not run");
        }
    }

    public void setStoryEmbedder(String storyEmbedder) {
        this.storyEmbedder = storyEmbedder;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }
}