package org.jbehave.examples.trader.scenarios;

import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import java.util.Calendar;

import org.jbehave.core.parser.*;
import org.jbehave.examples.trader.converters.CalendarConverter;
import org.jbehave.core.JUnitStory;
import org.jbehave.core.PropertyBasedConfiguration;
import org.jbehave.core.RunnableStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.core.steps.StepsConfiguration;
import org.jbehave.core.steps.StepsFactory;

public class ClaimsWithNullCalendar extends JUnitStory {

    private static StoryNameResolver converter = new UnderscoredCamelCaseResolver(".scenario");

    public ClaimsWithNullCalendar(){
        this(ClaimsWithNullCalendar.class);
    }

    public ClaimsWithNullCalendar(final Class<? extends RunnableStory> scenarioClass) {
        super(new PropertyBasedConfiguration() {
            @Override
            public StoryDefiner forDefiningStories() {
                return new ClasspathStoryDefiner(converter, new PatternStoryParser(keywords()));
            }

            @Override
            public StoryReporter forReportingStories() {
                return new StoryReporterBuilder(new FilePrintStreamFactory(scenarioClass, converter))
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
        		monitor, new CalendarConverter("dd/MM/yyyy"))); 
		configuration.useMonitor(monitor);        
        addSteps(new StepsFactory(configuration).createCandidateSteps(new CalendarSteps()));
    }

    public static class CalendarSteps {
     

        @Given("a plan with calendar date of <date>") 
        public void aPlanWithCalendar(@Named("date") Calendar calendar) {
            System.out.println(calendar);
        }
        
        @Then("the claimant should receive an amount of <amount>")         
        public void theClaimantReceivesAmount(@Named("amount") double amount) {
            System.out.println(amount);
        }

    }

}
