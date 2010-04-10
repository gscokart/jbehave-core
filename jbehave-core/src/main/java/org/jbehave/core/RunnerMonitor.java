package org.jbehave.core;


public interface RunnerMonitor {
    void batchStoriesFailed(String message);

    void storyFailed(String message, Throwable e);

    void runningStory(String storyName);

    void storiesNotRun();
}
