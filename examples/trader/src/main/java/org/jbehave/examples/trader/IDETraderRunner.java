package org.jbehave.examples.trader;

import org.jbehave.core.parser.StoryLocation;
import org.jbehave.core.parser.StoryPathFinder;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * A simple facade to StoryEmbedder that shows how we can embed the story running
 * into any IDE, using any running framework. 
 */
public class IDETraderRunner {

    // Embedder defines the configuration and candidate steps
    private TraderStoryEmbedder embedder = new TraderStoryEmbedder();

    @Test
    public void runAsJUnit() {
        embedder.runStoriesAsPaths(storyPaths());
    }

    protected List<String> storyPaths() {
        StoryPathFinder finder = new StoryPathFinder();
        String basedir = new StoryLocation("", this.getClass()).getCodeLocation();
        return finder.listStoryPaths(basedir, "", asList("**/*.story"), asList(""));
    }

}
