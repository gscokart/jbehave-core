package org.jbehave.core.parser.stories;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedStoryConfiguration;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;

public class MyMultipleStory extends JUnitStory {
    public MyMultipleStory() {
        // Making sure this doesn't output to the build while it's running
        useConfiguration(new PropertyBasedStoryConfiguration() {
            @Override
            public StoryReporter storyReporter() {
                return new PrintStreamStoryReporter(new PrintStream(new ByteArrayOutputStream()));
            }
        });
    }
}
