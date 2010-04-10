package org.jbehave.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jbehave.core.RunnerMode;
import org.jbehave.core.RunnerMonitor;
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
        StoriesRunner runner = new StoriesRunner(new MavenRunnerMonitor(), new RunnerMode(batch, skipStories(), ignoreFailure()));
        runner.run(stories());
    }

    private class MavenRunnerMonitor implements RunnerMonitor {
        public void batchStoriesFailed(String message) {
            getLog().warn(message);
        }

        public void storyFailed(String message, Throwable e) {
            getLog().warn(message, e);
        }

        public void runningStory(String storyName) {
            getLog().info("Running story "+storyName);
        }

        public void storiesNotRun() {
            getLog().info("Stories not run");
        }
    }
}


