package org.chorusbdd.chorus.processes.manager;

import org.chorusbdd.chorus.context.ChorusContext;
import org.chorusbdd.chorus.logging.ChorusLog;
import org.chorusbdd.chorus.logging.ChorusLogFactory;
import org.chorusbdd.chorus.util.assertion.ChorusAssert;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nick on 20/10/2014.
 */
public class TailLogPatternMatcher implements ProcessOutputPatternMatcher {

    private static ChorusLog log = ChorusLogFactory.getLog(TailLogPatternMatcher.class);

    private ChorusProcess process;
    private File logFile;
    private TailLogBufferedReader tailLogBufferedReader;

    public TailLogPatternMatcher(ChorusProcess process, File logFile) {
        this.process = process;
        this.logFile = logFile;
    }

    public void waitForMatch(String pattern, boolean searchWithinLines, TimeUnit timeUnit, long length) {
        if ( tailLogBufferedReader == null) {
            tailLogBufferedReader = new TailLogBufferedReader(logFile);
        }

        long timeoutMilliseconds = timeUnit.toMillis(length);
        waitForOutputPattern(pattern, tailLogBufferedReader, searchWithinLines, timeoutMilliseconds);
    }

    private void waitForOutputPattern(String pattern, TailLogBufferedReader bufferedReader, boolean searchWithinLines, long timeoutMilliseconds) {
        Pattern p = Pattern.compile(pattern);
        long timeout = System.currentTimeMillis() + timeoutMilliseconds;
        try {
            String matched = waitForPattern(timeout, bufferedReader, p, searchWithinLines, timeoutMilliseconds / 1000);

            //store into the ChorusContext the exact string which matched the pattern so this can be used
            //in subsequent test steps
            ChorusContext.getContext().put(LAST_MATCH, matched);
        } catch (IOException e) {
            log.warn("Failed while matching pattern " + p, e);
            ChorusAssert.fail("Failed while matching pattern");
        }
    }

    //read ahead without blocking and attempt to match the pattern
    private String waitForPattern(long timeout, TailLogBufferedReader bufferedReader, Pattern pattern, boolean searchWithinLines, long timeoutInSeconds) throws IOException {
        StringBuilder sb = new StringBuilder();
        String result = null;
        label:
        while(true) {
            while ( bufferedReader.ready() ) {
                int c = bufferedReader.read();
                if ( c != -1 ) {
                    if (c == '\n' || c == '\r') {
                        if (sb.length() > 0) {
                            Matcher m = pattern.matcher(sb);
                            boolean match = searchWithinLines ? m.find() : m.matches();
                            if (match) {
                                result = sb.toString();
                                break label;
                            } else {
                                sb.setLength(0);
                            }
                        }
                    } else {
                        sb.append((char) c);
                    }
                }
            }

            //nothing more to read, does the current output match the pattern?
            if ( sb.length() > 0 && searchWithinLines) {
                Matcher m = pattern.matcher(sb);
                if ( m.find() ) {
                    result = m.group(0);
                    break label;
                }
            }

            try {
                Thread.sleep(10); //avoid a busy loop since we are using nonblocking ready() / read()
            } catch (InterruptedException e) {}

            checkTimeout(timeout, timeoutInSeconds);

            if ( process.isStopped() && ! bufferedReader.ready()) {
                ChorusAssert.fail(
                        process.isExitCodeFailure() ?
                                "Process stopped with error code " + process.getExitCode() + " while waiting for match" :
                                "Process stopped while waiting for match"
                );
            }
        }
        return result;
    }

    private void checkTimeout(long timeout, long seconds) {
        if ( System.currentTimeMillis() > timeout ) {
            ChorusAssert.fail("Timed out after " + seconds + " seconds");
        }
    }


    @Override
    public void close() {
        if ( tailLogBufferedReader != null) {
            try {
                tailLogBufferedReader.close();
            } catch (IOException e) {
                log.trace("Failed to close tailLogBufferedReader", e);
            }
        }
    }
}