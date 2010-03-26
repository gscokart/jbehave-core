package org.jbehave.core.steps;

import java.util.Map;

import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;

/**
 * Represents the strategy for the creation of executable {@link Step}s from a
 * given story or core model matching a list of {@link CandidateSteps}.
 */
public interface StepCreator {

    enum Stage {
        BEFORE, AFTER
    };

    Step[] createStepsFrom(Story story, Stage stage, boolean embeddedStory, CandidateSteps... candidateSteps);

    Step[] createStepsFrom(Scenario scenario, Map<String, String> tableRow,
            CandidateSteps... candidateSteps);

}
