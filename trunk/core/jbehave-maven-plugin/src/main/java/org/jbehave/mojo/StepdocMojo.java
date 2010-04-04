package org.jbehave.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jbehave.core.RunnableStory;

/**
 * Mojo to generate stepdocs
 * 
 * @author Mauro Talevi
 * @goal stepdoc
 */
public class StepdocMojo extends AbstractStoryMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipStories()) {
            getLog().info("Skipped generating stepdoc");
            return;
        }
        for (RunnableStory story : stories()) {
            String storyName = story.getClass().getName();
            try {
                getLog().info("Generating stepdoc for " + storyName);
                story.generateStepdoc();
            } catch (Throwable e) {
                String message = "Failed to generate stepdoc for " + storyName;
                if (ignoreFailure()) {
                    getLog().warn(message, e);
                } else {
                    throw new MojoExecutionException(message, e);
                }
            }
        }
    }

}
