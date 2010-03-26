package org.jbehave.core.parser.scenarios;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.JUnitScenario;
import org.jbehave.core.reporters.PrintStreamScenarioReporter;

public class MyPendingScenario extends JUnitScenario {

    public MyPendingScenario() {
        // Making sure this doesn't output to the build while it's running
        super(new PropertyBasedConfiguration() {
            @Override
            public PrintStreamScenarioReporter forReportingScenarios() {
                return new PrintStreamScenarioReporter(new PrintStream(new ByteArrayOutputStream()));
            }
        });
    }
}