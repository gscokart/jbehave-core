package org.jbehave.core.steps;

import com.thoughtworks.paranamer.Paranamer;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.parser.StepPatternBuilder;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.jbehave.Ensure.ensureThat;

public class ImmutableStepsConfigurationBehaviour {

    @Test
    public void shouldProvideDelegateConfigurationElements() {
        StepsConfiguration delegate = new MostUsefulStepsConfiguration();
        StepsConfiguration immutable = new ImmutableStepsConfiguration(delegate);
        ensureThat(immutable.keywords(), is(delegate.keywords()));
        ensureThat(immutable.monitor(), is(delegate.monitor()));
        ensureThat(immutable.paranamer(), is(delegate.paranamer()));
        ensureThat(immutable.patternBuilder(), is(delegate.patternBuilder()));
        ensureThat(immutable.parameterConverters(), is(delegate.parameterConverters()));
    }


    @Test
    public void shouldNotAllowMutabilityOfConfigurationElements() throws NoSuchMethodException, IllegalAccessException {
        StepsConfiguration delegate = new MostUsefulStepsConfiguration();
        StepsConfiguration immutable = new ImmutableStepsConfiguration(delegate);
        ensureThatNotAllowed(immutable, "useKeywords", KeyWords.class);
        ensureThatNotAllowed(immutable, "useMonitor", StepMonitor.class);
        ensureThatNotAllowed(immutable, "useParanamer", Paranamer.class);
        ensureThatNotAllowed(immutable, "usePatternBuilder", StepPatternBuilder.class);
        ensureThatNotAllowed(immutable, "useParameterConverters", ParameterConverters.class);
    }

    private void ensureThatNotAllowed(StepsConfiguration immutable, String methodName, Class<?> type) throws NoSuchMethodException, IllegalAccessException {
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