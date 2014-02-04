/**
 *  Copyright (C) 2000-2013 The Software Conservancy and Original Authors.
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to
 *  deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 *
 *  Nothing in this notice shall be deemed to grant any rights to trademarks,
 *  copyrights, patents, trade secrets or any other intellectual property of the
 *  licensor or any contributor except as expressly stated herein. No patent
 *  license is granted separate from the Software, for code that you delete from
 *  the Software, or for combinations of the Software with other software or
 *  hardware.
 */
package org.chorusbdd.chorus.util.logging;

import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.results.ResultsSummary;
import org.chorusbdd.chorus.results.ScenarioToken;
import org.chorusbdd.chorus.results.StepToken;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by: Steve Neal
 * Date: 30/09/11
 */
public class PlainOutputFormatter implements OutputFormatter {

    private PrintWriter out;

    /**
     * Create a results formatter which outputs results
     * All OutputFormatter must have a no argument constructor
     */
    public PlainOutputFormatter() {}

    public void setPrintStream(PrintStream outStream) {
        out = new PrintWriter(outStream);
    }

    public void printResults(ResultsSummary s) {
        if (s != null) {
            //only show the pending count if there were pending steps, makes the summary more legible
            if ( s.getFeaturesPending() > 0) {
                printMessage(String.format("%nFeatures  (total:%d) (passed:%d) (pending:%d) (failed:%d)",
                    s.getTotalFeatures(),
                    s.getFeaturesPassed(),
                    s.getFeaturesPending(),
                    s.getFeaturesFailed()));
            } else {
                printMessage(String.format("%nFeatures  (total:%d) (passed:%d) (failed:%d)",
                    s.getTotalFeatures(),
                    s.getFeaturesPassed(),
                    s.getFeaturesFailed()));
            }

            //only show the pending count if there were pending steps, makes the summary more legible
            if ( s.getScenariosPending() > 0 ) {
                //print scenarios summary
                printMessage(String.format("Scenarios (total:%d) (passed:%d) (pending:%d) (failed:%d)",
                    s.getTotalScenarios(),
                    s.getScenariosPassed(),
                    s.getScenariosPending(),
                    s.getScenariosFailed()));
            } else {
                //print scenarios summary
                printMessage(String.format("Scenarios (total:%d) (passed:%d) (failed:%d)",
                    s.getTotalScenarios(),
                    s.getScenariosPassed(),
                    s.getScenariosFailed()));
            }

            //print steps summary
            printMessage(String.format("Steps     (total:%d) (passed:%d) (failed:%d) (undefined:%d) (pending:%d) (skipped:%d)",
                    s.getStepsPassed() + s.getStepsFailed() + s.getStepsUndefined() + s.getStepsPending() + s.getStepsSkipped(),
                    s.getStepsPassed(),
                    s.getStepsFailed(),
                    s.getStepsUndefined(),
                    s.getStepsPending(),
                    s.getStepsSkipped()));
        }
    }

    public void printFeature(FeatureToken feature) {
        out.printf("Feature: %-84s%-7s %s%n", feature.getNameWithConfiguration(), "", "");
    }

    public void printScenario(ScenarioToken scenario) {
        out.printf("  Scenario: %s%n", scenario.getName());
    }

    public void printStepStart(StepToken step, int depth) {
//        StringBuilder depthPadding = getDepthPadding(depth);
//        int maxStepTextChars = Math.max(89, 50);  //always show at least 50 chars of step text
//        String terminator = step.isStepMacro() ? "->%n" : ".\r";
//        out.printf("    " + depthPadding + "%-" + maxStepTextChars + "s" + terminator, step.toString());
    }


    public void printStepEnd(StepToken step, int depth) {
        if ( ! step.isStepMacro()) { //we don't print results for the step macro step itself but show it for each child step
            StringBuilder depthPadding = getDepthPadding(depth);
            int maxStepTextChars = Math.max(89, 50);  //always show at least 50 chars of step text
            out.printf("    " + depthPadding + "%-" + maxStepTextChars + "s%-7s %s%n", step.toString(), step.getEndState(), step.getMessage());
        }
    }

    private StringBuilder getDepthPadding(int depth) {
        StringBuilder sb = new StringBuilder();
        for ( int loop=1; loop < depth; loop++ ) {
            sb.append("..");
        }
        if ( depth > 1 ) {
            sb.append(" ");
        }
        return sb;
    }

    public void printStackTrace(String stackTrace) {
        out.print(stackTrace);
    }

    public void printMessage(String message) {
        out.printf("%s%n", message);
    }

    public void flush() {
        out.flush();
    }

    public void log(LogLevel level, Object message) {
        if ( level == LogLevel.ERROR ) {
            logErr(level, message);    
        } else {
            logOut(level, message);
        }
    }

    public void logThrowable(LogLevel level, Throwable t) {
        if ( level == LogLevel.ERROR ) {
            t.printStackTrace(ChorusOut.err);    
        } else {
            t.printStackTrace(ChorusOut.out);
        }
    }

    private void logOut(LogLevel type, Object message) {
        //Use 'Chorus' instead of class name for logging, since we are testing the log output up to info level
        //and don't want refactoring the code to break tests if log statements move class
        out.println(String.format("%s --> %-7s - %s", "Chorus", type, message));
    }

    protected void logErr(LogLevel type, Object message) {
        //Use 'Chorus' instead of class name for logging, since we are testing the log output up to info level
        //and don't want refactoring the code to break tests if log statements move class
        out.println(String.format("%s --> %-7s - %s", "Chorus", type, message));
    }

}