package org.jbehave.core.parser;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstraction of story location, handling cases in which story path is defined as URL and as class path resource.
 */
public class StoryLocation {

    private final String storyPath;
    private String codeLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

    public StoryLocation(String storyPath) {
        this.storyPath = storyPath;
    }

    public StoryLocation(String storyPath, Class<?> codeSourceClass) {
        this.storyPath = storyPath;
        this.codeLocation = codeSourceClass.getProtectionDomain().getCodeSource().getLocation().getFile();;
    }

    public String getPath() {
        return storyPath;
    }

    public String getCodeLocation() {
        return codeLocation;
    }

    public String getPathWithCodeLocation() {
        return codeLocation + storyPath;
    }

    public String getPathWithoutCodeLocation() {
        int codeLocationToStripOff = storyPath.indexOf(codeLocation) + codeLocation.length();
        return storyPath.substring(codeLocationToStripOff, storyPath.length());
    }

    public boolean isURL() {
        try {
            new URL(storyPath);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}