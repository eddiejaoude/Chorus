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
package org.chorusbdd.chorus.config;

import org.chorusbdd.chorus.util.assertion.ChorusAssert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 06/07/12
 * Time: 18:49
 */
public class TestConfigReader extends ChorusAssert {

    @Test
    public void testBooleanSwitchWithValue() throws InterpreterPropertyException {
        String[] switches = new String[] { "-f", "./features", "-h", "org.chorusbdd.chorus", "-dryrun", "true" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        c.readConfiguration();
        assertTrue(c.isTrue(ChorusConfigProperty.DRY_RUN));
        assertTrue(c.isSet(ChorusConfigProperty.DRY_RUN));
    }

    @Test
    public void testBooleanSwitchCanBeSetFalse() throws InterpreterPropertyException {
        String[] switches = new String[] { "-f", "./features", "-h", "org.chorusbdd.chorus", "-dryrun", "false" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        c.readConfiguration();
        assertTrue(! c.isTrue(ChorusConfigProperty.DRY_RUN));
        assertTrue(c.isSet(ChorusConfigProperty.DRY_RUN));
    }

    @Test
    public void testDefaultValueGetsSetIfAvailable() throws InterpreterPropertyException {
        String[] switches = new String[] { "-f", "./features", "-h", "org.chorusbdd.chorus" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        c.readConfiguration();
        assertTrue(! c.isTrue(ChorusConfigProperty.DRY_RUN));
        assertTrue(c.isSet(ChorusConfigProperty.DRY_RUN));
    }

    @Test
    public void testADefaultValueDoesNotGetSetIfNoDefaultDefined() throws InterpreterPropertyException {
        String[] switches = new String[] { "-f", "./features", "-h", "org.chorusbdd.chorus" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        c.readConfiguration();
        assertTrue(! c.isSet(ChorusConfigProperty.TAG_EXPRESSION));
    }

    @Test
    public void testBooleanSwitchWithoutValue() throws InterpreterPropertyException {
        String[] switches = new String[] { "-f", "./features", "-h", "org.chorusbdd.chorus", "-dryrun" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        c.readConfiguration();
        assertTrue(c.isTrue(ChorusConfigProperty.DRY_RUN));
        assertTrue(c.isSet(ChorusConfigProperty.DRY_RUN));
    }

    @Test
    public void testBooleanSwitchUsingShortName() throws InterpreterPropertyException {
        String[] switches = new String[] { "-f", "./features", "-h", "org.chorusbdd.chorus", "-d" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        c.readConfiguration();
        assertTrue(c.isTrue(ChorusConfigProperty.DRY_RUN));
        assertTrue(c.isSet(ChorusConfigProperty.DRY_RUN));
    }

    @Test
    public void testCannotSetLessThanMinimumValues() {
        ConfigurationProperty propertyWithMinValues = new TestProperty(ChorusConfigProperty.HANDLER_PACKAGES) {
            public int getMinValueCount() {
                return 2;
            }
        };

        ConfigReader c = new ConfigReader(Collections.singletonList(propertyWithMinValues), new String[] { "-h", "onevalue" });
        try {
            c.readConfiguration();
        } catch (InterpreterPropertyException e) {
            assertTrue("contains At Least 2", e.getMessage().contains("At least 2 value(s) must be supplied"));
            return;
        }
        fail("Must complain when less than min vals set");
    }

    @Test
    public void testCannotSetMoreThanMaxValues() {
        ConfigurationProperty propertyWithMinValues = new TestProperty(ChorusConfigProperty.HANDLER_PACKAGES) {
            public int getMaxValueCount() {
                return 1;
            }
        };

        ConfigReader c = new ConfigReader(Collections.singletonList(propertyWithMinValues), new String[] { "-h", "onevalue", "twovalues" });
        try {
            c.readConfiguration();
        } catch (InterpreterPropertyException e) {
            assertTrue("contains At Most 1", e.getMessage().contains("At most 1 value(s) must be supplied"));
            return;
        }
        fail("Must complain when more than max vals set");
    }

    @Test
    public void mandatoryPropertyMustBeSet() {
        System.clearProperty(ChorusConfigProperty.FEATURE_PATHS.getSystemProperty());  //in case set
        String[] switches = new String[] { "-d" };
        ConfigReader c = new ConfigReader(ChorusConfigProperty.getAll(), switches);
        try {
            c.readConfiguration();
        } catch (InterpreterPropertyException e) {
            assertTrue(e.getMessage().contains("Mandatory property featurePaths was not set"));
            return;
        }
        fail("Must require mandatory -f property value");
    }

    @Test
    public void appendPropertyMayBeSetFromMultipleSources() throws InterpreterPropertyException {
        ConfigurationProperty propertyWithMinValues = new TestProperty(ChorusConfigProperty.HANDLER_PACKAGES) {
            public PropertySourceMode getPropertySourceMode() {
                return PropertySourceMode.APPEND;
            }
        };

        try {
            System.setProperty("chorusHandlerPackages", "secondvalue");
            ConfigReader c = new ConfigReader(Collections.singletonList(propertyWithMinValues), new String[] { "-h", "onevalue" });
            c.readConfiguration();
            List<String> values = c.getValues(propertyWithMinValues);
            assertEquals("property value count", 2, values.size());
        } finally {
            System.clearProperty("chorusHandlerPackages");
        }
    }

    private class TestProperty implements ConfigurationProperty {

        private ConfigurationProperty delegate;

        private TestProperty(ConfigurationProperty delegate) {
            this.delegate = delegate;
        }

        public String getSwitchName() {
            return delegate.getSwitchName();
        }

        public String getSwitchShortName() {
            return delegate.getSwitchShortName();
        }

        public String getHyphenatedSwitch() {
            return delegate.getHyphenatedSwitch();
        }

        public String getSystemProperty() {
            return delegate.getSystemProperty();
        }

        public boolean isMandatory() {
            return delegate.isMandatory();
        }

        public int getMinValueCount() {
            return delegate.getMinValueCount();
        }

        public int getMaxValueCount() {
            return delegate.getMaxValueCount();
        }

        public String getValidatingExpression() {
            return delegate.getValidatingExpression();
        }

        public String getExample() {
            return delegate.getExample();
        }

        public String getDescription() {
            return delegate.getDescription();
        }

        public String[] getDefaults() {
            return delegate.getDefaults();
        }

        public boolean matchesSwitch(String s) {
            return delegate.matchesSwitch(s);
        }

        public PropertySourceMode getPropertySourceMode() {
            return delegate.getPropertySourceMode();
        }
    }

}