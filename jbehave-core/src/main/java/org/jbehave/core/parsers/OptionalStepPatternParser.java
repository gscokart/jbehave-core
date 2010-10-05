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
	if (matcher.find()) {
	    boolean isPossiblyBegin = isPossiblyEmpty(matchThisButLeaveBrackets
		    .substring(0, matcher.start()));
	    boolean isPossiblyEnd = isPossiblyEmpty(matchThisButLeaveBrackets
		    .substring(matcher.end()));
	    String r;
	    if (!isPossiblyBegin && !isPossiblyEnd) {
		r = matcher.replaceFirst("(?:(?:$1$2$3)?|\\\\s+)");
	    } else if (!isPossiblyBegin) {
		r = matcher.replaceFirst("(?:(?:$1$2\\\\s*)?|\\\\s+)");
	    } else if (!isPossiblyEnd) {
		r = matcher.replaceFirst("(?:(?:\\\\s*$2$3)?|\\\\s+)");
	    } else {
		r = matcher.replaceFirst("(?:$2)?");
	    }
	    return replaceOptionals(r);
	} else {
	    return matchThisButLeaveBrackets;
	}
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
