package com.lunivore.gameoflife;

import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.JUnitStory;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.parser.ClasspathStoryDefiner;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;

import com.lunivore.gameoflife.steps.GridSteps;

public class ICanToggleACell extends JUnitStory {

    public ICanToggleACell() {
        super(new PropertyBasedConfiguration() {
            @Override
            public ClasspathStoryDefiner forDefiningStories() {
                return new ClasspathStoryDefiner(new UnderscoredCamelCaseResolver(), new PatternStoryParser(keywords()));
            }
            @Override
            public StoryReporter forReportingStories() {
                return new PrintStreamStoryReporter();
            }
        });
        addSteps(new GridSteps());
    }
}
