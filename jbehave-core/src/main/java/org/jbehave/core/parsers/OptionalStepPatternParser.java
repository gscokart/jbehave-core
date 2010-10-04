package org.jbehave.core.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PatternParser which captures arguments starting with $ in a matching step and
 * which support optional blocks placed inside "[]"
 */

public class OptionalStepPatternParser implements StepPatternParser {

    private static final Pattern EMPTYABLE_PATTERN = Pattern
	    .compile("\\\\\\[([^\\[\\]]+)\\\\]|\\s+|\\(\\?:[^\\(\\)]+\\)\\?|\\A(?:\\\\\\[)\\z+|\\A(?:\\\\\\])+\\z");
    private static final Pattern OPTIONAL_PATERN = Pattern
	    .compile("(\\s+)?\\\\\\[([^\\[\\]]+)\\\\](\\s+)?");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(
	    "(\\$\\w*)(\\W|\\Z)", Pattern.DOTALL);


    public StepMatcher parseStep(String stepPattern) {
	return new RegexStepMatcher(buildPattern(stepPattern),extractGroupNames(stepPattern));
    }

    
    Pattern buildPattern(String matchThis) {
	String matchThisButLeaveBrackets = escapeRegexpPunctuation(matchThis);
	String matchThisWithOptionals = replaceOptionals(matchThisButLeaveBrackets);
	List<Replacement> replacements = findArgumentsToReplace(matchThisWithOptionals);
	String patternToMatchAgainst = replaceIdentifiedArgsWithCapture(
		matchThisWithOptionals, replacements);
	String matchThisButIgnoreWhitespace = anyWhitespaceWillDo(patternToMatchAgainst);
	return Pattern.compile(matchThisButIgnoreWhitespace, Pattern.DOTALL);
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

    private String anyWhitespaceWillDo(String matchThis) {
	return matchThis.replaceAll("\\s+", "\\\\s+");
    }

    private List<Replacement> findArgumentsToReplace(String matchThisWithOptionals) {
	Matcher findingAllTheDollarWords = ARGUMENT_PATTERN
		.matcher(matchThisWithOptionals);
	List<Replacement> replacements = new ArrayList<Replacement>();
	while (findingAllTheDollarWords.find()) {
	    replacements.add(new Replacement(matchThisWithOptionals,
		    findingAllTheDollarWords.start(), 
		    findingAllTheDollarWords.end(), 
		    findingAllTheDollarWords.group(2)));
	}
	return replacements;
    }

    private String replaceIdentifiedArgsWithCapture(String escapedMatch,
	    List<Replacement> replacements) {
	String matchTemp = escapedMatch;
	for (int i = replacements.size(); i > 0; i--) {
	    String start = matchTemp.substring(0, replacements.get(i - 1).start);
	    String end = matchTemp.substring(replacements.get(i - 1).end);
	    String whitespaceIfAny = replacements.get(i - 1).whitespaceIfAny;
	    matchTemp = start + "(.*)" + whitespaceIfAny + end;
	}
	return matchTemp;
    }

    private String escapeRegexpPunctuation(String matchThis) {
	String escapedMatch = matchThis.replaceAll("([\\[\\]\\{\\}\\?\\^\\.\\*\\(\\)\\+\\\\])", "\\\\$1");
	return escapedMatch;
    }

    private class Replacement {
	private final int start;
	private final int end;
	private final String whitespaceIfAny;
	private final String name;

	public Replacement(String pattern, int start, int end, String whitespaceIfAny) {
	    this.start = start;
	    this.end = end;
	    this.whitespaceIfAny = whitespaceIfAny;
	    this.name = pattern.substring(start + 1, end).trim();
	}

    }

    public String[] extractGroupNames(String pattern) {
	List<String> names = new ArrayList<String>();
	for (Replacement replacement : findArgumentsToReplace(pattern)) {
	    names.add(replacement.name);
	}
	return names.toArray(new String[names.size()]);
    }

}
