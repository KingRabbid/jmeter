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

import java.awt.Container;
import java.awt.Component;
import java.io.File;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class implementing a file open dialogue
 */
public final class FileDialoger {
    /**
     * The last directory visited by the user while choosing Files.
     */
    private static String lastJFCDirectory = null;

    private static JFileChooser jfc = new JFileChooser();

    private static final Logger LOG = LoggerFactory.getLogger(FileDialoger.class);

    /**
     * Prevent instantiation of utility class.
     */
    private FileDialoger() {
    }

    /**
     * Prompts the user to choose a file or a directory from their filesystems for our own
     * devious uses. This method maintains the last directory the user visited
     * before dismissing the dialog. This does NOT imply they actually chose a
     * file from that directory, only that they closed the dialog there. It is
     * the caller's responsibility to check to see if the selected file is
     * non-null.
     *
     * @return the JFileChooser that interacted with the user, after they are
     *         finished using it - null if no file was chosen
     */
    public static JFileChooser promptToOpenFile() {
        return promptToOpenFile((String)null);
    }

    /**
     * Prompts the user to choose a file or a directory from their filesystems for our own
     * devious uses. This method maintains the last directory the user visited
     * before dismissing the dialog. This does NOT imply they actually chose a
     * file from that directory, only that they closed the dialog there. It is
     * the caller's responsibility to check to see if the selected file is
     * non-null.
     * @param existingFileName The name of a file with path. If the filename points
     *             to an existing file, the directory in which it lies, will be used
     *             as the starting point for the returned JFileChooser.
     *
     * @return the JFileChooser that interacted with the user, after they are
     *         finished using it - null if no file was chosen
     */
    public static JFileChooser promptToOpenFile(String existingFileName) {
        return promptToOpenFile(new String[0], existingFileName);
    }

    /**
     * Prompts the user to choose a file or a directory from their filesystems for our own
     * devious uses. This method maintains the last directory the user visited
     * before dismissing the dialog. This does NOT imply they actually chose a
     * file from that directory, only that they closed the dialog there. It is
     * the caller's responsibility to check to see if the selected file is
     * non-null.
     * @param existingFileName The name of a file with path. If the filename points
     *             to an existing file, the directory in which it lies, will be used
     *             as the starting point for the returned JFileChooser.
     * @param onlyDirectories If true, only directories are displayed in the FileChooser
     *
     * @return the JFileChooser that interacted with the user, after they are
     *         finished using it - null if no file was chosen
     */
    public static JFileChooser promptToOpenFile(String existingFileName, boolean onlyDirectories) {
        return promptToOpenFile(new String[0], existingFileName, onlyDirectories);
    }


    /**
     * Prompts the user to choose a file or a directory from their filesystems for our own
     * devious uses. This method maintains the last directory the user visited
     * before dismissing the dialog. This does NOT imply they actually chose a
     * file from that directory, only that they closed the dialog there. It is
     * the caller's responsibility to check to see if the selected file is
     * non-null.
     * @param exts The list of allowed file extensions. If empty, any
     *             file extension is allowed
     *
     * @return the JFileChooser that interacted with the user, after they are
     *         finished using it - null if no file was chosen
     */
    public static JFileChooser promptToOpenFile(String[] exts) {
        return promptToOpenFile(exts, null);
    }

    /**
     * Prompts the user to choose a file or a directory from their filesystems for our own
     * devious uses. This method maintains the last directory the user visited
     * before dismissing the dialog. This does NOT imply they actually chose a
     * file from that directory, only that they closed the dialog there. It is
     * the caller's responsibility to check to see if the selected file is
     * non-null.
     * @param exts The list of allowed file extensions. If empty, any
     *             file extension is allowed
     * @param existingFileName The name of a file with path. If the filename points
     *             to an existing file, the directory in which it lies, will be used
     *             as the starting point for the returned JFileChooser.
     *
     * @return the JFileChooser that interacted with the user, after they are
     *         finished using it - null if no file was chosen
     */
    public static JFileChooser promptToOpenFile(String[] exts, String existingFileName) {
        return promptToOpenFile(exts, existingFileName, false);
    }

