package org.jbehave.core.reporters;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jbehave.core.model.Description;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Story;

/**
 * Reporter which collects other {@link StoryReporter}s and delegates all
 * invocations to the collected reporters.
 * 
 * @author Mirko FriedenHagen
 */
public class DelegatingStoryReporter implements StoryReporter {

    private final Collection<StoryReporter> delegates;

    /**
     * Creates DelegatingStoryReporter with a given collections of delegates
     * 
     * @param delegates the ScenarioReporters to delegate to
     */
    public DelegatingStoryReporter(Collection<StoryReporter> delegates) {
        this.delegates = delegates;
    }

    /**
     * Creates DelegatingStoryReporter with a given varargs of delegates
     * 
     * @param delegates the ScenarioReporters to delegate to
     */
    public DelegatingStoryReporter(StoryReporter... delegates) {
        this(asList(delegates));
    }

    public void afterScenario() {
        for (StoryReporter reporter : delegates) {
            reporter.afterScenario();
        }
    }

    public void afterStory(boolean embeddedStory) {
        for (StoryReporter reporter : delegates) {
            reporter.afterStory(embeddedStory);
        }
    }

    public void afterStory() {
        for (StoryReporter reporter : delegates) {
            reporter.afterStory();
        }
    }

    public void beforeScenario(String title) {
        for (StoryReporter reporter : delegates) {
            reporter.beforeScenario(title);
        }
    }

    public void beforeStory(Story story, boolean embeddedStory) {
        for (StoryReporter reporter : delegates) {
            reporter.beforeStory(story, embeddedStory);
        }
    }

    public void beforeStory(Description description) {
        for (StoryReporter reporter : delegates) {
            reporter.beforeStory(description);
        }
    }

    public void beforeExamples(List<String> steps, ExamplesTable table) {
        for (StoryReporter reporter : delegates) {
            reporter.beforeExamples(steps, table);
        }
    }

    public void example(Map<String, String> tableRow) {
        for (StoryReporter reporter : delegates) {
            reporter.example(tableRow);
        }
    }

    public void afterExamples() {
        for (StoryReporter reporter : delegates) {
            reporter.afterExamples();
        }
    }

    public void examplesTable(ExamplesTable table) {
        beforeExamples(new ArrayList<String>(), table);
    }

    public void examplesTableRow(Map<String, String> tableRow) {
        example(tableRow);
    }

    public void failed(String step, Throwable e) {
        for (StoryReporter reporter : delegates) {
            reporter.failed(step, e);
        }
    }

    public void givenStories(List<String> givenScenarios) {
        for (StoryReporter reporter : delegates) {
            reporter.givenStories(givenScenarios);
        }
    }

    public void ignorable(String step) {
        for (StoryReporter reporter : delegates) {
            reporter.ignorable(step);
        }
    }
    
    public void notPerformed(String step) {
        for (StoryReporter reporter : delegates) {
            reporter.notPerformed(step);
        }
    }

    public void pending(String step) {
        for (StoryReporter reporter : delegates) {
            reporter.pending(step);
        }
    }

    public void successful(String step) {
        for (StoryReporter reporter : delegates) {
            reporter.successful(step);
        }
    }

    public Collection<StoryReporter> getDelegates() {
        return delegates;
    }

}
