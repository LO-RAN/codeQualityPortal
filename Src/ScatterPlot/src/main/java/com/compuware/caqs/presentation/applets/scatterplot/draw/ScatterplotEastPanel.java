package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.compuware.caqs.business.chart.resources.Messages;
import com.compuware.caqs.presentation.applets.scatterplot.data.Element;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterPlotDataSet;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterplotBubble;

public class ScatterplotEastPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6586988352501573145L;
    private JButton raz = null;
    private JButton export = null;
    private ScatterplotAppletContainer parent = null;
    private JList selectedMethodsList = null;
    private ScatterplotTree arbo = null;
    private JScrollPane arboScroll = null;
    private JScrollPane selectedElementsScroll = null;
    private JSplitPane split = null;

    public ScatterplotEastPanel(ScatterPlotDataSet d, ScatterplotAppletContainer p, Locale locale) {
        this.parent = p;
        this.setLayout(new BorderLayout());
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setBorder(BorderFactory.createEmptyBorder());

        JPanel buttonPanel = new JPanel();

        this.raz = new JButton(Messages.getString("caqs.scatterplot.RAZ", locale));
        this.raz.addActionListener(new RAZButtonActionListener(this.parent));
        buttonPanel.add(this.raz);
        this.export = new JButton(Messages.getString("caqs.scatterplot.export", locale));
        this.export.addMouseListener(new ExportButtonMouseListener(this.parent, locale));
        try {
            URL icon = new URL(p.getServerAdress() + "images/page_excel.gif");
            this.export.setIcon(new ImageIcon(icon));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        buttonPanel.add(this.export);

        this.arbo = new ScatterplotTree(this.parent);
        arboScroll = new JScrollPane(this.arbo.getComponent());
        arboScroll.setMinimumSize(new Dimension(300, 230));

        this.selectedMethodsList = new JList();
        this.selectedMethodsList.addMouseListener(new SelectedElementListMouseListener(this.parent, this.selectedMethodsList, locale));
        this.selectedMethodsList.setModel(new DefaultListModel());
        this.selectedMethodsList.setCellRenderer(new SelectedElementsRenderer());
        this.selectedElementsScroll = new JScrollPane(this.selectedMethodsList);
        this.selectedElementsScroll.setPreferredSize(new Dimension(300, 230));

        this.add(buttonPanel, BorderLayout.NORTH);
        split.setTopComponent(this.arboScroll);
        split.setBottomComponent(this.selectedElementsScroll);

        this.add(split, BorderLayout.CENTER);
    }

    public void resizeWidthComponents() {
        int width = this.getWidth() - 10;
        this.arboScroll.setSize(new Dimension(width, this.arboScroll.getHeight()));
        this.selectedMethodsList.setSize(new Dimension(width, this.selectedMethodsList.getHeight()));
    }

    public void resizeHeightComponents() {
        int height = this.getHeight();
        this.split.setSize(new Dimension(this.split.getWidth(), height));
    }

    public void majSelectedElementsList() {
        ScatterPlotDataSet datas = this.parent.getDatasRetriever().getPlotDatas();
        List<ScatterplotBubble> selectedBubbles = datas.getSelectedBubbles();
        DefaultListModel dlm = (DefaultListModel) this.selectedMethodsList.getModel();
        dlm.clear();
        for (ScatterplotBubble bubble : selectedBubbles) {
            for (Element elt : bubble.getSelectedElements()) {
                dlm.addElement(new SelectedElementPanel(elt.getLabel(), elt.getIdElt()));
            }
        }
    }

    public void removeElementsFromList() {
        DefaultListModel dlm = (DefaultListModel) this.selectedMethodsList.getModel();
        dlm.removeAllElements();
    }

    public void deselectAll() {
        this.removeElementsFromList();
        this.arbo.clearSelection();
    }

    public void refresh() {
        this.removeElementsFromList();
        this.arbo.refreshTree();
        arboScroll.setViewportView(this.arbo.getComponent());
    }
}

class RAZButtonActionListener implements ActionListener {

    ScatterplotAppletContainer parent = null;

    public RAZButtonActionListener(ScatterplotAppletContainer p) {
        this.parent = p;
    }

    public void actionPerformed(ActionEvent ae) {
        this.parent.reset();
    }
}

class ExportButtonMouseListener extends MouseAdapter {

    public static final int EXPORT_ALL = 1;
    public static final int EXPORT_SELECTED = 2;
    private ScatterplotAppletContainer parent = null;
    private final Locale locale;

    public ExportButtonMouseListener(ScatterplotAppletContainer p, Locale l) {
        this.parent = p;
        this.locale = l;
    }

