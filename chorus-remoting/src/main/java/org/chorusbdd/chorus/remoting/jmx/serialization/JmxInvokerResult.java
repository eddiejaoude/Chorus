/**
 *  Copyright (C) 2000-2015 The Software Conservancy and Original Authors.
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
package org.chorusbdd.chorus.remoting.jmx.serialization;

import org.chorusbdd.chorus.stepinvoker.StepInvoker;

/**
 * Created by nick on 25/10/14.
 *
 * Convert an Invoker to a Map and back again
 *
 * This avoids serializing the invoker instance across the wire, making it easier to maintain backwards compatibility
 * when attributes are added or removed from StepInvoker
 */
public class JmxInvokerResult extends AbstractJmxResult {

    private static final long serialVersionUID = 1;

    public static final String STEP_ID = "STEP_ID";
    public static final String PENDING_MSG = "PENDING_MSG";
    public static final String IS_PENDING = "IS_PENDING";
    public static final String PATTERN = "PATTERN";
    public static final String TECHNICAL_DESCRIPTION = "TECHNICAL_DESCRIPTION";

    /**
     * @return a map of properties representing a step invoker exported over the network using RMI protocol,
     * or null if the step invoker cannot be converted for remoting
     */
    public JmxInvokerResult(StepInvoker i) {
        put(API_VERSION, ApiVersion.API_VERSION);
        put(STEP_ID, i.getId());
        put(PENDING_MSG, i.getPendingMessage());
        put(IS_PENDING, i.isPending());
        put(PATTERN, i.getStepPattern().toString());
        put(TECHNICAL_DESCRIPTION, i.getTechnicalDescription());
    }

}
