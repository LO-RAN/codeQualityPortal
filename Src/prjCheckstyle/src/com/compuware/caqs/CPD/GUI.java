/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.CPD;

import javax.swing.*;

import com.compuware.caqs.metricsCollector.Metrics;
import com.compuware.caqs.metricsCollector.PHPRulesChecking;

import net.sourceforge.pmd.PMD;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Hashtable;

public class GUI implements CPDListener {

    private static class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class FindBugs implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                public void run() {
                    tokenizingFilesBar.setValue(0);
                    tokenizingFilesBar.setString("");
                    resultsTextArea.setText("");
                    phaseLabel.setText("");
                    
                    findBugs();
                }
            }).start();
        }
    }
    
    private class CalculateMetrics implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                public void run() {
                    tokenizingFilesBar.setValue(0);
                    tokenizingFilesBar.setString("");
                    resultsTextArea.setText("");
                    phaseLabel.setText("");
                    
                    calculatemetrics();
                }
            }).start();
        }
    }
    
    private class GoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                public void run() {
                    tokenizingFilesBar.setValue(0);
                    tokenizingFilesBar.setString("");
                    resultsTextArea.setText("");
                    phaseLabel.setText("");
                   
                    go();
                }
            }).start();
        }
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int ret = fcSave.showSaveDialog(GUI.this.frame);
            File f = fcSave.getSelectedFile();
            if (f == null || ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
            if (!f.canWrite()) {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileOutputStream(f));
                    if (evt.getActionCommand().equals(".txt")) {
                        pw.write(new SimpleRenderer().render(matches.iterator()));
                    } else if (evt.getActionCommand().equals(".xml")) {
                        pw.write(new XMLRenderer().render(matches.iterator()));
                    } else if (evt.getActionCommand().equals(".csv")) {
                        pw.write(new CSVRenderer().render(matches.iterator()));
                    }
                    pw.flush();
                    JOptionPane.showMessageDialog(frame, "File saved");
                } catch (IOException e) {
                    error("Couldn't save file" + f.getAbsolutePath(), e);
                } finally {
                    if (pw != null) {
                        pw.close();
                    }
                }
            } else {
                error("Could not write to file " + f.getAbsolutePath(), null);
            }
        }

        private void error(String message, Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(GUI.this.frame, message);
        }

    }

    private class BrowseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser(rootDirectoryField.getText());
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.showDialog(frame, "Select");
            if (fc.getSelectedFile() != null) {
                rootDirectoryField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private JTextField rootDirectoryField = new JTextField("C:\\");//user.home
    private JTextField minimumLengthField = new JTextField("75");
    
    public static JLabel phaseLabel = new JLabel();
    public static JProgressBar tokenizingFilesBar = new JProgressBar();
    private JTextArea resultsTextArea = new JTextArea();
    private JCheckBox recurseCheckbox = new JCheckBox("", true);
    private JCheckBox ignoreLiteralsCheckbox = new JCheckBox("", false);
    private JComboBox languageBox = new JComboBox();
    public static JTextField extensionField = new JTextField(".java");
    private JFileChooser fcSave = new JFileChooser();

    private JFrame frame;

    private List matches = new ArrayList();

    public GUI() {
        frame = new JFrame("CAQS - mini GUI");
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');
        JMenuItem saveItem = new JMenuItem("Save Text");
        saveItem.setMnemonic('s');
        saveItem.addActionListener(new SaveListener());
        fileMenu.add(saveItem);
        saveItem.setActionCommand(".txt");
        saveItem = new JMenuItem("Save XML");
        saveItem.addActionListener(new SaveListener());
        fileMenu.add(saveItem);
        saveItem.setActionCommand(".xml");
        saveItem = new JMenuItem("Save CSV");
        saveItem.setActionCommand(".csv");
        saveItem.addActionListener(new SaveListener());
        fileMenu.add(saveItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('x');
        exitItem.addActionListener(new CancelListener());
        fileMenu.add(exitItem);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // first make all the buttons
        JButton browseButton = new JButton("Browse");
        browseButton.setMnemonic('b');
        browseButton.addActionListener(new BrowseListener());
        JButton goButton = new JButton("Search Copy-Paste Pattern");
        goButton.setMnemonic('g');
        goButton.addActionListener(new GoListener());
        JButton cxButton = new JButton("Cancel");
        cxButton.addActionListener(new CancelListener());
        JButton metricsButton = new JButton("Calculate metrics");
        metricsButton.setMnemonic('m');
        metricsButton.addActionListener(new CalculateMetrics());
        JButton bugsButton = new JButton("Find bugs");
        bugsButton.setMnemonic('f');
        bugsButton.addActionListener(new FindBugs());
        
        JPanel settingsPanel = makeSettingsPanel(browseButton, goButton, cxButton, metricsButton, bugsButton);
        JPanel progressPanel = makeProgressPanel();
        JPanel resultsPanel = makeResultsPanel();

        frame.getContentPane().setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(settingsPanel, BorderLayout.NORTH);
        topPanel.add(progressPanel, BorderLayout.CENTER);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(resultsPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.show();
    }

    private JPanel makeSettingsPanel(JButton browseButton, JButton goButton, JButton cxButton, JButton mButton, JButton fButton) {
        JPanel settingsPanel = new JPanel();
        GridBagHelper helper = new GridBagHelper(settingsPanel, new double[]{0.1,0.3,0.1, 0.1, 0.1, 0.1, 0.05, 0.05, 0.05, 0.05});
        
        helper.addLabel("source directory:");
        helper.add(rootDirectoryField,5);
        helper.add(browseButton,1);
        
        helper.addLabel("Also scan subdirectories?");
        helper.add(recurseCheckbox);
        
        
        //helper.addLabel("Language:");
        helper.add(languageBox,1);
        languageBox.addItem("Java");
        languageBox.addItem("C++");
        languageBox.addItem("VB");
        languageBox.addItem("PHP");
        languageBox.addItem("Ruby");
        helper.nextRow();
        helper.add(mButton);
        helper.add(fButton);
        helper.add(goButton);
        helper.addLabel("Extension:");
        helper.add(extensionField);
        extensionField.setEnabled(false);
        
        
        
        
        
        helper.add(cxButton);
        
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        return settingsPanel;
    }

    private JPanel makeProgressPanel() {
        JPanel progressPanel = new JPanel();
        final double[] weights = {0.0, 0.8, 0.4, 0.2};
        GridBagHelper helper = new GridBagHelper(progressPanel, weights);
        helper.addLabel("Tokenizing files:");
        helper.add(tokenizingFilesBar, 3);
        helper.nextRow();
        helper.addLabel("Phase:");
        helper.add(phaseLabel);
        
        helper.nextRow();
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        return progressPanel;
    }

    private JPanel makeResultsPanel() {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());
        JScrollPane areaScrollPane = new JScrollPane(resultsTextArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(600, 300));
        resultsPanel.add(areaScrollPane, BorderLayout.CENTER);
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        
        return resultsPanel;
    }

    private void findBugs() {
        try {
            if (!(new File(rootDirectoryField.getText())).exists()) {
                JOptionPane.showMessageDialog(frame,
                        "Can't read from that root source directory",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Language language = null;
            
            LanguageFactory lf = new LanguageFactory();
            Properties p = new Properties();
            p.setProperty(JavaTokenizer.IGNORE_LITERALS, String.valueOf(ignoreLiteralsCheckbox.isSelected()));
            p.setProperty(LanguageFactory.EXTENSION, extensionField.getText());
            if (languageBox.getSelectedItem().equals("Java")) {
                language = lf.createLanguage(LanguageFactory.JAVA_KEY, p);
            } else if (languageBox.getSelectedItem().equals("C++")) {
                language = lf.createLanguage(LanguageFactory.CPP_KEY);
            } else if (languageBox.getSelectedItem().equals("Ruby")) {
                language = lf.createLanguage(LanguageFactory.RUBY_KEY);
            } else if (languageBox.getSelectedItem().equals("by extension...")) {
                language = lf.createLanguage(LanguageFactory.BY_EXTENSION, p);
            } else if (languageBox.getSelectedItem().equals("PHP")) {
                language = lf.createLanguage(LanguageFactory.PHP_KEY);
            } else if (languageBox.getSelectedItem().equals("VB")) {
                language = lf.createLanguage(LanguageFactory.VB_KEY);
            }
            tokenizingFilesBar.setMinimum(0);
            
            
            
            
            String report="";
            System.out.println("etude du répertoire "+rootDirectoryField.getText());
            PHPRulesChecking m = new PHPRulesChecking(rootDirectoryField.getText(), language.toString());
            report=m.hits;
                 
            
            if (report.length() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "Done; couldn't find any duplicates longer than " + minimumLengthField.getText() + " tokens");
            } else {
                resultsTextArea.setText(report);
            }
            
        } catch (Throwable t) {
            t.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Halted due to " + t.getClass().getName() + "; " + t.getMessage());
        }
    }
    
    private void calculatemetrics() {
        try {
            if (!(new File(rootDirectoryField.getText())).exists()) {
                JOptionPane.showMessageDialog(frame,
                        "Can't read from that root source directory",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Language language = null;
            
            LanguageFactory lf = new LanguageFactory();
            Properties p = new Properties();
            p.setProperty(JavaTokenizer.IGNORE_LITERALS, String.valueOf(ignoreLiteralsCheckbox.isSelected()));
            p.setProperty(LanguageFactory.EXTENSION, extensionField.getText());
            if (languageBox.getSelectedItem().equals("Java")) {
                language = lf.createLanguage(LanguageFactory.JAVA_KEY, p);
            } else if (languageBox.getSelectedItem().equals("C++")) {
                language = lf.createLanguage(LanguageFactory.CPP_KEY);
            } else if (languageBox.getSelectedItem().equals("Ruby")) {
                language = lf.createLanguage(LanguageFactory.RUBY_KEY);
            } else if (languageBox.getSelectedItem().equals("by extension...")) {
                language = lf.createLanguage(LanguageFactory.BY_EXTENSION, p);
            } else if (languageBox.getSelectedItem().equals("PHP")) {
                language = lf.createLanguage(LanguageFactory.PHP_KEY);
            } else if (languageBox.getSelectedItem().equals("VB")) {
                language = lf.createLanguage(LanguageFactory.VB_KEY);
            }
            
            CPD cpd = new CPD(Integer.parseInt(minimumLengthField.getText()), language);
            cpd.setCpdListener(this);
            tokenizingFilesBar.setMinimum(0);
            phaseLabel.setText("");
            
            
            
            
            String report="";
            System.out.println("etude du répertoire "+rootDirectoryField.getText());
            Metrics m = new Metrics(rootDirectoryField.getText(), language);
               	
            report=m.getClassResults();
        	/*Hashtable ht=m.getMethodResults();
        	for(Enumeration ite=ht.elements();ite.hasMoreElements();){
        		report+=(String)ite.nextElement()+" "+ht.get(ite.toString())+PMD.EOL;
        	}*/
            report+=m.getMethodResults();        
            
            if (report.length() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "Done; couldn't find any duplicates longer than " + minimumLengthField.getText() + " tokens");
            } else {
                resultsTextArea.setText(report);
            }
            
        } catch (Throwable t) {
            t.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Halted due to " + t.getClass().getName() + "; " + t.getMessage());
        }
    }
    
    private void go() {
        try {
            if (!(new File(rootDirectoryField.getText())).exists()) {
                JOptionPane.showMessageDialog(frame,
                        "Can't read from that root source directory",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Language language = null;
            LanguageFactory lf = new LanguageFactory();
            Properties p = new Properties();
            p.setProperty(JavaTokenizer.IGNORE_LITERALS, String.valueOf(ignoreLiteralsCheckbox.isSelected()));
            p.setProperty(LanguageFactory.EXTENSION, extensionField.getText());
            if (languageBox.getSelectedItem().equals("Java")) {
                language = lf.createLanguage(LanguageFactory.JAVA_KEY, p);
            } else if (languageBox.getSelectedItem().equals("C++")) {
                language = lf.createLanguage(LanguageFactory.CPP_KEY);
            } else if (languageBox.getSelectedItem().equals("Ruby")) {
                language = lf.createLanguage(LanguageFactory.RUBY_KEY);
            } else if (languageBox.getSelectedItem().equals("by extension...")) {
                language = lf.createLanguage(LanguageFactory.BY_EXTENSION, p);
            } else if (languageBox.getSelectedItem().equals("PHP")) {
                language = lf.createLanguage(LanguageFactory.PHP_KEY);
            }
            CPD cpd = new CPD(Integer.parseInt(minimumLengthField.getText()), language);
            cpd.setCpdListener(this);
            tokenizingFilesBar.setMinimum(0);
            phaseLabel.setText("");
            if (rootDirectoryField.getText().endsWith(".class")
                    || rootDirectoryField.getText().endsWith(".php")
                    || rootDirectoryField.getText().endsWith(".rb")
                    || rootDirectoryField.getText().endsWith(".java")
                    || rootDirectoryField.getText().endsWith(".cpp")
                    || rootDirectoryField.getText().endsWith(".c")) {
                cpd.add(new File(rootDirectoryField.getText()));
            } else {
                if (recurseCheckbox.isSelected()) {
                    cpd.addRecursively(rootDirectoryField.getText());
                } else {
                    cpd.addAllInDirectory(rootDirectoryField.getText());
                }
            }
            
            matches = new ArrayList();
            for (Iterator i = cpd.getMatches(); i.hasNext();) {
                Match m = (Match) i.next();
                matches.add(m);
            }

            String report = new SimpleRenderer().render(cpd.getMatches());
            if (report.length() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "Done; couldn't find any duplicates longer than " + minimumLengthField.getText() + " tokens");
            } else {
                resultsTextArea.setText(report);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Halted due to " + t.getClass().getName() + "; " + t.getMessage());
        }
    }

    // CPDListener
    public void phaseUpdate(int phase) {
        phaseLabel.setText(getPhaseText(phase));
    }

    public String getPhaseText(int phase) {
        switch (phase) {
            case CPDListener.INIT:
                return "Initializing";
            case CPDListener.HASH:
                return "Hashing";
            case CPDListener.MATCH:
                return "Matching";
            case CPDListener.GROUPING:
                return "Grouping";
            case CPDListener.DONE:
                return "Done";
            default :
                return "Unknown";
        }
    }

    public void addedFile(int fileCount, File file) {
        tokenizingFilesBar.setMaximum(fileCount);
        tokenizingFilesBar.setValue(tokenizingFilesBar.getValue() + 1);
    }
    // CPDListener

    public static void main(String[] args) {
        //this should prevent the disk not found popup
        System.setSecurityManager(null);
        new GUI();
    }

}
