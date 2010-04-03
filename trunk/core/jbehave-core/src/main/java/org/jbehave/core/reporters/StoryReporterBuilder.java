package org.jbehave.core.reporters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jbehave.core.reporters.FilePrintStreamFactory.FileConfiguration;

/**
 * <p>
 * A <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder</a> for
 * {@link StoryReporter}s. It builds a {@link DelegatingStoryReporter}
 * with delegates for a number of formats - mostly file-based ones except
 * {@Format.CONSOLE}. It requires a
 * {@link FilePrintStreamFactory} and provides default delegate instances for
 * each format.
 * </p>
 * <p>
 * To build reporter with default delegates for given formats:
 * <pre>
 * Class&lt;MyStory&gt; storylass = MyStory.class;
 * StoryPathResolver nameResolver = new UnderscoredCamelCaseResolver();
 * FilePrintStreamFactory printStreamFactory = new FilePrintStreamFactory(storyClass, nameResolver);
 * StoryReporter reporter = new StoryReporterBuilder(printStreamFactory).with(HTML).with(TXT).build();
 * </pre> 
 * </p>
 * <p>The builder is configured to build with the {@link Format#STATS} format by default.  To change the default formats
 * the user can override the method:
 * <pre>
 * new StoryReporterBuilder(printStreamFactory){
 *    protected void withDefaultFormats() {
 *      with(Format.STATS);
 *    }
 *  }
 * </pre>
 * </p>
 * <p>The builder configures the file-based reporters to output to the default file directory {@link FileConfiguration#DIRECTORY}.
 * To change the default:
 * <pre>
 * new StoryReporterBuilder(printStreamFactory).outputTo("my-reports").with(HTML).with(TXT).build();
 * </pre>
 * </p> 
 * <p>The builder provides default instances for all reporters.  To change the reporter for a specific instance, 
 * e.g. to report format <b>TXT</b> to <b>.text</b> files and to inject other non-default parameters, 
 * such as keywords for a different locale:
 * <pre>
 * new StoryReporterBuilder(printStreamFactory){
 *   public StoryReporter reporterFor(Format format){
 *       switch (format) {
 *           case TXT:
 *               factory.useConfiguration(new FileConfiguration("text"));
 *               return new PrintStreamStoryReporter(factory.getPrintStream(), new Properties(), new I18nKeywords(Locale), true);
 *            default:
 *               return super.reporterFor(format);
 *   }
 * }
 * </pre>
 */
public class StoryReporterBuilder {

    public enum Format {
        CONSOLE, STATS, TXT, HTML, XML
    };

    protected final FilePrintStreamFactory factory;
    protected Map<Format, StoryReporter> delegates = new HashMap<Format, StoryReporter>();
    private String fileDirectory = new FileConfiguration().getDirectory();

    public StoryReporterBuilder(FilePrintStreamFactory factory) {
        this.factory = factory;
        withDefaultFormats();
    }

    protected void withDefaultFormats() {
        with(Format.STATS);
    }

    public StoryReporter build() {
        return new DelegatingStoryReporter(delegates.values());
    }

    public StoryReporterBuilder outputTo(String fileDirectory){
        this.fileDirectory = fileDirectory;
        return this;
    }
    
    public StoryReporterBuilder with(Format format) {
        delegates.put(format, reporterFor(format));
        return this;
    }

    public StoryReporter reporterFor(Format format) {
        switch (format) {
            case CONSOLE:
                return new PrintStreamStoryReporter();
            case STATS:
                factory.useConfiguration(fileConfiguration("stats"));
                return new StatisticsStoryReporter(factory.getPrintStream());
            case TXT:
                factory.useConfiguration(fileConfiguration("txt"));
                return new PrintStreamStoryReporter(factory.getPrintStream());
            case HTML:
                factory.useConfiguration(fileConfiguration("html"));
                return new HtmlPrintStreamStoryReporter(factory.getPrintStream());
            case XML:
                factory.useConfiguration(fileConfiguration("xml"));
                return new XmlPrintStreamStoryReporter(factory.getPrintStream());
            default:
                throw new UnsupportedReporterFormatException(format);
        }
    }
    
    public Map<Format, StoryReporter> getDelegates() {
        return Collections.unmodifiableMap(delegates);
    }

    protected FileConfiguration fileConfiguration(String extension) {
        return new FileConfiguration(fileDirectory, extension);
    }

    @SuppressWarnings("serial")
    public static class UnsupportedReporterFormatException extends RuntimeException {

        public UnsupportedReporterFormatException(Format format) {
            super("Building StoryReporter not supported for format " + format);
        }

    }

}
