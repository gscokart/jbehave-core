package org.jbehave.core.model;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private final String title;
	private final List<String> givenScenarios;
    private final List<String> steps;
	private final ExamplesTable table;

    public Scenario(String title) {
        this("", new ArrayList<String>());
    }

	public Scenario(List<String> steps) {
        this("", steps);
    }

    public Scenario(String title, List<String> steps) {
        this(title, new ArrayList<String>(), new ExamplesTable(""), steps);
    }

    public Scenario(String title, List<String> givenScenarios, List<String> steps) {
        this(title, givenScenarios, new ExamplesTable(""), steps);
    }

    public Scenario(String title, List<String> givenScenarios, ExamplesTable table, List<String> steps) {
    	this.title = title;
		this.givenScenarios = givenScenarios;
    	this.steps = steps;
		this.table = table;
    }

    public Scenario(String title, ExamplesTable table, String... steps) {
        this(title, new ArrayList<String>(), table, asList(steps));
    }

    public List<String> getGivenScenarios() {
		return givenScenarios;
	}

	public List<String> getSteps() {
        return steps;
    }

    public String getTitle() {
        return title;
    }

    public ExamplesTable getTable(){
    	return table;
    }
    
}
