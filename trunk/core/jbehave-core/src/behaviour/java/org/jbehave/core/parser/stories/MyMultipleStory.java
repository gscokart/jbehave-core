package org.jbehave.core.parser.stories;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.reporters.PrintStreamStoryReporter;

public class MyMultipleStory extends JUnitStory {
    public MyMultipleStory() {
        // Making sure this doesn't output to the build while it's running
        super(new PropertyBasedConfiguration() {
            @Override
            public PrintStreamStoryReporter forReportingStories() {
                return new PrintStreamStoryReporter(new PrintStream(new ByteArrayOutputStream()));
            }
        });
    }
}
