package org.jbehave.core.embedder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.reporters.PrintStreamOutput.Format;
import org.jbehave.core.reporters.ReportsCount;
import org.junit.Test;

public class EmbedderMonitorBehaviour {

    @Test
    public void shouldNotPrintWithSilentMonitor() throws Throwable {
        OutputStream out = new ByteArrayOutputStream();
        SilentEmbedderMonitor monitor = new SilentEmbedderMonitor(new PrintStream(out));
        monitor.print("a message");
        monitor.printStackTrace(new RuntimeException());
        assertThat(out.toString(), is(""));
    }

    @Test
    public void shouldOnlyPrintFailuresWithReportingFailuresMonitor() throws Throwable {
        OutputStream out = new ByteArrayOutputStream();
        ReportingFailuresEmbedderMonitor monitor = new ReportingFailuresEmbedderMonitor(new PrintStream(out));
        monitor.runningEmbeddable("embeddable");
        monitor.runningStory("/path");
        monitor.generatingReportsView(new File("target"), Arrays.asList(Format.HTML.name()), new Properties());
        monitor.reportsViewGenerated(new ReportsCount(1, 0, 2, 1, 0));
        assertThat(out.toString(), is(""));
        monitor.batchFailed(new BatchFailures());
        monitor.storyFailed("/path", new RuntimeException());
        assertThat(out.toString(), is(not("")));
   }

    @Test
    public void shouldDelegateOutput() throws Throwable {
        EmbedderMonitor monitor = new ReportingFailuresEmbedderMonitor();
        assertThat(monitor.toString(), containsString(ReportingFailuresEmbedderMonitor.class.getSimpleName()+"[output="));
   }

}