    public void mouseClicked(MouseEvent e) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem exportAllItem = new JMenuItem(Messages.getString("caqs.scatterplot.exportAll", locale));
        exportAllItem.addActionListener(new ExportActionListener(this.parent, EXPORT_ALL, this.locale));
        popup.add(exportAllItem);
        JMenuItem exportSelectedItem = new JMenuItem(Messages.getString("caqs.scatterplot.exportSelected", locale));
        exportSelectedItem.addActionListener(new ExportActionListener(this.parent, EXPORT_SELECTED, this.locale));
        popup.add(exportSelectedItem);
        popup.show((JButton) e.getSource(), e.getX(), e.getY());
        popup.setVisible(true);
    }
}

class ExportActionListener implements ActionListener {

    private final int typeExport;
    private ScatterplotAppletContainer parent;
    private final Locale locale;

    public ExportActionListener(ScatterplotAppletContainer p, int t, Locale l) {
        this.typeExport = t;
        this.parent = p;
        this.locale = l;
    }

    public void actionPerformed(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        //on commence par placer les noms des métriques
        sb.append("name;").
                append(this.parent.getDatasRetriever().getMetricX().getId()).
                append(";").
                append(this.parent.getDatasRetriever().getMetricY().getId()).
                append("\n");

        switch (this.typeExport) {
            case ExportButtonMouseListener.EXPORT_ALL:
                Vector<Element> elements = this.parent.getDatasRetriever().getPlotDatas().getAllElements();
                for (Element elt : elements) {
                    this.addElementToOutput(sb, elt);
                }
                break;
            case ExportButtonMouseListener.EXPORT_SELECTED:
                List<ScatterplotBubble> bubbles = this.parent.getDatasRetriever().getPlotDatas().getSelectedBubbles();
                for (ScatterplotBubble bubble : bubbles) {
                    for (Element elt : bubble.getSelectedElements()) {
                        this.addElementToOutput(sb, elt);
                    }
                }
                break;
        }


        JDialog dialog = new JDialog();
        dialog.setResizable(true);
        dialog.setModal(true);
        dialog.setTitle(Messages.getString("caqs.scatterplot.dataExportTitle", locale));
        dialog.setSize(500, 500);
        dialog.setLocation(200, 200);
        dialog.setLayout(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setText(sb.toString());
        JScrollPane scroll = new JScrollPane(ta);
        scroll.setPreferredSize(new Dimension(480, 460));
        JPanel panel = new JPanel();
        panel.add(scroll);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void addElementToOutput(StringBuilder sb, Element elt) {
        sb.append(elt.getLabel()).
                append(";").
                append(elt.getContainer().getX()).
                append(";").
                append(elt.getContainer().getY()).
                append("\n");
    }
}

class SelectedElementListMouseListener extends MouseAdapter {

    private final ScatterplotAppletContainer parent;
    private final JList parentList;
    private final Locale locale;

    public SelectedElementListMouseListener(ScatterplotAppletContainer p, JList l, Locale locale) {
        this.parent = p;
        this.parentList = l;
        this.locale = locale;
    }

    private void openBrowser(String idElt) {
        String url = parent.getServerAdress() + "RetrieveSourceFile.do?id_elt=" +
                idElt + "&sizePx=" + 10;
        ScatterplotDisplaySourceDialog dialog = new ScatterplotDisplaySourceDialog(url);
        dialog.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {
        JList list = (JList) e.getSource();
        //clic gauche et double clic
        if (e.getButton() == 1 && e.getClickCount() == 2) {
            int index = list.locationToIndex(e.getPoint());
            SelectedElementPanel obj = (SelectedElementPanel) list.getModel().getElementAt(index);
            this.openBrowser(obj.getIdElt());
        } else if (e.getButton() == 3) {
            //clic gauche
            list.setSelectedValue(e.getSource(), false);
            JPopupMenu popup = new JPopupMenu();
            JMenuItem browserItem = new JMenuItem(Messages.getString("caqs.scatterplot.browser", locale));
            browserItem.addActionListener(new RightClickPopupListener());
            popup.add(browserItem);
            popup.show((JList) e.getSource(), e.getX(), e.getY());
            popup.setVisible(true);
        }
    }

    class RightClickPopupListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object o = parentList.getSelectedValue();
            if (o instanceof SelectedElementPanel) {
                SelectedElementPanel obj = (SelectedElementPanel) o;
                openBrowser(obj.getIdElt());
            }
        }
    }
}

class SelectedElementsRenderer extends DefaultListCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 7999856824453719895L;

    /* This is the only method defined by ListCellRenderer.  We just
     * reconfigure the Jlabel each time we're called.
     */
    public Component getListCellRendererComponent(
            JList list,
            Object value, // value to display
            int index, // cell index
            boolean iss, // is the cell selected
            boolean chf) // the list and the cell have the focus
    {
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, iss, chf);

        /* We additionally set the JLabels icon property here.
         */
        //String s = value.toString();
        //setIcon((s.length > 10) ? longIcon : shortIcon);

        return this;
    }
}
