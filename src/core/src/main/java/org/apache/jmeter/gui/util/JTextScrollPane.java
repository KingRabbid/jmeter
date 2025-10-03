/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.gui.util;

import org.apache.jmeter.util.JMeterUtils;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;


/**
 * Utility class to handle RSyntaxTextArea code
 * It's not currently possible to instantiate the RTextScrollPane class when running headless.
 * So we use getInstance methods to create the class and allow for headless testing.
 */
public class JTextScrollPane extends RTextScrollPane {

    private static final long serialVersionUID = 210L;

    @Deprecated
    public JTextScrollPane() {
        // for use by test code only
    }

    /*
    public static JTextScrollPane getInstance(JSyntaxTextArea scriptField, boolean foldIndicatorEnabled) {
        try {
            return new JTextScrollPane(scriptField, foldIndicatorEnabled);
        } catch (NullPointerException npe) { // for headless unit testing
            if ("true".equals(System.getProperty("java.awt.headless"))) { // $NON-NLS-1$ $NON-NLS-2$
                return new JTextScrollPane();
            } else {
                throw npe;
            }
        }
    }
     */

    public static JTextScrollPane getInstance(JSyntaxTextArea scriptField) {
        try {
            return new JTextScrollPane(scriptField);
        } catch (NullPointerException npe) { // for headless unit testing
            if ("true".equals(System.getProperty("java.awt.headless"))) { // $NON-NLS-1$ $NON-NLS-2$
                return new JTextScrollPane();
            } else {
                throw npe;
            }
        }
    }

    /**
     * @param scriptField syntax text are to wrap
     * @deprecated use {@link #getInstance(JSyntaxTextArea)} instead
     */
    @Deprecated
    public JTextScrollPane(JSyntaxTextArea scriptField) {
        super(scriptField);
    }

    /**
     *
     * @param scriptField syntax text are to wrap
     * @param foldIndicatorEnabled flag, whether fold indicator should be enabled
     * @deprecated use {@link #getInstance(JSyntaxTextArea, boolean)} instead
     */
    @Deprecated
    public JTextScrollPane(JSyntaxTextArea scriptField, boolean foldIndicatorEnabled) {
        super(scriptField);
        super.setFoldIndicatorEnabled(foldIndicatorEnabled);
    }

    public static JTextScrollPane getInstance(JSyntaxTextArea scriptField, boolean foldIndicatorEnabled) {
        try {
            JTextScrollPane scrollPane = new JTextScrollPane(scriptField, foldIndicatorEnabled);

            javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
            toolBar.setFloatable(false);

            // Line wrap Checkbox
            javax.swing.JCheckBox lineWrapCheck = new javax.swing.JCheckBox(JMeterUtils.getResString("jsyntaxtextarea_line_wrap"));
            lineWrapCheck.setSelected(scriptField.getLineWrap());
            lineWrapCheck.addActionListener(e -> scriptField.setLineWrap(lineWrapCheck.isSelected()));

            // Line wrap word Checkbox
            javax.swing.JCheckBox wrapStyleCheck = new javax.swing.JCheckBox(JMeterUtils.getResString("jsyntaxtextarea_line_wrap_style"));
            wrapStyleCheck.setSelected(scriptField.getWrapStyleWord());
            wrapStyleCheck.addActionListener(e -> scriptField.setWrapStyleWord(wrapStyleCheck.isSelected()));

            // EOL visible Checkbox
            javax.swing.JCheckBox eolCheck = new javax.swing.JCheckBox(JMeterUtils.getResString("jsyntaxtextarea_line_eol"));
            eolCheck.setSelected(scriptField.getEOLMarkersVisible());
            eolCheck.addActionListener(e -> scriptField.setEOLMarkersVisible(eolCheck.isSelected()));

            // Whhitespace visible Checkbox
            javax.swing.JCheckBox whitespaceCheck = new javax.swing.JCheckBox(JMeterUtils.getResString("jsyntaxtextarea_line_whitespace"));
            whitespaceCheck.setSelected(scriptField.isWhitespaceVisible());
            whitespaceCheck.addActionListener(e -> scriptField.setWhitespaceVisible(whitespaceCheck.isSelected()));

            // Line Numbers Checkbox
            javax.swing.JCheckBox lineNumbersCheck = new javax.swing.JCheckBox(JMeterUtils.getResString("jsyntaxtextarea_line_line_number"));
            lineNumbersCheck.setSelected(scrollPane.getLineNumbersEnabled());
            lineNumbersCheck.addActionListener(e -> scrollPane.setLineNumbersEnabled(lineNumbersCheck.isSelected()));

            // Code Folding Checkbox
            javax.swing.JCheckBox codeFoldingCheck = new javax.swing.JCheckBox(JMeterUtils.getResString("jsyntaxtextarea_line_code_folding"));
            codeFoldingCheck.setSelected(scrollPane.isFoldIndicatorEnabled());
            codeFoldingCheck.addActionListener(e -> scrollPane.setFoldIndicatorEnabled(codeFoldingCheck.isSelected()));

            // Add components to the toolbar
            toolBar.add(lineWrapCheck);
            toolBar.add(wrapStyleCheck);
            toolBar.add(eolCheck);
            toolBar.add(whitespaceCheck);
            toolBar.add(lineNumbersCheck);
            toolBar.add(codeFoldingCheck);

            scrollPane.setColumnHeaderView(toolBar);

            return scrollPane;
        } catch (NullPointerException npe) {
            if ("true".equals(System.getProperty("java.awt.headless"))) {
                return new JTextScrollPane();
            } else {
                throw npe;
            }
        }
    }

}
