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
    protected List<Parameter> findParametersToReplace(String matchThisButLeaveBrackets) {
	List<Parameter> parameters = new ArrayList<Parameter>();
	Matcher findingAllPrefixedWords = findAllPrefixedWords().matcher(matchThisButLeaveBrackets);
	while (findingAllPrefixedWords.find()) {
	    parameters.add(new PrefixParameter(matchThisButLeaveBrackets,
		    findingAllPrefixedWords.start(), findingAllPrefixedWords.end(),
		    findingAllPrefixedWords.group(2)));
	}
	return parameters;
    }

    private Pattern findAllPrefixedWords() {
	return Pattern.compile("(\\" + prefix + "\\w*)(\\W|\\Z)", Pattern.DOTALL);
    }


    private class PrefixParameter extends Parameter {
	public PrefixParameter(String pattern, int start, int end, String whitespaceIfAny) {
	    super(start, end, whitespaceIfAny, pattern.substring(start + prefix.length(), end).trim());
	}

    }
}
