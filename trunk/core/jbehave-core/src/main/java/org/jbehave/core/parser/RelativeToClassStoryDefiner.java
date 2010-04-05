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
 * <p/>
 * See MAVEN_TEST_DIR, which implies a traversal out of 'target/test-classes'
 */
public class RelativeToClassStoryDefiner implements StoryDefiner {

    private final StoryParser parser;
    private final String traversal;
    private final URL location;
    private static final String MAVEN_TEST_DIR = "../../src/test/java";

    public RelativeToClassStoryDefiner(StoryParser parser, Class storyClass, String traversal) {
        this.parser = parser;
        this.traversal = traversal;
        this.location = locationFor(storyClass);
    }

    public RelativeToClassStoryDefiner(Class storyClass, String traversal) {
        this(new PatternStoryParser(), storyClass, traversal);
    }

    public RelativeToClassStoryDefiner(Class storyClass) {
        this(storyClass, MAVEN_TEST_DIR);
    }

    protected URL locationFor(Class storyClass) {
        return storyClass.getProtectionDomain().getCodeSource().getLocation();
    }

    public Story defineStory(String storyPath) {
        try {
            String fileLocation = new File(location.getFile()).getCanonicalPath() + "/";
            fileLocation = fileLocation + traversal + "/" + storyPath;
            fileLocation = fileLocation.replace("/", File.separator); // Windows and Unix
            File file = new File(fileLocation);
            Story story = parser.parseStory(IOUtils.toString(new FileInputStream(file)), storyPath);
            // file name w/o path
            story.namedAs(new File(storyPath).getName());
            return story;
        } catch (IOException e) {
            throw new InvalidStoryResourceException("Story path '" + storyPath + "' not found.", e);
        }

    }

}