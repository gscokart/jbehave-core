package org.jbehave.core.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PatternParser which captures arguments starting with "$" (or a configurable prefix)
 * in a matching step and which support optional blocks placed inside "[]"
 */
public class OptionalStepPatternParser extends RegexPrefixCapturingPatternParser implements StepPatternParser {

    private static final Pattern EMPTYABLE_PATTERN = Pattern
	    .compile("\\\\\\[([^\\[\\]]+)\\\\]|\\s+|\\(\\?:[^\\(\\)]+\\)\\?|\\A(?:\\\\\\[)\\z+|\\A(?:\\\\\\])+\\z");
    private static final Pattern OPTIONAL_PATERN = Pattern
	    .compile("(\\s+)?\\\\\\[([^\\[\\]]+)\\\\](\\s+)?");


    public OptionalStepPatternParser() {
    }
    
    public OptionalStepPatternParser(String prefix) {
	super(prefix);
    }


    @Override
    protected String stepPatternPreprocessing(String matchThis) {
        String result = super.stepPatternPreprocessing(matchThis);
        return replaceOptionals(result);
    }
    
    private String replaceOptionals(String matchThisButLeaveBrackets) {
	Matcher matcher = OPTIONAL_PATERN.matcher(matchThisButLeaveBrackets);
	while(matcher.find()) {
	    boolean isPossiblyBegin = isPossiblyEmpty(matchThisButLeaveBrackets
		    .substring(0, matcher.start()));
	    boolean isPossiblyEnd = isPossiblyEmpty(matchThisButLeaveBrackets
		    .substring(matcher.end()));
	    StringBuffer sb = new StringBuffer();
	    if (!isPossiblyBegin && !isPossiblyEnd) {
		matcher.appendReplacement(sb, "(?:(?:$1$2$3)?|\\\\s+)");
	    } else if (!isPossiblyBegin) {
		matcher.appendReplacement(sb, "(?:(?:$1$2\\\\s*)?|\\\\s+)");
	    } else if (!isPossiblyEnd) {
		matcher.appendReplacement(sb, "(?:(?:\\\\s*$2$3)?|\\\\s+)");
	    } else {
		matcher.appendReplacement(sb, "(?:$2)?");
	    }
	    matcher.appendTail(sb);
	    matchThisButLeaveBrackets = sb.toString();
	    matcher = OPTIONAL_PATERN.matcher(matchThisButLeaveBrackets);
	} 
	return matchThisButLeaveBrackets;
    }

    private boolean isPossiblyEmpty(String substring) {
	String next = substring;
	do {
	    substring = next;
	    next = EMPTYABLE_PATTERN.matcher(substring).replaceAll("");
	} while (!next.equals(substring));
	return substring.length() == 0;
    }


}
