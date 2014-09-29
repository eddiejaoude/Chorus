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
package org.chorusbdd.chorus.processes.processmanager;

import org.chorusbdd.chorus.logging.ChorusLog;
import org.chorusbdd.chorus.logging.ChorusLogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 21/07/13
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class NativeProcessCommandLineBuilder extends AbstractCommandLineBuilder {

    private static ChorusLog log = ChorusLogFactory.getLog(NativeProcessCommandLineBuilder.class);

    private ProcessInfo processInfo;
    private File featureDir;

    public NativeProcessCommandLineBuilder(ProcessInfo processInfo, File featureDir) {
        this.processInfo = processInfo;
        this.featureDir = featureDir;
    }
    
    @Override
    public List<String> buildCommandLine() {
        String executableToken = getExecutableToken(processInfo);
        List<String> argsTokens = getSpaceSeparatedTokens(processInfo.getArgs());

        List<String> commandLineTokens = new ArrayList<String>();
        commandLineTokens.add(executableToken);
        commandLineTokens.addAll(argsTokens);
        return commandLineTokens;
    }

    private String getExecutableToken(ProcessInfo processesConfig) {
        String executableTxt = processesConfig.getPathToExecutable();
        executableTxt = getPathToExecutable(featureDir, executableTxt);
        return executableTxt;
    }
    
    public static String getPathToExecutable(File featureDir, String path) {
        File scriptPath = new File(path);
        if ( ! scriptPath.isAbsolute() ) {
            scriptPath = new File(featureDir, path);
        }
        return scriptPath.getAbsolutePath();
    }
    
}
