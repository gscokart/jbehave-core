package org.jbehave.ant;

import static org.apache.tools.ant.Project.MSG_INFO;
import static org.apache.tools.ant.Project.MSG_WARN;

import org.apache.tools.ant.BuildException;
import org.jbehave.core.RunnableStory;
import org.jbehave.core.StoryEmbedder;

/**
 * Ant task that generate stepdocs
 * 
 * @author Mauro Talevi
 */
public class StepdocTask extends AbstractStoryTask {

    public void execute() throws BuildException {
        StoryEmbedder embedder = newStoryEmbedder();
        embedder.useRunnerMonitor(new AntRunnerMonitor());
        embedder.generateStepdoc();
    }

}
