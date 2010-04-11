package org.jbehave.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jbehave.core.StoryRunnerMode;
import org.jbehave.core.StoryRunnerMonitor;
import org.jbehave.core.StoriesRunner;

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
        // TODO the runner class should be configurable and instantiated at runtime
        StoriesRunner runner = new StoriesRunner();
        runner.useRunnerMonitor(new MavenRunnerMonitor());
        runner.useRunnerMode(new StoryRunnerMode(batch, skipStories(), ignoreFailure()));
        runner.run(stories());
    }

    private class MavenRunnerMonitor implements StoryRunnerMonitor {
        public void storiesBatchFailed(String failedStories) {
            getLog().warn("Failed to run stories batch: "+failedStories);
        }

        public void storyFailed(String storyName, Throwable e) {
            getLog().warn("Failed to run story "+storyName, e);
        }

        public void runningStory(String storyName) {
            getLog().info("Running story "+storyName);
        }

        public void storiesNotRun() {
            getLog().info("Stories not run");
        }
    }
}


