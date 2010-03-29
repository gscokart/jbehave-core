package org.jbehave.core.parser.stories;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.reporters.PrintStreamStoryReporter;

public class MyPendingStory extends JUnitStory {

    public MyPendingStory() {
        // Making sure this doesn't output to the build while it's running
        useConfiguration(new PropertyBasedConfiguration() {
            @Override
            public PrintStreamStoryReporter forReportingStories() {
                return new PrintStreamStoryReporter(new PrintStream(new ByteArrayOutputStream()));
            }
        });
    }
}