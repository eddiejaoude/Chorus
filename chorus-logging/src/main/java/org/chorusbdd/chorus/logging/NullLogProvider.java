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
package org.chorusbdd.chorus.logging;

/**
 * A null implementation of ChorusLogProvider
 * This should never get used
 */
class NullLogProvider implements ChorusLogProvider {

    public static final NullLogProvider NULL_LOG_PROVIDER = new NullLogProvider();

    private NullLogProvider() {}

    public ChorusLog getLog(Class clazz) {
        return new NullLog();
    }

    private class NullLog implements ChorusLog {

        public boolean isDebugEnabled() {
            return false;
        }

        public boolean isErrorEnabled() {
            return false;
        }

        public boolean isFatalEnabled() {
            return false;
        }

        public boolean isInfoEnabled() {
            return false;
        }

        public boolean isTraceEnabled() {
            return false;
        }

        public boolean isWarnEnabled() {
            return false;
        }

        public void trace(Object message) {
        }

        public void trace(Object message, Throwable t) {
        }

        public void debug(Object message) {
        }

        public void debug(Object message, Throwable t) {
        }

        public void info(Object message) {
        }

        public void info(Object message, Throwable t) {
        }

        public void warn(Object message) {
        }

        public void warn(Object message, Throwable t) {
        }

        public void error(Object message) {
        }

        public void error(Object message, Throwable t) {
        }

        public void fatal(Object message) {
        }

        public void fatal(Object message, Throwable t) {
        }

        private void logWarning() {
            System.err.println("No ChorusLogProvider configured, ChorusLogFactory has not been initialized properly");
        }
    }

}
