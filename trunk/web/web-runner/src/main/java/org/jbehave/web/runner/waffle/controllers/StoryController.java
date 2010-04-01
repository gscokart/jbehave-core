package org.jbehave.web.runner.waffle.controllers;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.action.annotation.PRG;
import org.codehaus.waffle.menu.Menu;
import org.codehaus.waffle.menu.MenuAwareController;
import org.jbehave.core.StoryConfiguration;
import org.jbehave.core.PropertyBasedStoryConfiguration;
import org.jbehave.core.StoryRunner;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.model.Story;
import org.jbehave.core.parser.StoryParser;
import org.jbehave.core.reporters.PrintStreamStoryReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.steps.CandidateSteps;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class StoryController extends MenuAwareController {

	private final StoryParser storyParser;
	private final StoryRunner storyRunner;
	private final CandidateSteps[] steps;

	private ByteArrayOutputStream outputStream;
	private StoryConfiguration configuration;
	private StoryContext storyContext;

    public StoryController(Menu menu, StoryConfiguration configuration,
			StoryParser storyParser, StoryRunner storyRunner,
			CandidateSteps... steps) {
		super(menu);
		this.storyParser = storyParser;
		this.storyRunner = storyRunner;
		this.steps = steps;
		this.outputStream = new ByteArrayOutputStream();
        final Properties properties = new Properties();
        properties.setProperty("beforeStory", "{0}\n");
        final KeyWords keywords = configuration.keywords();
        final boolean reportErrors = false;
		this.configuration = new PropertyBasedStoryConfiguration(configuration) {
			public StoryReporter storyReporter() {
				return new PrintStreamStoryReporter(new PrintStream(
						outputStream), properties, keywords, reportErrors);
			}
		};
		this.storyContext = new StoryContext();
	}

    @ActionMethod(asDefault = true)
	public void show() {
		// no-op
	}

	@ActionMethod(asDefault = false)
	@PRG(false)
	public void run() {
		if (isNotBlank(storyContext.getInput())) {
			try {
				outputStream.reset();
				storyContext.clearFailureCause();
				storyRunner.run(defineStory(), configuration, true, steps);
			} catch (Throwable e) {
				storyContext.runFailedFor(e);
			}
			storyContext.setOutput(outputStream.toString());
		}
	}

	private Story defineStory() {
		return storyParser.defineStoryFrom(storyContext.getInput());
	}

	public StoryContext getStoryContext() {
		return storyContext;
	}

	public void setStoryContext(StoryContext storyContext) {
		this.storyContext = storyContext;
	}

}
