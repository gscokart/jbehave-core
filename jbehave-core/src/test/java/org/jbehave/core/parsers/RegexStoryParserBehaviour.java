package org.jbehave.core.parsers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.util.List;

import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.model.Description;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.junit.Test;


public class RegexStoryParserBehaviour {

    private static final String NL = "\n";
    private StoryParser parser = new RegexStoryParser(new LocalizedKeywords());
    private String storyPath = "path/to/my.story";

    @Test
    public void shouldParseStoryAndProvideNameFromPath() {
        Story story = parser.parseStory("", storyPath);
        assertThat(story.getPath(), equalTo(storyPath));
        assertThat(story.getName(), equalTo(new File(storyPath).getName()));
    }

    @Test
    public void shouldParseStoryAndProvideEmptyNameWhenPathIsNull() {
        Story story = parser.parseStory("", null);
        assertThat(story.getPath(), equalTo(""));
        assertThat(story.getName(), equalTo(story.getPath()));
    }

    @Test
    public void shouldParseStoryWithMeta() {
        String wholeStory = "Meta: @theme parsing @ignore true" + NL +
                "Scenario: " + NL +
                "Meta: @author Mauro" + NL +
                "Given a scenario " + NL +
                "When I parse it" + NL +
                "Then I should get steps";
        Story story = parser.parseStory(
                wholeStory, storyPath);
        assertThat(story.getPath(), equalTo(storyPath));
        Meta storyMeta = story.getMeta();
        assertThat(storyMeta.getProperty("theme"), equalTo("parsing"));
        assertThat(storyMeta.getProperty("ignore"), equalTo("true"));
        assertThat(storyMeta.getProperty("unknown"), equalTo(""));        
        Scenario scenario = story.getScenarios().get(0);
        Meta scenarioMeta = scenario.getMeta();
        assertThat(scenarioMeta.getProperty("author"), equalTo("Mauro"));
    }
    
    @Test
    public void shouldAllowSpacesInMetaProperties() {
        String wholeStory = "Meta: @ theme parsing @ ignore true" + NL +
                "Scenario: " + NL +
                "Meta: @authors Mauro Paul" + NL +
                "Given a scenario " + NL +
                "When I parse it" + NL +
                "Then I should get steps";
        Story story = parser.parseStory(
                wholeStory, storyPath);
        assertThat(story.getPath(), equalTo(storyPath));
        Meta storyMeta = story.getMeta();
        assertThat(storyMeta.getProperty("theme"), equalTo("parsing"));
        assertThat(storyMeta.getProperty("ignore"), equalTo("true"));
        assertThat(storyMeta.getProperty("unknown"), equalTo(""));        
        Scenario scenario = story.getScenarios().get(0);
        Meta scenarioMeta = scenario.getMeta();
        assertThat(scenarioMeta.getProperty("authors"), equalTo("Mauro Paul"));
    }
    
    @Test
    public void shouldParseStoryWithSimpleSteps() {
        String wholeStory = "Given a scenario" + NL +
                "!-- ignore me" + NL +
                "When I parse it" + NL +
                "Then I should get steps";
        Story story = parser.parseStory(
                wholeStory, storyPath);
        assertThat(story.getPath(), equalTo(storyPath));
        List<String> steps = story.getScenarios().get(0).getSteps();
        assertThat(steps.get(0), equalTo("Given a scenario"));
        assertThat(steps.get(1), equalTo("!-- ignore me"));
        assertThat(steps.get(2), equalTo("When I parse it"));
        assertThat(steps.get(3), equalTo("Then I should get steps"));
    }

    @Test
    public void shouldParseStoryWithStepsContainingKeywordsAtStartOfOtherWords() {
        String wholeStory = "Given a scenario Givenly" + NL +
                "When I parse it to Whenever" + NL +
                "And I parse it to Anderson" + NL +
                "!-- ignore me too" + NL +
                "Then I should get steps Thenact";
        Story story = parser.parseStory(
                wholeStory, storyPath);

        List<String> steps = story.getScenarios().get(0).getSteps();
        assertThat(steps.get(0), equalTo("Given a scenario Givenly"));
        assertThat(steps.get(1), equalTo("When I parse it to Whenever"));
        assertThat(steps.get(2), equalTo("And I parse it to Anderson"));
        assertThat(steps.get(3), equalTo("!-- ignore me too"));
        assertThat(steps.get(4), equalTo("Then I should get steps Thenact"));
    }

