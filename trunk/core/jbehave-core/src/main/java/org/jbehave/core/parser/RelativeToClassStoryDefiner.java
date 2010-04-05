package org.jbehave.core.parser;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.errors.InvalidStoryResourceException;
import org.jbehave.core.model.Story;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Defines stories by loading the content from a place that is relative and 
 * predictable to the compiled scenario class.
 *
 * See MAVEN_TEST_DIR, which implies a traversal out of 'target/test-classes'
 */
public class RelativeToClassStoryDefiner implements StoryDefiner {

    private final StoryParser parser;
    private final String traversal;
    private final URL location;
    private static final String MAVEN_TEST_DIR = "../../src/test/java";

    public RelativeToClassStoryDefiner(StoryParser parser, Class aScenarioClass, String traversal) {
        this.parser = parser;
        this.traversal = traversal;
        this. location = getLocation(aScenarioClass);
    }

    public RelativeToClassStoryDefiner(Class aScenarioClass, String traversal) {
        this(new PatternStoryParser(), aScenarioClass, traversal);
    }

    public RelativeToClassStoryDefiner(Class aScenarioClass) {
        this(aScenarioClass, MAVEN_TEST_DIR);
    }

    protected URL getLocation(Class aScenarioClass) {
        return aScenarioClass.getProtectionDomain().getCodeSource().getLocation();
    }

    public Story defineStory(String storyPath) {
        String storyAsString;
        try {
            String locn = new File(location.getFile()).getCanonicalPath() + "/";
            locn = locn + traversal + "/" + storyPath;
            locn = locn.replace("/", File.separator); // Windows and Unix
            File file = new File(locn);
            storyAsString = IOUtils.toString(new FileInputStream(file));
        } catch (IOException e) {
            throw new InvalidStoryResourceException("Story path '" + storyPath + "' not found.", e);
        }
        Story story = parser.defineStoryFrom(storyAsString, storyPath);
        // file name w/o path
        story.namedAs(new File(storyPath).getName());
        return story;
    }

}