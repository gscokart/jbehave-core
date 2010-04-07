package org.jbehave.core.parser;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.jbehave.Ensure.ensureThat;
import static org.mockito.Mockito.*;

public class StoryLocationBehaviour {

    @Test
    public void canHandleURLs() {
        String codeLocation = new StoryLocation("").getCodeLocation();
        String storyPath = "file:" + codeLocation + "org/jbehave/core/parser/stories/my_pending_story";
        StoryLocation storyLocation = new StoryLocation(storyPath);
        ensureThat(storyLocation.getPath(), equalTo(storyPath));
        ensureThat(storyLocation.getLocation(), equalTo(storyPath));
        ensureThat(storyLocation.getName(), equalTo("org/jbehave/core/parser/stories/my_pending_story"));
    }
                                 
    @Test
    public void canHandleClasspathResources() {
        String storyPath = "org/jbehave/core/parser/stories/my_pending_story";
        StoryLocation storyLocation = new StoryLocation(storyPath);
        ensureThat(storyLocation.getPath(), equalTo(storyPath));
        ensureThat(storyLocation.getLocation(), equalTo("file:" + storyLocation.getCodeLocation() + storyPath));
        ensureThat(storyLocation.getName(), equalTo(storyPath));
    }

}