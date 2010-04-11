package org.jbehave.examples.trader;

import org.jbehave.core.MostUsefulStoryConfiguration;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.StoryEmbedder;
import org.jbehave.core.parser.*;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.*;
import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.examples.trader.service.TradingService;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.*;

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