    /**
    * Prompts the user to choose a file or a directory from their filesystems for our own
    * devious uses. This method maintains the last directory the user visited
    * before dismissing the dialog. This does NOT imply they actually chose a
    * file from that directory, only that they closed the dialog there. It is
    * the caller's responsibility to check to see if the selected file is
    * non-null.
    * @param exts The list of allowed file extensions. If empty, any
    *             file extension is allowed
    * @param existingFileName The name of a file with path. If the filename points
    *             to an existing file, the directory in which it lies, will be used
    *             as the starting point for the returned JFileChooser.
     * @param onlyDirectories If true, only directories are displayed in the FileChooser
    *
    * @return the JFileChooser that interacted with the user, after they are
    *         finished using it - null if no file was chosen
    */
    public static JFileChooser promptToOpenFile(String[] exts, String existingFileName, boolean onlyDirectories) {
        return promptToOpenFile(GuiPackage.getInstance().getMainFrame(), exts, existingFileName, onlyDirectories);
    }

    /**
    * Prompts the user to choose a file or a directory from their filesystems for our own
    * devious uses. This method maintains the last directory the user visited
    * before dismissing the dialog. This does NOT imply they actually chose a
    * file from that directory, only that they closed the dialog there. It is
    * the caller's responsibility to check to see if the selected file is
    * non-null.
    * @param parentComponent Component parent of current element
    * @param exts The list of allowed file extensions. If empty, any
    *             file extension is allowed
    * @param existingFileName The name of a file with path. If the filename points
    *             to an existing file, the directory in which it lies, will be used
    *             as the starting point for the returned JFileChooser.
    * @param onlyDirectories If true, only directories are displayed in the FileChooser
    *
    * @return the JFileChooser that interacted with the user, after they are
    *         finished using it - null if no file was chosen
    */
    public static JFileChooser promptToOpenFile(Component parentComponent, String[] exts, String existingFileName, boolean onlyDirectories) {
       if (onlyDirectories) {
           jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       } else {
           jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
       }
       setCurrentDirOnJFC(existingFileName, lastJFCDirectory, System.getProperty("user.dir"));
       clearFileFilters();
       if(exts != null && exts.length > 0) {
           JMeterFileFilter currentFilter = new JMeterFileFilter(exts);
           jfc.addChoosableFileFilter(currentFilter);
           jfc.setAcceptAllFileFilterUsed(true);
           jfc.setFileFilter(currentFilter);
       }
       Action details = jfc.getActionMap().get("viewTypeDetails");
       details.actionPerformed(null);
       jfc.rescanCurrentDirectory();
       autoSizeColumnsLogic(jfc);
       enableAutoSizeColumns(jfc);
       int retVal = jfc.showOpenDialog(parentComponent);
       lastJFCDirectory = jfc.getCurrentDirectory().getAbsolutePath();

       if (retVal == JFileChooser.APPROVE_OPTION) {
           return jfc;
       }
       return null;
   }

    private static void setCurrentDirOnJFC(String... dirNames) {
        for (String dirName : dirNames) {
            if (StringUtils.isBlank(dirName)) {
                continue;
            }
            File possibleDir = new File(dirName);
            if (possibleDir.exists() && possibleDir.canRead()) {
                jfc.setCurrentDirectory(possibleDir);
                return;
            }
        }
        LOG.info("No valid initial directory found for: {}",
                    Arrays.asList(dirNames));
        jfc.setCurrentDirectory(null);
    }

    private static void clearFileFilters() {
        FileFilter[] filters = jfc.getChoosableFileFilters();
        for (FileFilter filter : filters) {
            jfc.removeChoosableFileFilter(filter);
        }
    }

    /**
     * Prompts the user to choose a file from their filesystems for our own
     * devious uses. This method maintains the last directory the user visited
     * before dismissing the dialog. This does NOT imply they actually chose a
     * file from that directory, only that they closed the dialog there. It is
     * the caller's responsibility to check to see if the selected file is
     * non-null.
     * @param filename  The name of a file with path. If the filename points
     *             to an existing file, the directory in which it lies, will be used
     *             as the starting point for the returned JFileChooser.
     *
     * @return the JFileChooser that interacted with the user, after they are
     *         finished using it - null if no file was chosen
     * @see #promptToOpenFile()
     */
    public static JFileChooser promptToSaveFile(String filename) {
        return promptToSaveFile(filename, null);
    }

