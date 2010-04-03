package org.jbehave.core.reporters;

import org.jbehave.core.parser.StoryLocation;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.jbehave.Ensure.ensureThat;


public class FilePrintStreamFactoryBehaviour {

    @Test
    public void shouldHandleStoryPathInClasspath() {
        String codeLocation = new StoryLocation("").getCodeLocation();
        String storyPath = "org/jbehave/examples/trader/stories/my_given.story";
        ensureOutputFileHasProtocol("", codeLocation, storyPath);
    }

    @Test
    public void shouldHandleStoryPathAsURL() {
        String codeLocation = new StoryLocation("").getCodeLocation();
        String storyPath = "file://" + codeLocation + "org/jbehave/examples/trader/stories/my_given.story";
        ensureOutputFileHasProtocol("file:", codeLocation, storyPath);
    }

    @Test
    public void shouldHandleStoryPathAsURLWithSpecifiedCodeSourceClass() {
        String codeLocation = new StoryLocation("", FilePrintStreamFactory.class).getCodeLocation();
        String storyPath = "file://" + codeLocation + "org/jbehave/examples/trader/stories/my_given.story";
        ensureOutputFileHasProtocol("file:", codeLocation, storyPath);
    }

    private void ensureOutputFileHasProtocol(String protocol, String codeLocation, String storyPath) {
        FilePrintStreamFactory.FileConfiguration configuration = new FilePrintStreamFactory.FileConfiguration("ext");
        FilePrintStreamFactory factory = new FilePrintStreamFactory(storyPath, configuration);
        File outputFile = factory.getOutputFile();
        String expected = protocol + codeLocation + "org/jbehave/examples/trader/stories/" + configuration.getDirectory() + "/"
                + "org.jbehave.examples.trader.stories.my_given." + configuration.getExtension();
        ensureThat(outputFile.toString(), equalTo(expected));

    }


}
