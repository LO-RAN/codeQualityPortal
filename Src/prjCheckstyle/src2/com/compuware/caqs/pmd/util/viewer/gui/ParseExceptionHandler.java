package com.compuware.caqs.pmd.util.viewer.gui;


import com.compuware.caqs.pmd.util.viewer.util.NLS;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * handles parsing exceptions
 *
 * @author Boris Gruschko ( boris at gruschko.org )
 * @version $Id: ParseExceptionHandler.java,v 1.10 2006/02/10 14:15:31 tomcopeland Exp $
 */

public class ParseExceptionHandler extends JDialog implements ActionListener {
    private Exception exc;
    private JTextArea errorArea;
    private JButton okBtn;

    /**
     * creates the dialog
     *
     * @param parent dialog's parent
     * @param exc    exception to be handled
     */
    public ParseExceptionHandler(JFrame parent, Exception exc) {
        super(parent, NLS.nls("COMPILE_ERROR.DIALOG.TITLE"), true);
        this.exc = exc;
        init();
    }

    private void init() {
        errorArea = new JTextArea();
        errorArea.setEditable(false);
        errorArea.setText(exc.getMessage() + "\n");
        getContentPane().setLayout(new BorderLayout());
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        NLS.nls("COMPILE_ERROR.PANEL.TITLE"))));
        messagePanel.add(new JScrollPane(errorArea), BorderLayout.CENTER);
        getContentPane().add(messagePanel, BorderLayout.CENTER);
        JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okBtn = new JButton(NLS.nls("COMPILE_ERROR.OK_BUTTON.CAPTION"));
        okBtn.addActionListener(this);
        btnPane.add(okBtn);
        getRootPane().setDefaultButton(okBtn);
        getContentPane().add(btnPane, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okBtn) {
            dispose();
        }
    }
}
