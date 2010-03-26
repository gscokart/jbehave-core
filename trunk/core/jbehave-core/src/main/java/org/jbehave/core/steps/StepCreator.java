package org.jbehave.core.steps;

import java.util.Map;

import org.jbehave.core.definition.ScenarioDefinition;
import org.jbehave.core.definition.StoryDefinition;

/**
 * Represents the strategy for the creation of executable {@link Step}s from a
 * given story or core definition matching a list of {@link CandidateSteps}.
 */
public interface StepCreator {

    enum Stage {
        BEFORE, AFTER
    };

    Step[] createStepsFrom(StoryDefinition storyDefinition, Stage stage, boolean embeddedStory, CandidateSteps... candidateSteps);

    Step[] createStepsFrom(ScenarioDefinition scenarioDefinition, Map<String, String> tableRow,
            CandidateSteps... candidateSteps);

}
