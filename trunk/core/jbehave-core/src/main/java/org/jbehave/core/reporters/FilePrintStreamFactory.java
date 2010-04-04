package org.jbehave.core.reporters;

import org.jbehave.core.parser.StoryLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Creates {@link PrintStream} instances that write to a file. It also provides
 * useful defaults for the file directory and the extension.
 */
public class FilePrintStreamFactory implements PrintStreamFactory {

    private final String storyPath;
    private FileConfiguration configuration;
    private PrintStream printStream;
    private File outputFile;

    public FilePrintStreamFactory(String storyPath) {
        this(storyPath, new FileConfiguration());
    }

    public FilePrintStreamFactory(String storyPath, FileConfiguration configuration) {
        this.storyPath = storyPath;
        this.configuration = configuration;
        this.outputFile = outputFile();
    }

    public PrintStream getPrintStream() {
        try {
            outputFile.getParentFile().mkdirs();
            printStream = new PrintStream(new FileOutputStream(outputFile, true));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return printStream;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void useConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
        this.outputFile = outputFile();
    }

    protected File outputFile() {
        File outputDirectory = outputDirectory();
        String fileName = fileName();
        return new File(outputDirectory, fileName);
    }

    protected String fileName() {
        String storyName = storyName();
        String name = storyName.substring(0, storyName.lastIndexOf("."));
        return name + "." + configuration.getExtension();
    }
    
    protected File outputDirectory() {
        File targetDirectory = new File(storyLocation()).getParentFile();
        return new File(targetDirectory, configuration.getDirectory());
    }

    private String storyLocation() {
        return new StoryLocation(storyPath).getLocation().replace("file:","");
    }

    private String storyName() {
        return new StoryLocation(storyPath).getName().replace('/', '.');
    }

    /**
     * StoryConfiguration class for file print streams. Allows specification of the
     * file directory (relative to the core class code source location) and
     * the file extension. Provides as defaults {@link #DIRECTORY} and
     * {@link #HTML}.
     */
    public static class FileConfiguration {
        public static final String DIRECTORY = "jbehave-reports";
        public static final String HTML = "html";

        private final String directory;
        private final String extension;

        public FileConfiguration() {
            this(DIRECTORY, HTML);
        }

        public FileConfiguration(String extension) {
            this(DIRECTORY, extension);
        }

        public FileConfiguration(String directory, String extension) {
            this.directory = directory;
            this.extension = extension;
        }

        public String getDirectory() {
            return directory;
        }

        public String getExtension() {
            return extension;
        }

    }

}
