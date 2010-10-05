package org.jbehave.core.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * An abstract StepPatternParser providing to subclasses a common algorithm to implement
 * StepPatternParser using a RegexStepMatcher.<br/>
 * 
 * The algorithm consists in : <ul>
 * <li>stepPatternPreprocessing : Transforming the stepPattern sentences into a valid regex by escaping regex special characters but 
 * 	keeping the parameter references. (overwritable by subclasses)</li>
 * <li>findParametersToReplace : Finding the parameters to replace in the stepPattern (to be provided by subclasses)</li>
 * <li>replaceParametersWithCapture : Replacing the parameters by regex capture (overwritable by sublcasses)</li>
 * <li>anyWhitespaceWillDo: Allow any number and type of spaces when the pattern contained a single space (overwritable by sublcasses)</li>
 * </ul>
 */
public abstract class RegexPatternParserTemplate implements StepPatternParser {

    public StepMatcher parseStep(String stepPattern) {
	String matchThisButLeaveBrackets = stepPatternPreprocessing(stepPattern);
	List<Parameter> parameters = findParametersToReplace(matchThisButLeaveBrackets);
	String patternToMatchAgainst = replaceParametersWithCapture(matchThisButLeaveBrackets, parameters);
	String matchThisButIgnoreWhitespace = anyWhitespaceWillDo(patternToMatchAgainst);
	Pattern pattern = Pattern.compile(matchThisButIgnoreWhitespace, Pattern.DOTALL);
	List<String> names = new ArrayList<String>();
	for (Parameter parameter : parameters) {
	    names.add(parameter.name);
	}
	String[] paramNames = names.toArray(new String[names.size()]);
	return new RegexStepMatcher(pattern, paramNames);
    }

    
    
    /**
     * Transform the given stepPattern into a valid regex (still containing the parameter reference).</br>
     * Default implementation simply escapes regex special characters.
     */
    protected String stepPatternPreprocessing(String stepPattern) {
	return stepPattern.replaceAll("([\\[\\]\\{\\}\\?\\^\\.\\*\\(\\)\\+\\\\])", "\\\\$1");
    }

    
    /**
     * Find all parameters in the escapedStepPattern.
     */
    protected abstract List<Parameter> findParametersToReplace(String escapedStepPattern);

    
    /**
     * Transform regex so that any sequence of spaces is authorized when the stepPattern contains at least one space.
     */
    protected String anyWhitespaceWillDo(String regex) {
	return regex.replaceAll("\\s+", "\\\\s+");
    }

    /**
     * Replace the parameters by regex capture.
     * Default implementation replace each parameter by the "(.*)" regex.
     */
    protected String replaceParametersWithCapture(String escapedMatch, List<Parameter> parameters) {
	String replaced = escapedMatch;
	for (int i = parameters.size(); i > 0; i--) {
	    String start = replaced.substring(0, parameters.get(i - 1).start);
	    String end = replaced.substring(parameters.get(i - 1).end);
	    String whitespaceIfAny = parameters.get(i - 1).whitespaceIfAny;
	    replaced = start + "(.*)" + whitespaceIfAny + end;
	}
	return replaced;
    }

    protected class Parameter {
	protected final int start;
	protected final int end;
	protected final String whitespaceIfAny;
	protected final String name;
	
	public Parameter(int start, int end, String whitespaceIfAny, String name) {
	    this.start = start;
	    this.end = end;
	    this.whitespaceIfAny = whitespaceIfAny;
	    this.name = name;
	}
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
