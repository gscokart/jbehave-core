package org.jbehave.examples.trader.spring;

import org.jbehave.examples.trader.TraderScenario;
import org.jbehave.core.RunnableScenario;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.spring.SpringApplicationContextFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.beans.factory.ListableBeanFactory;

public class SpringTraderScenario extends TraderScenario {

    public SpringTraderScenario(Class<? extends RunnableScenario> scenarioClass) {
        super(scenarioClass);
    }

    @Override
    protected CandidateSteps[] createSteps(StepsConfiguration configuration) {
        ListableBeanFactory parent = createBeanFactory();
        return new SpringStepsFactory(configuration, parent).createCandidateSteps();
    }

    private ListableBeanFactory createBeanFactory() {
        return new SpringApplicationContextFactory("org/jbehave/examples/trader/spring/steps.xml").getApplicationContext();
    }

}