    @Test
    public void shouldParseStoryWithScenarioTitleGivenStoriesAndStepsContainingKeywordsNotAtStartOfLine() {
        String wholeStory = "Scenario: Show that we have Given/When/Then as part of description or step content"+ NL +
                "GivenStories: GivenAStoryContainingAKeyword" + NL +
                "Given a scenario Given" + NL +
                "When I parse it to When" + NL +
                "And I parse it to And" + NL +
                "!-- And ignore me too" + NL +
                "Then I should get steps Then" + NL +
                "Examples:" + NL +
                "|Given|When|Then|And|" + NL +
                "|Dato che|Quando|Allora|E|" + NL +
                "|Dado que|Quando|Então|E|";
        Story story = parser.parseStory(
                wholeStory, storyPath);

        Scenario scenario = story.getScenarios().get(0);
        assertThat(scenario.getTitle(), equalTo("Show that we have Given/When/Then as part of description or step content"));
        assertThat(scenario.getGivenStoryPaths(), equalTo(asList("GivenAStoryContainingAKeyword")));
        List<String> steps = scenario.getSteps();
        assertThat(steps.get(0), equalTo("Given a scenario Given"));
        assertThat(steps.get(1), equalTo("When I parse it to When"));
        assertThat(steps.get(2), equalTo("And I parse it to And"));
        assertThat(steps.get(3), equalTo("!-- And ignore me too"));
        assertThat(steps.get(4), equalTo("Then I should get steps Then"));
        
        assertThat(story.getScenarios().get(0).getExamplesTable().asString(), 
                    equalTo("|Given|When|Then|And|" + NL +
                            "|Dato che|Quando|Allora|E|" + NL +
                            "|Dado que|Quando|Então|E|"));
    }

    @Test
    public void shouldParseStoryWithMultilineSteps() {
        String wholeStory = "Given a scenario" + NL +
                "with this line" + NL +
                "When I parse it" + NL +
                "with another line" + NL + NL +
                "Then I should get steps" + NL +
                "without worrying about lines" + NL +
                "or extra white space between or after steps" + NL + NL;
        Story story = parser.parseStory(wholeStory, storyPath);

        List<String> steps = story.getScenarios().get(0).getSteps();

        assertThat(steps.get(0), equalTo("Given a scenario" + NL +
                "with this line"));
        assertThat(steps.get(1), equalTo("When I parse it" + NL +
                "with another line"));
        assertThat(steps.get(2), equalTo("Then I should get steps" + NL +
                "without worrying about lines" + NL +
                "or extra white space between or after steps"));
    }

    @Test
    public void shouldParseStoryWithMultilineScenarioTitle() {
        String wholeStory = "Scenario: A title\n that is spread across\n multiple lines" + NL + NL +
                "Given a step that's pending" + NL +
                "When I run the scenario" + NL +
                "Then I should see this in the output";

        Story story = parser.parseStory(wholeStory, null);

        assertThat(story.getScenarios().get(0).getTitle(), equalTo("A title\n that is spread across\n multiple lines"));
    }

    @Test
    public void shouldParseWithMultipleScenarios() {
        String wholeStory = "Scenario: the first scenario " + NL + NL +
                "Given my scenario" + NL + NL +
                "Scenario: the second scenario" + NL + NL +
                "Given my second scenario";
        Story story = parser.parseStory(wholeStory, storyPath);

        assertThat(story.getScenarios().get(0).getTitle(), equalTo("the first scenario"));
        assertThat(story.getScenarios().get(0).getSteps(), equalTo(asList("Given my scenario")));
        assertThat(story.getScenarios().get(1).getTitle(), equalTo("the second scenario"));
        assertThat(story.getScenarios().get(1).getSteps(), equalTo(asList("Given my second scenario")));
    }

    @Test
    public void shouldParseStoryWithDescriptionAndNarrative() {
        String wholeStory = "Story: This is free-text description"+ NL +
                "Narrative: This bit of text is ignored" + NL +
                "In order to renovate my house" + NL +
                "As a customer" + NL +
                "I want to get a loan" + NL +
                "Scenario:  A first scenario";
        Story story = parser.parseStory(
                wholeStory, storyPath);
        Description description = story.getDescription();
        assertThat(description.asString(), equalTo("Story: This is free-text description"));
        Narrative narrative = story.getNarrative();
        assertThat(narrative.isEmpty(), not(true));
        assertThat(narrative.inOrderTo().toString(), equalTo("renovate my house"));
        assertThat(narrative.asA().toString(), equalTo("customer"));
        assertThat(narrative.iWantTo().toString(), equalTo("get a loan"));
    }

