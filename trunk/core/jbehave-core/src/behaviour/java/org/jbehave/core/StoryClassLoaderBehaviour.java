package org.jbehave.core;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StoryClassLoaderBehaviour {

    @Test
    public void canInstantiateNewScenarioWithDefaultConstructor() throws MalformedURLException {
        StoryClassLoader classLoader = new StoryClassLoader(Arrays.<String>asList());
        String storyClassName = MyStory.class.getName();
        assertScenarioIsInstantiated(classLoader, storyClassName);
    }

    @Test
    public void canInstantiateNewScenarioWithClassLoader() throws MalformedURLException {
        StoryClassLoader classLoader = new StoryClassLoader(Arrays.<String>asList());
        String storyClassName = MyStory.class.getName();
        assertScenarioIsInstantiated(classLoader, storyClassName, ClassLoader.class);
    }

    private void assertScenarioIsInstantiated(StoryClassLoader classLoader, String storyClassName, Class<?>... parameterTypes) {
        RunnableStory story = classLoader.newStory(storyClassName);
        assertNotNull(story);
        assertEquals(storyClassName, story.getClass().getName());
    }

    private static class MyStory extends JUnitStory {

    	public MyStory(){

        }

        public MyStory(ClassLoader classLoader){

        }
    }

}