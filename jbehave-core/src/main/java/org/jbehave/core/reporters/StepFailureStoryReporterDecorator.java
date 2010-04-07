package org.jbehave.core.reporters;

import java.util.List;
import java.util.Map;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Story;
import org.jbehave.core.errors.StepFailure;

/**
 * <p>
 * When a step fails, the {@link Throwable} that caused the failure is wrapped
 * in a {@link StepFailure} together with the step during which the failure
 * occurred. If such a failure occurs it will throw the {@link StepFailure}
 * after the story is finished.
 * </p>
 * 
 * @see StepFailure
 */
public class StepFailureStoryReporterDecorator implements StoryReporter {

	private final StoryReporter delegate;
	private StepFailure failure;

	public StepFailureStoryReporterDecorator(StoryReporter delegate) {
		this.delegate = delegate;
	}

	public void afterScenario() {
		delegate.afterScenario();
	}

	public void afterStory(boolean embeddedStory) {
		delegate.afterStory(embeddedStory);
		if (failure != null) {
			throw failure;
		}
	}

    public void beforeScenario(String title) {
		delegate.beforeScenario(title);
	}

    public void beforeStory(Story story, boolean embeddedStory) {
        failure = null;
        delegate.beforeStory(story, embeddedStory);
    }

	public void failed(String step, Throwable cause) {
		failure = new StepFailure(step, cause);
		delegate.failed(step, failure);
	}

    public void ignorable(String step) {
        delegate.ignorable(step);
    }
    
	public void notPerformed(String step) {
		delegate.notPerformed(step);
	}

	public void pending(String step) {
		delegate.pending(step);
	}

	public void successful(String step) {
		delegate.successful(step);
	}

	public void givenStoryPaths(List<String> givenStoryPaths) {
		delegate.givenStoryPaths(givenStoryPaths);
	}

	public void beforeExamples(List<String> steps, ExamplesTable table) {
		delegate.beforeExamples(steps, table);
	}

	public void example(Map<String, String> tableRow) {
		delegate.example(tableRow);
	}

    public void afterExamples() {
        delegate.afterExamples();        
    }

}