    @Test
    public void shouldParseStoryWithIncompleteNarrative() {
        String wholeStory = "Story: This is free-text description"+ NL +
                "Narrative: This is an incomplete narrative" + NL +
                "In order to renovate my house" + NL +
                "As a customer" + NL +
                "Scenario:  A first scenario";
        Story story = parser.parseStory(
                wholeStory, storyPath);
        Description description = story.getDescription();
        assertThat(description.asString(), equalTo("Story: This is free-text description"));
        Narrative narrative = story.getNarrative();
        assertThat(narrative.isEmpty(), is(true));
    }

    @Test
    public void shouldParseStoryWithAllElements() {
        String wholeStory = "This is just a story description" + NL + NL +

                "Narrative: " + NL +
                "In order to see what we're not delivering" + NL + NL +
                "As a developer" + NL +
                "I want to see the narrative for my story when a scenario in that story breaks" + NL +

                "Scenario: A pending scenario" + NL + NL +
                "Given a step that's pending" + NL +
                "When I run the scenario" + NL +
                "!-- A comment between steps" + NL +
                "Then I should see this in the output" + NL +

                "Scenario: A passing scenario" + NL +
                "Given I'm not reporting passing stories" + NL +
                "When I run the scenario" + NL +
                "Then this should not be in the output" + NL +

                "Scenario: A failing scenario" + NL +
                "Given a step that fails" + NL +
                "When I run the scenario" + NL +
                "Then I should see this in the output" + NL +
                "And I should see this in the output" + NL;

        Story story = parser.parseStory(wholeStory, storyPath);

        assertThat(story.toString(), containsString("This is just a story description"));
        assertThat(story.getDescription().asString(), equalTo("This is just a story description"));

        assertThat(story.toString(), containsString("Narrative"));
        assertThat(story.getNarrative().inOrderTo(), equalTo("see what we're not delivering"));
        assertThat(story.getNarrative().asA(), equalTo("developer"));
        assertThat(story.getNarrative().iWantTo(), equalTo("see the narrative for my story when a scenario in that story breaks"));

        assertThat(story.toString(), containsString("A pending scenario"));
        assertThat(story.getScenarios().get(0).getTitle(), equalTo("A pending scenario"));
        assertThat(story.getScenarios().get(0).getGivenStoryPaths().size(), equalTo(0));
        assertThat(story.getScenarios().get(0).getSteps(), equalTo(asList(
                "Given a step that's pending",
                "When I run the scenario",
                "!-- A comment between steps",
                "Then I should see this in the output"
        )));

        assertThat(story.toString(), containsString("A passing scenario"));
        assertThat(story.getScenarios().get(1).getTitle(), equalTo("A passing scenario"));
        assertThat(story.getScenarios().get(1).getGivenStoryPaths().size(), equalTo(0));
        assertThat(story.getScenarios().get(1).getSteps(), equalTo(asList(
                "Given I'm not reporting passing stories",
                "When I run the scenario",
                "Then this should not be in the output"
        )));

        assertThat(story.toString(), containsString("A failing scenario"));
        assertThat(story.getScenarios().get(2).getTitle(), equalTo("A failing scenario"));
        assertThat(story.getScenarios().get(2).getGivenStoryPaths().size(), equalTo(0));
        assertThat(story.getScenarios().get(2).getSteps(), equalTo(asList(
                "Given a step that fails",
                "When I run the scenario",
                "Then I should see this in the output",
                "And I should see this in the output"
        )));
    }
    
    public void shouldParseStoryWithVeryLongStep() {
        String scenario = aScenarioWithAVeryLongGivenStep();
        ensureThatScenarioCanBeParsed(scenario);
    }

    private String aScenarioWithAVeryLongGivenStep() {
        StringBuilder longScenarioBuilder = new StringBuilder()
                .append("Given all these examples:" + NL)
                .append("|one|two|three|" + NL);
        int numberOfLinesInStep = 50;
        for (int i = 0; i < numberOfLinesInStep; i++) {
            longScenarioBuilder.append("|a|sample|line|" + NL);
        }
        longScenarioBuilder.append("When I do something" + NL);
        longScenarioBuilder.append("Then something should happen" + NL);
        return longScenarioBuilder.toString();
    }

