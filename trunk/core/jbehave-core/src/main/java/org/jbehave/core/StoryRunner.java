package org.jbehave.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.PendingError;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.Step;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepResult;
import org.jbehave.core.steps.StepCreator.Stage;

/**
 * Allows to run a story and describe the results to the {@link StoryReporter}.
 *
 * @author Elizabeth Keogh
 * @author Mauro Talevi
 * @author Paul Hammant
 */
public class StoryRunner {

    private State state = new FineSoFar();
    private ErrorStrategy currentStrategy;
    private PendingErrorStrategy pendingStepStrategy;
    private StoryReporter reporter;
    private ErrorStrategy errorStrategy;
    private Throwable throwable;
    private StepCreator stepCreator;

    public void run(StoryConfiguration configuration, Class<? extends RunnableStory> storyClass, CandidateSteps... candidateSteps) throws Throwable {
        String storyPath = configuration.storyPathResolver().resolve(storyClass);
        run(configuration, storyPath, candidateSteps);
    }

    public void run(StoryConfiguration configuration, List<String> storyPaths, CandidateSteps... candidateSteps) throws Throwable {
        for (String storyPath : storyPaths) {
            run(configuration, storyPath, candidateSteps);
        }
    }

    public void run(StoryConfiguration configuration, String storyPath, CandidateSteps... candidateSteps) throws Throwable {
        Story story = configuration.storyDefiner().defineStory(storyPath);
        run(configuration, story, candidateSteps);
    }

    public void run(StoryConfiguration configuration, Story story, CandidateSteps... candidateSteps) throws Throwable {
        // always start in a non-embedded mode
        run(configuration, story, false, candidateSteps);
    }

    public void run(StoryConfiguration configuration, String storyPath, boolean embeddedStory, CandidateSteps... candidateSteps) throws Throwable {
        Story story = configuration.storyDefiner().defineStory(storyPath);
        run(configuration, story, embeddedStory, candidateSteps);
    }

    public void run(StoryConfiguration configuration, Story story, boolean embeddedStory, CandidateSteps... candidateSteps) throws Throwable {
        stepCreator = configuration.stepCreator();
        reporter = configuration.storyReporter(story.getPath());
        pendingStepStrategy = configuration.pendingErrorStrategy();
        errorStrategy = configuration.errorStrategy();
        currentStrategy = ErrorStrategy.SILENT;
        throwable = null;

        reporter.beforeStory(story, embeddedStory);
        runStorySteps(story, embeddedStory, StepCreator.Stage.BEFORE, candidateSteps);
        for (Scenario scenario : story.getScenarios()) {
            reporter.beforeScenario(scenario.getTitle());
            runGivenScenarios(configuration, scenario, candidateSteps); // first run any given scenarios, if any
            if (isExamplesTableScenario(scenario)) { // run examples table scenario
                runExamplesTableScenario(configuration, scenario, candidateSteps);
            } else { // run plain old scenario
                runScenarioSteps(configuration, scenario, new HashMap<String, String>(), candidateSteps);
            }
            reporter.afterScenario();
        }
        runStorySteps(story, embeddedStory, StepCreator.Stage.AFTER, candidateSteps);
        reporter.afterStory(embeddedStory);
        currentStrategy.handleError(throwable);
    }

    private void runGivenScenarios(StoryConfiguration configuration,
                                   Scenario scenario, CandidateSteps... candidateSteps)
            throws Throwable {
        List<String> givenScenarios = scenario.getGivenScenarios();
        if (givenScenarios.size() > 0) {
            reporter.givenStories(givenScenarios);
            for (String storyPath : givenScenarios) {
                // run in embedded mode
                run(configuration, storyPath, true, candidateSteps);
            }
        }
    }

    private boolean isExamplesTableScenario(Scenario scenario) {
        ExamplesTable table = scenario.getTable();
        return table != null && table.getRowCount() > 0;
    }

    private void runExamplesTableScenario(StoryConfiguration configuration,
                                          Scenario scenario, CandidateSteps... candidateSteps) {
        ExamplesTable table = scenario.getTable();
        reporter.beforeExamples(scenario.getSteps(), table);
        for (Map<String, String> tableRow : table.getRows()) {
            reporter.example(tableRow);
            runScenarioSteps(configuration, scenario, tableRow, candidateSteps);
        }
        reporter.afterExamples();
    }

    private void runStorySteps(Story story, boolean embeddedStory, Stage stage, CandidateSteps... candidateSteps) {
        runSteps(stepCreator.createStepsFrom(story, stage, embeddedStory, candidateSteps));
    }

    private void runScenarioSteps(StoryConfiguration configuration,
                                  Scenario scenario, Map<String, String> tableRow, CandidateSteps... candidateSteps) {
        runSteps(stepCreator.createStepsFrom(scenario, tableRow, candidateSteps));
    }

    /**
     * Runs a list of steps.
     *
     * @param steps the Steps to run
     */
    private void runSteps(Step[] steps) {
        if (steps == null || steps.length == 0) return;
        state = new FineSoFar();
        for (Step step : steps) {
            state.run(step);
        }
    }

    private class SomethingHappened implements State {
        public void run(Step step) {
            StepResult result = step.doNotPerform();
            result.describeTo(reporter);
        }
    }

    private final class FineSoFar implements State {

        public void run(Step step) {

            StepResult result = step.perform();
            result.describeTo(reporter);
            Throwable thisScenariosThrowable = result.getThrowable();
            if (thisScenariosThrowable != null) {
                state = new SomethingHappened();
                throwable = mostImportantOf(throwable, thisScenariosThrowable);
                currentStrategy = strategyFor(throwable);
            }
        }

        private Throwable mostImportantOf(
                Throwable throwable1,
                Throwable throwable2) {
            return throwable1 == null ? throwable2 :
                    throwable1 instanceof PendingError ? (throwable2 == null ? throwable1 : throwable2) :
                            throwable1;
        }

        private ErrorStrategy strategyFor(Throwable throwable) {
            if (throwable instanceof PendingError) {
                return pendingStepStrategy;
            } else {
                return errorStrategy;
            }
        }
    }

    private interface State {
        void run(Step step);
    }
}
