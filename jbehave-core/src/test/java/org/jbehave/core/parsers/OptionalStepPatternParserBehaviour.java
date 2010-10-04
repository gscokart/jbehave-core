package org.jbehave.core.parsers;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class OptionalStepPatternParserBehaviour extends RegexPrefixCapturingPatternParserBehaviour {

    @Override
    protected StepPatternParser createPatternParser() {
        return new OptionalStepPatternParser();
    }

    
    @Test
    public void shouldHandleOptionalParam(){
	StepMatcher pattern = parser.parseStep("[$xxx] a good [one] for [$zzzz]");
        assertThat(pattern , matches("X a good one for Z"));
        assertThat(pattern , matches("X a good for Z"));
        assertThat(pattern , matches("a good one for Z"));
        assertThat(pattern , matches("X a good for"));
        assertThat(pattern , matches("a good for"));
    }

    @Test
    public void shouldAllowRecursiveOptionalParam(){
	StepMatcher pattern = parser.parseStep("[[X]$xxx [alone]] and [also [at] [[the] end]]");
        assertThat(pattern , matches("X alone and"));
        assertThat(pattern , matches("X1 alone and"));
        assertThat(pattern , matches("1 alone and"));
        assertThat(pattern , matches("X and"));
        assertThat(pattern , matches("and"));
        assertThat(pattern , matches("X and also"));
        assertThat(pattern , matches("X and also end"));
        assertThat(pattern , matches("X and also at end"));
        assertThat(pattern , matches("X and also the end"));
        assertThat(pattern , matches("X and also at the end"));
        assertThat(pattern , matches("and also at the end"));
        assertThat(pattern , matches("X and also at"));
    }

    @Test
    public void shouldAllowParenthesisInOptionalParam(){
	StepMatcher pattern = parser.parseStep("[(A)] [as before] user did that [so that it works][(or not)]");
        assertThat(pattern , matches("(A) as before user did that so that it works (or not)"));
        assertThat(pattern , matches("as before user did that so that it works (or not)"));
        assertThat(pattern , matches("(A) user did that so that it works (or not)"));
        assertThat(pattern , matches("(A) as before user did that (or not)"));
        assertThat(pattern , matches("(A) as before user did that so that it works"));
        assertThat(pattern , matches("user did that so that it works (or not)"));
        assertThat(pattern , matches("as before user did that (or not)"));
        assertThat(pattern , matches("as before user did that so that it works"));
        assertThat(pattern , matches("(A) user did that (or not)"));
        assertThat(pattern , matches("(A) user did that so that it works"));
        assertThat(pattern , matches("(A) as before user did that"));
        assertThat(pattern , matches("(A) as before user did that so that it works (or not)"));
    }

}
