package org.jbehave.examples.trader;

import static java.util.Arrays.asList;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.ScenarioReporterBuilder.Format.XML;

import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.examples.trader.service.TradingService;
import org.jbehave.core.JUnitScenario;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.RunnableScenario;
import org.jbehave.core.parser.ClasspathScenarioDefiner;
import org.jbehave.core.parser.PatternScenarioParser;
import org.jbehave.core.parser.PrefixCapturingPatternBuilder;
import org.jbehave.core.parser.ScenarioDefiner;
import org.jbehave.core.parser.ScenarioNameResolver;
import org.jbehave.core.parser.UnderscoredCamelCaseResolver;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.ScenarioReporter;
import org.jbehave.core.reporters.ScenarioReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

public class TraderScenario extends JUnitScenario {

    private static ScenarioNameResolver resolver = new UnderscoredCamelCaseResolver(".scenario");

    public TraderScenario(final Class<? extends RunnableScenario> scenarioClass) {
        super(new PropertyBasedConfiguration() {
            @Override
            public ScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(resolver, new PatternScenarioParser(keywords()));
            }

            @Override
            public ScenarioReporter forReportingScenarios() {
                return new ScenarioReporterBuilder(new FilePrintStreamFactory(scenarioClass, resolver))
                            .with(CONSOLE)
                            .with(TXT)
                            .with(HTML)
                            .with(XML)
                            .build();
            }

        });

        StepsConfiguration configuration = new StepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
		configuration.useParameterConverters(new ParameterConverters(
        		monitor, new TraderConverter(mockTradePersister())));  // define converter for custom type Trader
        configuration.usePatternBuilder(new PrefixCapturingPatternBuilder("%")); // use '%' instead of '$' to identify parameters
        configuration.useMonitor(monitor);
        
        addSteps(createSteps(configuration));
    }

    protected CandidateSteps[] createSteps(StepsConfiguration configuration) {
        return new StepsFactory(configuration).createCandidateSteps(new TraderSteps(new TradingService()), new BeforeAfterSteps());
    }

    private TraderPersister mockTradePersister() {
        return new TraderPersister(new Trader("Mauro", asList(new Stock("STK1", 10.d))));
    }


}
