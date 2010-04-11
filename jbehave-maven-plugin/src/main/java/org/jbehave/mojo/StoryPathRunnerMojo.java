package org.jbehave.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jbehave.core.StoryEmbedder;
import org.jbehave.core.StoryRunnerMode;
import org.jbehave.core.StoryRunnerMonitor;

import java.net.MalformedURLException;


/**
 * Mojo to run stories via paths
 *
 * @author Mauro Talevi
 * @goal run-stories-as-paths
 */
public class StoryPathRunnerMojo extends AbstractStoryMojo {

    /**
     * The story embedder to run the stories
     *
     * @parameter default-value="org.jbehave.core.StoryEmbedder"
     */
    private String storyEmbedder;

    /**
     * The boolean flag to run in batch mode
     *
     * @parameter default-value="false"
     */
    private boolean batch;

    public void execute() throws MojoExecutionException, MojoFailureException {
        StoryEmbedder embedder = newStoryEmbedder();
        embedder.useRunnerMonitor(new MavenRunnerMonitor());
        embedder.useRunnerMode(new StoryRunnerMode(batch, skipStories(), ignoreFailure()));
        embedder.runStoriesAsPaths(storyPaths());
    }

    private StoryEmbedder newStoryEmbedder() {
        try {
            return (StoryEmbedder)createStoryClassLoader().loadClass(storyEmbedder).newInstance();
        } catch ( Exception e) {
            throw new RuntimeException("Failed to create story embedder "+storyEmbedder);
        }
    }

    protected class MavenRunnerMonitor implements StoryRunnerMonitor {
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