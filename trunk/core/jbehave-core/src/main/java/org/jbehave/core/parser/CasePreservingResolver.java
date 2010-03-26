package org.jbehave.core.parser;

import org.jbehave.core.RunnableScenario;


/**
 * <p>
 * Resolves core names while preserving the Java core class case eg:
 * "org.jbehave.core.ICanLogin.java" -> "org/jbehave/core/ICanLogin".
 * </p>
 * <p>
 * By default no extension is used, but this can be configured via the
 * constructor so that we can resolve name to eg
 * "org/jbehave/core/ICanLogin.core".
 * </p>
 */
public class CasePreservingResolver extends AbstractScenarioNameResolver {

    public CasePreservingResolver() {
        super();
    }

    public CasePreservingResolver(String extension) {
    	super(extension);
    }

	@Override
	protected String resolveFileName(Class<? extends RunnableScenario> scenarioClass) {
		return scenarioClass.getSimpleName();
	}

}
