/**
 *  Copyright (C) 2000-2014 The Software Conservancy and Original Authors.
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
 * Created by nick on 15/09/2014.
 */
public class DefaultLogProviderFactory {

    public ChorusLogProvider createLogProvider() {
        ChorusLogProvider result = NullLogProvider.NULL_LOG_PROVIDER;

        result = getSystemPropertyProvider(result);

        //at present I think it's probably better to force the user to turn on the ChorusCommonsLogProvider by
        //setting the appropriate system property -DchorusLogProvider

        //the alternative is that we end up with log4j missing configuration errors in the output
        //where log4j is in the classpath but the user hasn't actually configured it (common)
        //        if ( result == NullLogProvider.NULL_LOG_PROVIDER) {
        //            result = getCommonsProviderIfCommonsIsPresent();
        //        }

        if ( result == NullLogProvider.NULL_LOG_PROVIDER) {
            //fall back to a basic std out logger
            result = new StdOutLogProvider();
        }
        return result;
    }

//
//    private ChorusLogProvider getCommonsProviderIfCommonsIsPresent() {
//        ChorusLogProvider result = null;
//            try {
//
//                //do we have commons logging on the classpath?
//                Class c = Class.forName("org.apache.commons.logging.Log");
//
//                //if so load up the ChorusCommonsLogProvider
//                //doing this by reflection to avoid any nasty class loading issues if we import
//                //ChorusCommonsLogProvider and commons isn't actually present
//                Class commonsLogProvider = Class.forName("org.chorusbdd.chorus.logging.ChorusCommonsLogProvider");
//                result = (ChorusLogProvider)commonsLogProvider.newInstance();
//
//            } catch (Exception e) {
//            }
//        return result;
//    }

    private ChorusLogProvider getSystemPropertyProvider(ChorusLogProvider result) {
        String provider = System.getProperty("chorusLogProvider");
        if ( provider != null) {
            try {
                if ( provider != null ) {
                    Class c = Class.forName(provider);
                    result = (ChorusLogProvider)c.newInstance();
                }
            } catch (Throwable t) {
                System.err.println("Failed to instantiate ChorusLogProvider class " + provider + " will use the default LogProvider");
            }
        }
        return result;
    }
}
