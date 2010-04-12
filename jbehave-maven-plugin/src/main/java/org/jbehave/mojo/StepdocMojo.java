package org.jbehave.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jbehave.core.RunnableStory;
import org.jbehave.core.StoryEmbedder;
import org.jbehave.core.StoryRunnerMode;

/**
 * Mojo to generate stepdocs
 * 
 * @author Mauro Talevi
 * @goal stepdoc
 */
public class StepdocMojo extends AbstractStoryMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        StoryEmbedder embedder = newStoryEmbedder();
        embedder.useRunnerMonitor(new MavenRunnerMonitor());
        embedder.generateStepdoc();
    }

}
