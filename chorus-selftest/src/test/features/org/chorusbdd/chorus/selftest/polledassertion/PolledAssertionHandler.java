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
package org.chorusbdd.chorus.selftest.polledassertion;

import org.chorusbdd.chorus.annotations.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 14/06/12
 * Time: 09:21
 */
@Handler("Polled Assertion")
public class PolledAssertionHandler {

    private volatile int timeCount = 0;
    
    private int passesWithinPollCount;
    private int passesForPollCount;

    @Step("Chorus is working properly")
    public void isWorkingProperly() {
    }

    @Step("I increment a value with a timer task")
    public void incrementWithTimer() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                timeCount++;    
            }
        }, 300);
    }

    @Step("I increment a value")
    public void increment() {
        timeCount++;
    }
    
    @Step("the value is (\\d) within default period")
    @PassesWithin()
    public void passesWithinDefaultSecond(int expectCount) {
        assertEquals("Expect " + expectCount + " but was " + timeCount, expectCount, timeCount);    
    }

    @Step("the value is (\\d) within 2 seconds")
    @PassesWithin(length = 2)
    public void passesWithinTwoSeconds(int expectCount) {
        passesWithinPollCount++;
        assertEquals("Expect " + expectCount + " but was " + timeCount, expectCount, timeCount);
        
        //after 300ms the timer task should set the value, first poll should fail second should pass
        assertTrue("Expect to have been polled at least 2 times", passesWithinPollCount >= 2 && passesWithinPollCount < 5);
    }

    @Step("the value is not (\\d) within 0.2 seconds so this step should fail")
    @PassesWithin(length = 200, timeUnit = TimeUnit.MILLISECONDS, pollFrequencyInMilliseconds = 50)
    public void passesWithinPointTwoSeconds(int expectCount) {
        assertEquals("Expect " + expectCount, expectCount, timeCount);
    }

    @Step("the value is (\\d) for half a second")
    @PassesWithin(length = 500, timeUnit = TimeUnit.MILLISECONDS, pollFrequencyInMilliseconds = 50, pollMode = PollMode.PASS_THROUGHOUT_PERIOD)
    public void passesForHalfASecond(int expectCount) {
        passesForPollCount++;
        assertEquals("Expect " + expectCount, expectCount, timeCount);
    }

    @Step("the check method was polled (\\d+) times")
    public void checkPolledMultipleTimes(int times) {
        assertEquals("Expect to have been polled at least " + times + " times but was " + passesForPollCount, times, passesForPollCount);
    }
    
}