    /**
     * Get a JFileChooser with a new FileFilter.
     *
     * @param filename file name
     * @param extensions list of extensions
     * @return the FileChooser - null if no file was chosen
     */
    public static JFileChooser promptToSaveFile(String filename, String[] extensions) {
        if (lastJFCDirectory == null) {
            String start = System.getProperty("user.dir", "");//$NON-NLS-1$//$NON-NLS-2$
            if (start.length() > 0) {
                jfc = new JFileChooser(new File(start));
            }
            lastJFCDirectory = jfc.getCurrentDirectory().getAbsolutePath();
        }
        String ext = ".jmx";//$NON-NLS-1$
        if (filename != null) {
            jfc.setDialogTitle(filename);
            jfc.setSelectedFile(filename.lastIndexOf(File.separator) > 0 ?
                    new File(filename) :
                    new File(lastJFCDirectory, filename));
            int i = -1;
            if ((i = filename.lastIndexOf('.')) > -1) {//$NON-NLS-1$
                ext = filename.substring(i);
            }
        }
        clearFileFilters();
        if (extensions != null) {
            jfc.addChoosableFileFilter(new JMeterFileFilter(extensions));
        } else {
            jfc.addChoosableFileFilter(new JMeterFileFilter(new String[] { ext }));
        }

        Action details = jfc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
        jfc.rescanCurrentDirectory();
        autoSizeColumnsLogic(jfc);
        enableAutoSizeColumns(jfc);

        int retVal = jfc.showSaveDialog(GuiPackage.getInstance().getMainFrame());
        jfc.setDialogTitle(null);
        lastJFCDirectory = jfc.getCurrentDirectory().getAbsolutePath();
        if (retVal == JFileChooser.APPROVE_OPTION) {
            return jfc;
        }
        return null;
    }

    /**
     *
     * @return The last directory visited by the user while choosing Files
     */
    public static String getLastJFCDirectory() {
        return lastJFCDirectory;
    }

    /**
     *
     * @param lastJFCDirectory The last directory visited by the user while choosing Files
     */
    public static void setLastJFCDirectory(String lastJFCDirectory) {
        FileDialoger.lastJFCDirectory = lastJFCDirectory;
    }
    // Java
    private static void enableAutoSizeColumns(JFileChooser chooser) {
        chooser.addPropertyChangeListener(evt -> {
            if (Arrays.asList(JFileChooser.DIRECTORY_CHANGED_PROPERTY).contains(evt.getPropertyName())) {
                autoSizeColumnsLogic(chooser);
            }
        });
    }

    private static Component findDetailsTable(Container parent) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JTable) {
                return comp;
            } else if (comp instanceof Container) {
                Component result = findDetailsTable((Container) comp);
                if (result != null) return result;
            }
        }
        return null;
    }

    private static void autoSizeColumnsLogic(JFileChooser chooser) {
        Component tableComp = findDetailsTable(chooser);
        if (tableComp instanceof JTable) {
            JTable table = (JTable) tableComp;
            TableColumnModel columnModel = table.getColumnModel();

            // First pass: Calculate optimal width for all columns except the first one
            int totalOtherColsWidth = 0;
            for (int col = 1; col < columnModel.getColumnCount(); col++) {
                TableColumn column = columnModel.getColumn(col);

                // Check header width
                TableCellRenderer headerRenderer = column.getHeaderRenderer();
                if (headerRenderer == null) {
                    headerRenderer = table.getTableHeader().getDefaultRenderer();
                }
                Component headerComp = headerRenderer.getTableCellRendererComponent(
                        table, column.getHeaderValue(), false, false, 0, col);
                int maxWidth = headerComp.getPreferredSize().width;

                // Check data width for each row
                for (int row = 0; row < table.getRowCount(); row++) {
                    TableCellRenderer renderer = table.getCellRenderer(row, col);
                    Component comp = table.prepareRenderer(renderer, row, col);
                    maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
                }

                // Add some padding and set column width
                int finalWidth = maxWidth + 5;
                column.setPreferredWidth(finalWidth);
                totalOtherColsWidth += finalWidth;
            }

            // Second pass: Set first column width to fill remaining space
            if (columnModel.getColumnCount() > 0) {
                int viewportWidth = table.getParent() instanceof JViewport
                        ? ((JViewport) table.getParent()).getWidth()
                        : table.getWidth();

                // Account for possible vertical scrollbar width
                int scrollbarWidth = 5; // Approximate scrollbar width
                int firstColWidth = viewportWidth - totalOtherColsWidth - scrollbarWidth - 5; // 1 for padding
                columnModel.getColumn(0).setPreferredWidth(firstColWidth);
            }
        }
    }
}
