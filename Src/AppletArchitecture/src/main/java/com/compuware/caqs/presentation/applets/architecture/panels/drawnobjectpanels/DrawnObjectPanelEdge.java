/*
 * EdgePanel.java
 *
 * Created on 25 octobre 2002, 15:43
 */

/**
 *
 * @author  fxa
 */
package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Edge;
import com.compuware.caqs.domain.architecture.serializeddata.Element;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsCouple;
import com.compuware.caqs.presentation.applets.architecture.I18n;

public class DrawnObjectPanelEdge extends DrawnObjectPanel implements ActionListener {

    protected Edge m_edge;

    protected JTextArea m_list = new JTextArea();
    protected JButton m_deleteBtn = new JButton(I18n.getString("Effacer"));
    protected JLabel m_label = new JLabel();
    protected ArchitectureModel m_model;

    /**
     * Creates a new instance of EdgePanel
     */
    public DrawnObjectPanelEdge( Edge edge) {
        this.m_model = ArchitectureModel.getInstance();
        this.m_edge = edge;


        if (!this.m_edge.isReal()) {

            this.add(new JLabel(this.m_edge.getFrom().getLbl() + I18n.getString("_->_") + this.m_edge.getTo().getLbl()));
            if (this.m_model.isModifiable()) {
                this.m_deleteBtn.addActionListener(this);
                this.add(this.m_deleteBtn);//,BorderLayout.CENTER);
            }
        } else {
            List<ElementsCouple> couples = this.m_edge.getCouples();
            StringBuffer text = new StringBuffer();
            for (int i = 0; i < couples.size(); i++) {
                ElementsCouple currCouple = (ElementsCouple) couples.get(i);
                Element from = currCouple.getFrom();
                Element to = currCouple.getTo();
                text.append(from.getLabel() + I18n.getString("_->_") + to.getLabel() + "\n");
            }
            this.m_list.setText(text.toString());

            this.setLayout(new BorderLayout());
            JScrollPane js = new JScrollPane();
            js.setViewportView(this.m_list);
            this.add(new JLabel(this.m_edge.getFrom().getLbl() + I18n.getString("_->_") + this.m_edge.getTo().getLbl()), BorderLayout.NORTH);
            this.add(js, BorderLayout.CENTER);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == this.m_deleteBtn) {
            this.deleButtonActionPerformed();
        }
    }

    private void deleButtonActionPerformed() {
        int result = JOptionPane.showConfirmDialog(null, I18n.getString("confirm_element_delete")); //le premier paramètre est la fenêtre mère. Il sert à centrer la boite de dialogue    dans la fenêtre mère.
        switch (result) {
            case JOptionPane.YES_OPTION:
                this.m_model.getEdgesList().deleteEdge(this.m_edge);
                ArchitectureModel.getInstance().fireSelectedItemDeleted();
                this.m_model.computeRealLinks();
                
                break;
        }
        this.m_model.fireModelChangedEvent();
    }

}
