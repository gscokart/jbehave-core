package org.jbehave.core;

import org.jbehave.core.model.KeyWords;
import org.jbehave.core.errors.ErrorStrategy;
import org.jbehave.core.errors.PendingErrorStrategy;
import org.jbehave.core.parser.ScenarioDefiner;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporter;
import org.jbehave.core.reporters.StepdocReporter;
import org.jbehave.core.steps.StepCreator;
import org.jbehave.core.steps.StepdocGenerator;

/**
 * PropertyBasedConfiguration is backed by MostUsefulConfiguration as default, but has different
 * behaviour if certain system properties are non-null:
 * <ul>
 *   <li>PropertyBasedConfiguration.FAIL_ON_PENDING: uses  PendingErrorStrategy.FAILING as PendingErrorStrategy</li>
 *   <li>PropertyBasedConfiguration.OUTPUT_ALL:  uses PrintStreamScenarioReporter as ScenarioReporter</li>
 * </ul>
 */
public class PropertyBasedConfiguration implements Configuration {

    public static final String FAIL_ON_PENDING = "org.jbehave.failonpending";
    public static final String OUTPUT_ALL = "org.jbehave.outputall";
    private final Configuration defaultConfiguration;
    
    public PropertyBasedConfiguration() {
        this(new MostUsefulConfiguration());
    }

    public PropertyBasedConfiguration(Configuration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    /**
     * If the system property org.jbehave.outputall
     * is set to TRUE, uses a PrintStreamScenarioReporter;
     * otherwise uses the default ScenarioReporter.
     * 
     * Setting org.jbehave.outputall will allow you
     * to see the steps for all scenarios, regardless
     * of whether the scenarios fail.
     */
    public ScenarioReporter forReportingScenarios() {
        if (System.getProperty(OUTPUT_ALL) == null) {
            return defaultConfiguration.forReportingScenarios();
        } else {
            return new PrintStreamScenarioReporter();
        }
    }

    /**
     * Returns the default ScenarioDefiner.
     */
    public ScenarioDefiner forDefiningScenarios() {
        return defaultConfiguration.forDefiningScenarios();
    }

    /**
     * If the system property org.jbehave.failonpending
     * is non-null, returns PendingStepStrategy.FAILING,
     * otherwise returns the defaults.
     * 
     * <p>Setting org.jbehave.failonpending will cause
     * pending steps to throw an error,
     * so you can see if any steps don't match or are
     * still to be implemented.
     */
    public PendingErrorStrategy forPendingSteps() {
        if (System.getProperty(FAIL_ON_PENDING) == null) {
            return defaultConfiguration.forPendingSteps();
        }
        return PendingErrorStrategy.FAILING;
    }

    /**
     * Returns the default StepCreator.
     */
    public StepCreator forCreatingSteps() {
        return defaultConfiguration.forCreatingSteps();
    }

    /**
     * Returns the default ErrorStrategy for handling
     * errors.
     */
    public ErrorStrategy forHandlingErrors() {
        return defaultConfiguration.forHandlingErrors();
    }

    /**
     * Returns the default keywords.
     */
    public KeyWords keywords() {
        return defaultConfiguration.keywords();
    }

	public StepdocGenerator forGeneratingStepdoc() {		
		return defaultConfiguration.forGeneratingStepdoc();
	}

	public StepdocReporter forReportingStepdoc() {
		return defaultConfiguration.forReportingStepdoc();
	}    
	
}
