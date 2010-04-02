package org.jbehave.examples.trader.stories;

import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import java.util.Calendar;

import org.jbehave.core.*;
import org.jbehave.core.parser.*;
import org.jbehave.core.steps.*;
import org.jbehave.examples.trader.converters.CalendarConverter;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.parser.PatternStoryParser;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;

public class ClaimsWithNullCalendar extends JUnitStory {

    public ClaimsWithNullCalendar() {
        // here we show how we can override the default configuration via an inner class
        final StoryNameResolver nameResolver = new UnderscoredCamelCaseResolver(".story");
        StoryConfiguration storyConfiguration = new MostUsefulStoryConfiguration(){
            @Override
            public StoryDefiner storyDefiner() {
                return new ClasspathStoryDefiner(new PatternStoryParser(keywords()), this.getClass().getClassLoader());
            }

            @Override
            public StoryReporter storyReporter() {
                return new StoryReporterBuilder(new FilePrintStreamFactory(ClaimsWithNullCalendar.class, nameResolver))
                .with(CONSOLE)
                .with(TXT)
                .with(HTML)
                .with(XML)
                .build();
            }
        };
        storyConfiguration.useStoryNameResolver(nameResolver);               
        useConfiguration(storyConfiguration);

        StepsConfiguration stepsConfiguration = new MostUsefulStepsConfiguration();
        StepMonitor monitor = new SilentStepMonitor();
		stepsConfiguration.useParameterConverters(new ParameterConverters(
        		monitor, new CalendarConverter("dd/MM/yyyy"))); 
		stepsConfiguration.useMonitor(monitor);
        addSteps(new StepsFactory(stepsConfiguration).createCandidateSteps(new CalendarSteps()));
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
