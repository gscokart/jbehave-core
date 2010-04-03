package org.jbehave.core.parser;

import org.jbehave.core.JUnitStory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.jbehave.Ensure.ensureThat;
import static org.jbehave.core.parser.UnderscoredCamelCaseResolver.NUMBERS_AS_UPPER_CASE_LETTERS_PATTERN;

public class UnderscoredCamelCaseResolverBehaviour {

    @Test
    public void shouldResolveCamelCasedClassNameToUnderscoredName() {
    	StoryPathResolver resolver = new UnderscoredCamelCaseResolver();
        ensureThat(resolver.resolve(CamelCaseStory.class),
                equalTo("org/jbehave/core/parser/camel_case_story"));
    }
        
    @Test
    public void shouldResolveCamelCasedClassNameToUnderscoredNameWithExtension() {
    	StoryPathResolver resolver = new UnderscoredCamelCaseResolver(".story");
        ensureThat(resolver.resolve(CamelCase.class),
                equalTo("org/jbehave/core/parser/camel_case.story"));
    }

    /**
     * Some teams are not going to have /stories/ directories,
     * they are going to co-mingle with tests and match in Maven land with *Story
     */
    @Test
    public void shouldResolveCamelCasedClassNameToUnderscoredNameWithExtensionStrippingExtraneousWord() {
    	StoryPathResolver resolver = new UnderscoredCamelCaseResolver(".story").removeFromClassname("Story");
        ensureThat(resolver.resolve(CamelCaseStory.class),
                equalTo("org/jbehave/core/parser/camel_case.story"));
    }

    @Test
    public void shouldResolveCamelCasedClassNameWithNumbersTreatedAsLowerCaseLetters() {
    	StoryPathResolver resolver = new UnderscoredCamelCaseResolver();
        ensureThat(resolver.resolve(CamelCaseWithA3Qualifier.class),
                equalTo("org/jbehave/core/parser/camel_case_with_a3_qualifier"));
        ensureThat(resolver.resolve(CamelCaseWithA33Qualifier.class),
                equalTo("org/jbehave/core/parser/camel_case_with_a33_qualifier"));
    }
        
    @Test
    public void shouldResolveCamelCasedClassNameWithNumbersTreatedAsUpperCaseLetters() {
    	StoryPathResolver resolver = new UnderscoredCamelCaseResolver("", NUMBERS_AS_UPPER_CASE_LETTERS_PATTERN);
        ensureThat(resolver.resolve(CamelCaseWithA3Qualifier.class),
                equalTo("org/jbehave/core/parser/camel_case_with_a_3_qualifier"));
        ensureThat(resolver.resolve(CamelCaseWithA33Qualifier.class),
                equalTo("org/jbehave/core/parser/camel_case_with_a_3_3_qualifier"));
    }
    
    static class CamelCaseStory extends JUnitStory {
        
    }

    static class CamelCase extends JUnitStory {
        
    }
    
    static class CamelCaseWithA3Qualifier extends JUnitStory {
        
    }
    
    static class CamelCaseWithA33Qualifier extends JUnitStory {
        
    }
}
