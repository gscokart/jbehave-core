package org.jbehave.core;

import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.parser.StoryDefiner;
import org.jbehave.core.reporters.StepdocReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepdocGenerator;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.jbehave.Ensure.ensureThat;

public class ImmutableStoryConfigurationBehaviour {

    @Test
    public void shouldProvideDelegateConfigurationElements() {
        StoryConfiguration delegate = new MostUsefulStoryConfiguration();
        StoryConfiguration immutable = new ImmutableStoryConfiguration(delegate);
        ensureThat(immutable.keywords(), is(delegate.keywords()));
        ensureThat(immutable.stepCreator(), is(delegate.stepCreator()));
        ensureThat(immutable.storyDefiner(), is(delegate.storyDefiner()));
        ensureThat(immutable.storyReporter(), is(delegate.storyReporter()));
        ensureThat(immutable.errorStrategy(), is(delegate.errorStrategy()));
        ensureThat(immutable.pendingErrorStrategy(), is(delegate.pendingErrorStrategy()));
        ensureThat(immutable.stepdocGenerator(), is(delegate.stepdocGenerator()));
        ensureThat(immutable.stepdocReporter(), is(delegate.stepdocReporter()));
    }


    @Test
    public void shouldNotAllowMutabilityOfConfigurationElements() throws NoSuchMethodException, IllegalAccessException {
        StoryConfiguration delegate = new MostUsefulStoryConfiguration();
        StoryConfiguration immutable = new ImmutableStoryConfiguration(delegate);
        ensureThatNotAllowed(immutable, "useKeywords", KeyWords.class);
        ensureThatNotAllowed(immutable, "useStepCreator", StepCreator.class);
        ensureThatNotAllowed(immutable, "useStoryDefiner", StoryDefiner.class);
        ensureThatNotAllowed(immutable, "useStoryReporter", StoryReporter.class);
        ensureThatNotAllowed(immutable, "useErrorStrategy", ErrorStrategy.class);
        ensureThatNotAllowed(immutable, "usePendingErrorStrategy", PendingErrorStrategy.class);
        ensureThatNotAllowed(immutable, "useStepdocGenerator", StepdocGenerator.class);
        ensureThatNotAllowed(immutable, "useStepdocReporter", StepdocReporter.class);
    }

    private void ensureThatNotAllowed(StoryConfiguration immutable, String methodName, Class<?> type) throws NoSuchMethodException, IllegalAccessException {
        Method method = immutable.getClass().getMethod(methodName, type);
        try {
            method.invoke(immutable, new Object[]{null});
        } catch (IllegalAccessException e) {
            throw e; // should not occur
        } catch (InvocationTargetException e) {
            // expected
        }
    }

}