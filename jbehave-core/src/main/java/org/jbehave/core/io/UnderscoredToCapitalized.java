package org.jbehave.core.io;

import static org.apache.commons.lang.StringUtils.contains;
import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.apache.commons.lang.WordUtils.capitalize;

public class UnderscoredToCapitalized implements StoryNameResolver {

    public String resolveName(String path) {
        String name = path;
        if ( contains(name, '/') ){
            name = substringAfterLast(name, "/");
            name = substringBefore(name, ".");
        } else if ( contains(name, '.') ){
            name = substringAfterLast(name, ".");
        }
        return capitalize(name.replace("_", " "));
    }
    
}
