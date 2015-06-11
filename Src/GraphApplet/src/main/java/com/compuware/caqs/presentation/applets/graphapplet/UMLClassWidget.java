package com.compuware.caqs.presentation.applets.graphapplet;

import java.awt.Font;
import java.awt.Image;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Utilities;

/**
 * @author cwfr-fdubois
 */
public class UMLClassWidget extends Widget {

    private static final Image IMAGE_CLASS = Utilities.loadImage ("images/class.gif"); // NOI18N
    private static final Image IMAGE_MEMBER = Utilities.loadImage ("images/variablePublic.gif"); // NOI18N
    private static final Image IMAGE_OPERATION = Utilities.loadImage ("images/methodPublic.gif"); // NOI18N

    private String id;
    private boolean mainNode;
    private LabelWidget className;
    private Widget members;
    private Widget operations;
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder (4);

    public UMLClassWidget (Scene scene, boolean mainNode) {
        super (scene);
        setLayout (LayoutFactory.createVerticalFlowLayout ());
        setBorder (BorderFactory.createLineBorder ());
        setOpaque (true);
        setCheckClipping (true);

        this.mainNode = mainNode;

        Widget classWidget = new Widget (scene);
        classWidget.setLayout (LayoutFactory.createHorizontalFlowLayout ());
        classWidget.setBorder (BORDER_4);

        ImageWidget classImage = new ImageWidget (scene);
        classImage.setImage (IMAGE_CLASS);
        classWidget.addChild (classImage);

        className = new LabelWidget (scene);
        className.setFont (scene.getDefaultFont ().deriveFont (Font.BOLD));
        classWidget.addChild (className);
        addChild (classWidget);

        addChild (new SeparatorWidget (scene, SeparatorWidget.Orientation.HORIZONTAL));

        members = new Widget (scene);
        members.setLayout (LayoutFactory.createVerticalFlowLayout ());
        members.setOpaque (false);
        members.setBorder (BORDER_4);
        addChild (members);

        addChild (new SeparatorWidget (scene, SeparatorWidget.Orientation.HORIZONTAL));

        operations = new Widget (scene);
        operations.setLayout (LayoutFactory.createVerticalFlowLayout ());
        operations.setOpaque (false);
        operations.setBorder (BORDER_4);
        addChild (operations);
    }

    public String getClassName () {
        return className.getLabel ();
    }

    public void setClassName (String className) {
        this.className.setLabel (className);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMainNode() {
        return this.mainNode;
    }

    public Widget createMember (String member) {
        Scene scene = getScene ();
        Widget widget = new Widget (scene);
        widget.setLayout (LayoutFactory.createHorizontalFlowLayout ());

        ImageWidget imageWidget = new ImageWidget (scene);
        imageWidget.setImage (IMAGE_MEMBER);
        widget.addChild (imageWidget);

        LabelWidget labelWidget = new LabelWidget (scene);
        labelWidget.setLabel (member);
        widget.addChild (labelWidget);

         return widget;
    }

    public Widget createOperation (String operation) {
        Scene scene = getScene ();
        Widget widget = new Widget (scene);
        widget.setLayout (LayoutFactory.createHorizontalFlowLayout ());

        ImageWidget imageWidget = new ImageWidget (scene);
        imageWidget.setImage (IMAGE_OPERATION);
        widget.addChild (imageWidget);

        LabelWidget labelWidget = new LabelWidget (scene);
        labelWidget.setLabel (operation);
        widget.addChild (labelWidget);

        return widget;
    }

    public void addMember (Widget memberWidget) {
        members.addChild (memberWidget);
    }

    public void removeMember (Widget memberWidget) {
        members.removeChild (memberWidget);
    }

    public void addOperation (Widget operationWidget) {
        operations.addChild (operationWidget);
    }

    public void removeOperation (Widget operationWidget) {
        operations.removeChild (operationWidget);
    }

}