    private void ensureThatScenarioCanBeParsed(String scenarioAsText) {
        parser.parseStory(scenarioAsText);
    }

    @Test
    public void shouldParseLongStory() {
         String aGivenWhenThen =
                "Given a step" + NL +
                "When I run it" + NL +
                "Then I should seen an output" + NL;

        StringBuffer aScenario = new StringBuffer();

        aScenario.append("Scenario: A long scenario").append(NL);
        int numberOfGivenWhenThensPerScenario = 50;
        for (int i = 0; i < numberOfGivenWhenThensPerScenario; i++) {
            aScenario.append(aGivenWhenThen);
        }

        int numberOfScenarios = 100;
        StringBuffer wholeStory = new StringBuffer();
        wholeStory.append("Story: A very long story").append(NL);
        for (int i = 0; i < numberOfScenarios; i++) {
            wholeStory.append(aScenario).append(NL);
        }

        Story story = parser.parseStory(wholeStory.toString(), null);
        assertThat(story.getScenarios().size(), equalTo(numberOfScenarios));
        for (Scenario scenario : story.getScenarios()) {
            assertThat(scenario.getSteps().size(), equalTo(numberOfGivenWhenThensPerScenario * 3));
        }
    }

    @Test
    public void shouldParseStoryWithScenarioContainingExamplesTable() {
        String wholeStory = "Scenario: A scenario with examples table" + NL + NL +
                "Given a step with a <one>" + NL +
                "When I run the scenario of name <two>" + NL +
                "Then I should see <three> in the output" + NL +

                "Examples:" + NL +
                "|one|two|three|" + NL +
                "|11|12|13|" + NL +
                "|21|22|23|";

        Story story = parser.parseStory(wholeStory, storyPath);

        Scenario scenario = story.getScenarios().get(0);
        assertThat(scenario.getTitle(), equalTo("A scenario with examples table"));
        assertThat(scenario.getGivenStoryPaths().size(), equalTo(0));
        assertThat(scenario.getSteps(), equalTo(asList(
                "Given a step with a <one>",
                "When I run the scenario of name <two>",
                "Then I should see <three> in the output"
        )));
        ExamplesTable table = scenario.getExamplesTable();
        assertThat(table.asString(), equalTo(
                "|one|two|three|" + NL +
                        "|11|12|13|" + NL +
                        "|21|22|23|"));
        assertThat(table.getRowCount(), equalTo(2));
        assertThat(table.getRow(0), not(nullValue()));
        assertThat(table.getRow(0).get("one"), equalTo("11"));
        assertThat(table.getRow(0).get("two"), equalTo("12"));
        assertThat(table.getRow(0).get("three"), equalTo("13"));
        assertThat(table.getRow(1), not(nullValue()));
        assertThat(table.getRow(1).get("one"), equalTo("21"));
        assertThat(table.getRow(1).get("two"), equalTo("22"));
        assertThat(table.getRow(1).get("three"), equalTo("23"));
    }

    @Test
    public void shouldParseStoryWithScenarioContainingGivenStories() {
    	// given stories as CSV with no spaces or newlines
        parseStoryWithGivenStories(
        		"GivenStories: path/to/one,path/to/two" + NL + NL +
				"Given a step");
        // given stories as CSV with spaces and newlines
        parseStoryWithGivenStories(
        		"GivenStories: path/to/one , "+ NL +" path/to/two" + NL + NL +
				"Given a step");
    }

	private void parseStoryWithGivenStories(String wholeStory) {
		Story story = parser.parseStory(wholeStory, storyPath);

        Scenario scenario = story.getScenarios().get(0);
        assertThat(scenario.getGivenStoryPaths(), equalTo(asList(
                "path/to/one",
                "path/to/two")));
        assertThat(scenario.getSteps(), equalTo(asList(
                "Given a step"
        )));
	}

    @Test
    public void shouldParseStoryWithoutAPath() {
        String wholeStory = "Given a step" + NL +
                "When I run it" + NL +
                "Then I should an output";

        Story story = parser.parseStory(wholeStory);

        assertThat(story.getPath(), equalTo(""));
        Scenario scenario = story.getScenarios().get(0);
        assertThat(scenario.getSteps(), equalTo(asList(
                "Given a step",
                "When I run it",
                "Then I should an output"
        )));

    }
}
