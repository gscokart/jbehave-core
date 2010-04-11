package org.jbehave.core;

import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateStep;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.MostUsefulStepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.*;

public class StoryEmbedder {
    private StoryRunnerMonitor runnerMonitor;
    private StoryRunnerMode runnerMode;
    private StoryRunner runner = new StoryRunner();

    public StoryEmbedder() {
        this(new PrintStreamRunnerMonitor(), new StoryRunnerMode());
    }

    public StoryEmbedder(StoryRunnerMonitor runnerMonitor, StoryRunnerMode runnerMode) {
        this.runnerMonitor = runnerMonitor;
        this.runnerMode = runnerMode;
    }

    public void run(List<RunnableStory> runnableStories) {
        if (runnerMode.skip()) {
            runnerMonitor.storiesNotRun();
            return;
        }

        Map<String, Throwable> failedStories = new HashMap<String, Throwable>();
        for (RunnableStory story : runnableStories) {
            String storyName = story.getClass().getName();
            try {
                runnerMonitor.runningStory(storyName);
                story.run();
            } catch (Throwable e) {
                if (runnerMode.batch()) {
                    // collect and postpone decision to throw exception
                    failedStories.put(storyName, e);
                } else {
                    if (runnerMode.ignoreFailure()) {
                        runnerMonitor.storyFailed(storyName, e);
                    } else {
                        throw new RunningStoriesFailedException("Failed to run story " + storyName, e);
                    }
                }
            }
        }

        if (runnerMode.batch() && failedStories.size() > 0) {
            if (runnerMode.ignoreFailure()) {
                runnerMonitor.storiesBatchFailed(format(failedStories));
            } else {
                throw new RunningStoriesFailedException("Failed to run stories in batch: " + format(failedStories));
            }
        }

    }

    public void runStoriesAsPaths(List<String> storyPaths) {
        if (runnerMode.skip()) {
            runnerMonitor.storiesNotRun();
            return;
        }

        Map<String, Throwable> failedStories = new HashMap<String, Throwable>();
        for (String storyPath : storyPaths) {
            try {
                runnerMonitor.runningStory(storyPath);
                StoryReporter storyReporter = storyReporter(storyPath, storyReporterFormats());
                StoryConfiguration configuration = configuration();
                configuration.addStoryReporter(storyPath, storyReporter);                
                runner.run(configuration, candidateSteps(), storyPath);
            } catch (Throwable e) {
                if (runnerMode.batch()) {
                    // collect and postpone decision to throw exception
                    failedStories.put(storyPath, e);
                } else {
                    if (runnerMode.ignoreFailure()) {
                        runnerMonitor.storyFailed(storyPath, e);
                    } else {
                        throw new RunningStoriesFailedException("Failed to run story " + storyPath, e);
                    }
                }
            }
        }

        if (runnerMode.batch() && failedStories.size() > 0) {
            if (runnerMode.ignoreFailure()) {
                runnerMonitor.storiesBatchFailed(format(failedStories));
            } else {
                throw new RunningStoriesFailedException("Failed to run stories in batch: " + format(failedStories));
            }
        }

    }

    public StoryConfiguration configuration() {
        return new MostUsefulStoryConfiguration();
    }

    public List<CandidateSteps> candidateSteps() {
        return asList(new StepsFactory(new MostUsefulStepsConfiguration()).createCandidateSteps(new CandidateStep[]{}));
    }

    protected StoryReporter storyReporter(String storyPath, StoryReporterBuilder.Format... formats) {
        StoryReporterBuilder builder = new StoryReporterBuilder(new FilePrintStreamFactory(storyPath));
        for (StoryReporterBuilder.Format format : formats) {
            builder = builder.with(format);
        }
        return builder.build();
    }

    protected StoryReporterBuilder.Format[] storyReporterFormats(){
        return new StoryReporterBuilder.Format[]{CONSOLE};
    }

    public void useRunnerMode(StoryRunnerMode runnerMode) {
        this.runnerMode = runnerMode;
    }

    public void useRunnerMonitor(StoryRunnerMonitor runnerMonitor) {
        this.runnerMonitor = runnerMonitor;
    }

    private String format(Map<String, Throwable> failedStories) {
        StringBuffer sb = new StringBuffer();
        for (String storyName : failedStories.keySet()) {
            Throwable cause = failedStories.get(storyName);
            sb.append("\n");
            sb.append(storyName);
            sb.append(": ");
            sb.append(cause.getMessage());
        }
        return sb.toString();
    }

    private class RunningStoriesFailedException extends RuntimeException {
        public RunningStoriesFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        public RunningStoriesFailedException(String message) {
            super(message);
        }
    }


}
