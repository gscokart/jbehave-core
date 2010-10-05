package org.jbehave.core.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A step pattern parser that provides a step matcher which will capture
 * parameters starting with the given prefix in any matching step. Default
 * prefix is $.
 * 
 * @author Elizabeth Keogh
 */
public class RegexPrefixCapturingPatternParser extends RegexPatternParserTemplate implements StepPatternParser {

    private final String prefix;

    /**
     * Creates a parser which captures parameters starting with $ in a matching
     * step.
     */
    public RegexPrefixCapturingPatternParser() {
	this("$");
    }

    /**
     * Creates a parser which captures parameters starting with a given prefix
     * in a matching step.
     * 
     * @param prefix
     *            the prefix to use in capturing parameters
     */
    public RegexPrefixCapturingPatternParser(String prefix) {
	this.prefix = prefix;
    }

    public String getPrefix() {
	return prefix;
    }

    @Override
    protected List<Parameter> findParametersToReplace(String escapedStepPattern) {
	List<Parameter> parameters = new ArrayList<Parameter>();
	Matcher findingAllPrefixedWords = findAllPrefixedWords().matcher(escapedStepPattern);
	while (findingAllPrefixedWords.find()) {
	    int start = findingAllPrefixedWords.start();
	    int end = findingAllPrefixedWords.end();
	    String whitespaceIfAny = findingAllPrefixedWords.group(2);
	    String name = escapedStepPattern.substring(start + prefix.length(), end).trim();
	    parameters.add(new Parameter(start, end,whitespaceIfAny,name));
	}
	return parameters;
    }

    private Pattern findAllPrefixedWords() {
	return Pattern.compile("(\\" + prefix + "\\w*)(\\W|\\Z)", Pattern.DOTALL);
    }
}
