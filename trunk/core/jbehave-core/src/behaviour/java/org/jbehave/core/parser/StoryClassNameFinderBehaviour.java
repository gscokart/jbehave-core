package org.jbehave.core.parser;

import static java.util.Arrays.asList;
import static org.jbehave.Ensure.ensureThat;

import java.io.IOException;
import java.io.InputStream;

import org.jbehave.core.errors.InvalidStoryClassPathException;
import org.junit.Test;

public class StoryClassNameFinderBehaviour {

    @Test
    public void canListClassNames() {
        StoryClassNameFinder finder = new StoryClassNameFinder();
        ensureThat(finder.listStoryClassNames(".", ".", asList("**/stories/*.java"), asList("")).size() > 0);
    }

    @Test
    public void canReturnEmptyListForInexistentBasedir() {
        StoryClassNameFinder finder = new StoryClassNameFinder();
        ensureThat(finder.listStoryClassNames("/inexistent", null, asList(""), asList("")).size() == 0);
    }

    @Test(expected= InvalidStoryClassPathException.class)
    public void cannotListClassNamesForPathsThatAreInvalid() {
        StoryClassNameFinder finder = new StoryClassNameFinder();
        finder.listStoryClassNames(".", null, null, null);
    }
    
   static class InvalidClassLoader extends ClassLoader {

        @Override
        public InputStream getResourceAsStream(String name) {
            return new InputStream() {

                public int available() throws IOException {
                    return 1;
                }

                @Override
                public int read() throws IOException {
                    throw new IOException("invalid");
                }

            };
        }

    }

}
