package org.jbehave.core.parser;

import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;
import org.jbehave.core.parser.stories.MyPendingStory;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RelativeFileStoryContentLoaderBehaviour {

    @Test
    public void canLoadStoryContent() {
        // Given
        String storyPath = "org/jbehave/core/parser/stories/MyPendingStory.txt";
        String storyAsString = "Given my step";

        // When
        StoryContentLoader definer = new RelativeFileStoryContentLoader(MyPendingStory.class, "../../src/behaviour/java");
        definer.loadStoryContent(storyPath);
       
    }


}