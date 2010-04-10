package org.jbehave.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoriesRunner {
    private RunnerMonitor runnerMonitor;
    private RunnerMode runnerMode;

    public StoriesRunner(RunnerMonitor runnerMonitor, RunnerMode runnerMode) {
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
                        throw new RunningStoriesFailedException("Failed to run story "+storyName, e);
                    }
                }
            }
        }

        if (runnerMode.batch() && failedStories.size() > 0) {
            if ( runnerMode.ignoreFailure() ){
                 runnerMonitor.batchStoriesFailed(format(failedStories));
            } else {
                throw new RunningStoriesFailedException("Failed to run stories in batch: "+format(failedStories));
            }
        }
        
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
