package org.jbehave.core.reporters;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.jbehave.core.model.Description;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.OutcomesTable;
import org.jbehave.core.model.OutcomesTable.OutcomesFailed;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.junit.Test;

public class PostStoryStatisticsCollectorBehaviour {

    @Test
    public void shouldCollectStoryStatistics() {
        // Given
        OutputStream out = new ByteArrayOutputStream();
        PrintStream print = new PrintStream(out);
        StoryReporter reporter = new PostStoryStatisticsCollector(print);

        // When
        narrateAnInterestingStory(reporter);

        // Then
        assertThat(out.toString(), containsString("scenarios=2"));
        assertThat(out.toString(), containsString("scenariosSuccessful=1"));
        assertThat(out.toString(), containsString("scenariosFailed=1"));
        assertThat(out.toString(), containsString("examples=2"));
        assertThat(out.toString(), containsString("givenStories=1"));
        assertThat(out.toString(), containsString("steps=7"));
        assertThat(out.toString(), containsString("stepsFailed=1"));
        assertThat(out.toString(), containsString("stepsPending=1"));
        assertThat(out.toString(), containsString("stepsIgnorable=1"));
        assertThat(out.toString(), containsString("stepsNotPerformed=1"));
        assertThat(out.toString(), containsString("stepsSuccessful=3"));
        assertThat(reporter.toString(), containsString(print.toString()));
    }

    @Test
    public void shouldCollectStoryStatisticsWhenStoryNotAllowedByFilter() {
        // Given
        OutputStream out = new ByteArrayOutputStream();
        StoryReporter reporter = new PostStoryStatisticsCollector(new PrintStream(out));

        // When
        narrateAnInterestingStoryNotAllowedByFilter(reporter, true);

        // Then
        assertThat(out.toString(), containsString("notAllowed=1"));
    }

    @Test
    public void shouldCollectStoryStatisticsWhenScenariosNotAllowedByFilter() {
        // Given
        OutputStream out = new ByteArrayOutputStream();
        StoryReporter reporter = new PostStoryStatisticsCollector(new PrintStream(out));

        // When
        narrateAnInterestingStoryNotAllowedByFilter(reporter, false);

        // Then
        assertThat(out.toString(), containsString("notAllowed=0"));
        assertThat(out.toString(), containsString("scenariosNotAllowed=1"));
    }

    @Test
    public void shouldNotCountFailedScenariosIfExceptionsAreNull() {
        // Given
        OutputStream out = new ByteArrayOutputStream();
        StoryReporter reporter = new PostStoryStatisticsCollector(new PrintStream(out));

        // When
        reporter.failed("a failed step", null);
        reporter.failedOutcomes("some failed outcomes", null);

        // Then
        assertThat(out.toString(), not(containsString("scenariosFailed")));
    }

    private void narrateAnInterestingStory(StoryReporter reporter) {
        Story story = new Story("/path/to/story", new Description("An interesting story"), new Narrative(
                "renovate my house", "customer", "get a loan"), new ArrayList<Scenario>());
        boolean givenStory = false;
        reporter.dryRun();
        reporter.beforeStory(story, givenStory);
        reporter.beforeScenario("I ask for a loan");
        reporter.scenarioMeta(Meta.EMPTY);
        reporter.givenStories(asList("path/to/story1", "path/to/story2"));
        reporter.successful("Given I have a balance of $50");
        reporter.ignorable("!-- A comment");
        reporter.successful("When I request $20");
        reporter.successful("When I ask Liz for a loan of $100");
        reporter.afterScenario();
        reporter.beforeScenario("A failing scenario");
        OutcomesTable outcomesTable = new OutcomesTable();
        outcomesTable.addOutcome("I don't return all", 100.0, equalTo(50.));
        try {
            outcomesTable.verify();
        } catch (OutcomesFailed e) {
            reporter.failedOutcomes("Then I don't return loan", e.outcomesTable());
        }
        reporter.pending("Then I should have a balance of $30");
        reporter.notPerformed("Then I should have $20");
        ExamplesTable table = new ExamplesTable("|money|to|\n|$30|Mauro|\n|$50|Paul|\n");
        reporter.beforeExamples(asList("Given money <money>", "Then I give it to <to>"), table);
        reporter.example(table.getRow(0));
        reporter.example(table.getRow(1));
        reporter.afterExamples();
        reporter.afterScenario();
        reporter.afterStory(givenStory);
    }

    private void narrateAnInterestingStoryNotAllowedByFilter(StoryReporter reporter, boolean storyNotAllowed) {
        Properties meta = new Properties();
        meta.setProperty("theme", "testing");
        meta.setProperty("author", "Mauro");
        Story story = new Story("/path/to/story", new Description("An interesting story"), new Meta(meta),
                new Narrative("renovate my house", "customer", "get a loan"), Arrays.asList(new Scenario("A scenario",
                        new Meta(meta), GivenStories.EMPTY, ExamplesTable.EMPTY, new ArrayList<String>())));
        if (storyNotAllowed) {
            reporter.storyNotAllowed(story, "-theme testing");
        } else {
            reporter.beforeStory(story, false);
            reporter.scenarioNotAllowed(story.getScenarios().get(0), "-theme testing");
            reporter.afterStory(false);
        }

    }

}
