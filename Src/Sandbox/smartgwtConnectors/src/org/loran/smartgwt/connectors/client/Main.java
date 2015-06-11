package org.loran.smartgwt.connectors.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.LineCap;
import com.smartgwt.client.types.LinePattern;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.drawing.ColorStop;
import com.smartgwt.client.widgets.drawing.DrawCurve;
import com.smartgwt.client.widgets.drawing.DrawItem;
import com.smartgwt.client.widgets.drawing.DrawLabel;
import com.smartgwt.client.widgets.drawing.DrawLine;
import com.smartgwt.client.widgets.drawing.DrawLinePath;
import com.smartgwt.client.widgets.drawing.DrawOval;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.drawing.DrawPath;
import com.smartgwt.client.widgets.drawing.DrawRect;
import com.smartgwt.client.widgets.drawing.DrawSector;
import com.smartgwt.client.widgets.drawing.DrawTriangle;
import com.smartgwt.client.widgets.drawing.LinearGradient;
import com.smartgwt.client.widgets.drawing.Point;
import com.smartgwt.client.widgets.drawing.RadialGradient;
import com.smartgwt.client.widgets.drawing.SimpleGradient;
import com.smartgwt.client.widgets.drawing.events.ClickEvent;
import com.smartgwt.client.widgets.drawing.events.ClickHandler;
import com.smartgwt.client.widgets.drawing.events.MouseDownEvent;
import com.smartgwt.client.widgets.drawing.events.MouseDownHandler;
import com.smartgwt.client.widgets.drawing.events.MouseUpEvent;
import com.smartgwt.client.widgets.drawing.events.MouseUpHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {
    
    private DrawPane drawPane;

    private DynamicForm lineStyleDynamicForm;

    private DrawPane drawPane2;

    private DrawPane drawPane3;

    private DynamicForm simpleGradientDynamicForm;

    private DynamicForm linearGradientDynamicForm;

    private DrawPane drawPane4;

    private DynamicForm linearGradientSlidersDynamicForm;

    private DrawPane drawPane5;

    private DynamicForm radialGradientDynamicForm;

    private DynamicForm radielGradientSliderDynamicForm;


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        
        shapeGalleryZoomRotate();
        
        lineStyles();
        
        simpleGradient();
        
        linearGradient();

        radialGradient();
        
        /**/
    }

    private void shapeGalleryZoomRotate() {
        drawPane = new DrawPane();
        drawPane.setHeight(450);
        drawPane.setWidth(700);
        drawPane.setLeft(25);
        drawPane.setShowEdges(true);
        drawPane.setEdgeSize(4);
        drawPane.setBackgroundColor("papayawhip");
        drawPane.setOverflow(Overflow.HIDDEN);
        drawPane.setCursor(Cursor.AUTO);
        drawPane.setID("DrawPane1");
        drawPane.draw();

        DrawLabel triangleLabel = new DrawLabel();
        triangleLabel.setDrawPane(drawPane);
        triangleLabel.setLeft(50);
        triangleLabel.setTop(175);
        triangleLabel.setContents("Triangle");
        triangleLabel.draw();
     
        DrawTriangle drawTriangle = new DrawTriangle();
        drawTriangle.setDrawPane(drawPane);
        drawTriangle.setPoints(new Point(100,50),new Point(150,150),new Point(50,150));
        drawTriangle.setID("DrawTriangel1");
        drawTriangle.draw();

        DrawLabel curveLabel = new DrawLabel();
        curveLabel.setDrawPane(drawPane);
        curveLabel.setLeft(200);
        curveLabel.setTop(175);
        curveLabel.setContents("Curve");
        curveLabel.draw();
        
        DrawCurve drawCurve = new DrawCurve();
        drawCurve.setDrawPane(drawPane);
        drawCurve.setStartPoint(new Point(200,50));
        drawCurve.setEndPoint(new Point(300,150));
        drawCurve.setControlPoint1(new Point(250,0));
        drawCurve.setControlPoint2(new Point(250,200));
        drawCurve.draw();

        DrawLabel linePathLabel = new DrawLabel();
        linePathLabel.setDrawPane(drawPane);
        linePathLabel.setLeft(350);
        linePathLabel.setTop(175);
        linePathLabel.setContents("Line Path");
        linePathLabel.draw();
        
        DrawLinePath drawLinePath = new DrawLinePath();
        drawLinePath.setDrawPane(drawPane);
        drawLinePath.setID("drawLinePath1");
        drawLinePath.setStartPoint(new Point(350,50));
        drawLinePath.setEndPoint(new Point(450,150));
        drawLinePath.draw();

        DrawLabel pathLabel = new DrawLabel();
        pathLabel.setDrawPane(drawPane);
        pathLabel.setLeft(500);
        pathLabel.setTop(175);
        pathLabel.setContents("Path");
        pathLabel.draw();
        
        DrawPath drawPath = new DrawPath();
        drawPath.setDrawPane(drawPane);
        drawPath.setID("DrawPath1");
        drawPath.setPoints(
                new Point(500,50),
                new Point(525,50),
                new Point(550,75),
                new Point(575,75),
                new Point(600,75),
                new Point(600,125),
                new Point(575,125),
                new Point(550,125),
                new Point(525,150),
                new Point(500,150)
        );
        drawPath.draw();

        DrawLabel ovalLabel = new DrawLabel();
        ovalLabel.setDrawPane(drawPane);
        ovalLabel.setLeft(50);
        ovalLabel.setTop(415);
        ovalLabel.setContents("Oval");
        ovalLabel.draw();
        
        DrawOval drawOval = new DrawOval();
        drawOval.setDrawPane(drawPane);
        drawOval.setLeft(50);
        drawOval.setTop(300);
        drawOval.setWidth(100);
        drawOval.setHeight(100);
        drawOval.draw();

        DrawLabel rectLabel = new DrawLabel();
        rectLabel.setDrawPane(drawPane);
        rectLabel.setLeft(200);
        rectLabel.setTop(415);
        rectLabel.setContents("Rect");
        rectLabel.draw();
        
        DrawRect drawRect = new DrawRect();
        drawRect.setDrawPane(drawPane);
        drawRect.setLeft(200);
        drawRect.setTop(300);
        drawRect.setWidth(150);
        drawRect.setHeight(100);
        drawRect.draw();

        DrawLabel lineLabel = new DrawLabel();
        lineLabel.setDrawPane(drawPane);
        lineLabel.setLeft(400);
        lineLabel.setTop(415);
        lineLabel.setContents("Line");
        lineLabel.draw();
        
        DrawLine drawLine = new DrawLine();
        drawLine.setDrawPane(drawPane);
        drawLine.setStartPoint(new Point(400,300));
        drawLine.setEndPoint(new Point(500,400));
        drawLine.draw();

        DrawLabel sectorLabel = new DrawLabel();
        sectorLabel.setDrawPane(drawPane);
        sectorLabel.setLeft(550);
        sectorLabel.setTop(415);
        sectorLabel.setContents("Sector");
        sectorLabel.draw();
        
        DrawSector drawSector = new DrawSector();
        drawSector.setDrawPane(drawPane);
        drawSector.setCenterPoint(new Point(550,300));
        drawSector.setStartAngle(0);
        drawSector.setEndAngle(90);
        drawSector.setRadius(100);
        drawSector.draw();

        DrawItem[] drawItems = drawPane.getDrawItems();
        for (int i = 0; i < drawItems.length; i++) {
            DrawItem drawItem = drawItems[i];
            drawItem.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    SC.logWarn("Click on " + event.getSource());
                    
                }
            });
            drawItem.addMouseDownHandler(new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                   SC.logWarn("Mouse is Down on " + event.getSource());
                    
                }
            });
            drawItem.addMouseUpHandler(new MouseUpHandler() {
                @Override
                public void onMouseUp(MouseUpEvent event) {
                    SC.logWarn("Mouse Up on " + event.getSource());    
                }
            });

        }
        
        ValueChangedHandler shapeRotationSliderValueChangeHandler = new ValueChangedHandler() {
            @Override
            public void onValueChanged(ValueChangedEvent event) {
                DrawItem[] drawItems = drawPane.getDrawItems();
                for (int i = 0; i < drawItems.length; i++) {
                    DrawItem drawItem = drawItems[i];
                    if(drawItem instanceof DrawLabel){
                        continue;
                    }
                    drawItem.rotateTo(event.getValue());
                }
                drawPane.redraw();
            }
        };
        
        Slider shapesRotationSlider = new Slider();
        shapesRotationSlider.setMinValue(0);
        shapesRotationSlider.setMaxValue(360);
        shapesRotationSlider.setNumValues(360);
        shapesRotationSlider.setWidth(400);
        shapesRotationSlider.setLeft(25);
        shapesRotationSlider.setTop(450);
        shapesRotationSlider.setValue(0);
        shapesRotationSlider.setTitle("Rotate Shapes");
        shapesRotationSlider.setVertical(false);
        shapesRotationSlider.addValueChangedHandler(shapeRotationSliderValueChangeHandler);
        shapesRotationSlider.draw();

        /*
         * Behavior does not exist on DrawPane through GWT right now.
         */
        ValueChangedHandler paneRotationSliderValueChangeHandler = new ValueChangedHandler() {
            @Override
            public void onValueChanged(ValueChangedEvent event) {
                drawPane.setRotation(event.getValue());
                drawPane.redraw();
            }
        };
        
        Slider paneRotationSlider = new Slider();
        paneRotationSlider.setMinValue(0);
        paneRotationSlider.setMaxValue(360);
        paneRotationSlider.setNumValues(360);
        paneRotationSlider.setWidth(400);
        paneRotationSlider.setLeft(25);
        paneRotationSlider.setTop(500);
        paneRotationSlider.setValue(0);
        paneRotationSlider.setTitle("Rotate Pane");
        paneRotationSlider.setVertical(false);
        paneRotationSlider.addValueChangedHandler(paneRotationSliderValueChangeHandler);
//        paneRotationSlider.disable();
        paneRotationSlider.draw();
        /**/

        ValueChangedHandler zoomSliderValueChangeHandler = new ValueChangedHandler() {
            @Override
            public void onValueChanged(ValueChangedEvent event) {
                Slider sliderItem = (Slider) event.getSource();
                drawPane.zoom(sliderItem.getValue());
            }
        };
        
        Slider zoomSlider = new Slider();
        zoomSlider.setMinValue(.10f);
        zoomSlider.setMaxValue(3.0f);
        zoomSlider.setNumValues(300);
        zoomSlider.setWidth(400);
        zoomSlider.setLeft(25);
        zoomSlider.setTop(550);
        zoomSlider.setValue(1.0f);
        zoomSlider.setRoundValues(false);
        zoomSlider.setRoundPrecision(2);
        zoomSlider.setTitle("Zoom Shapes");
        zoomSlider.setVertical(false);
        zoomSlider.addValueChangedHandler(zoomSliderValueChangeHandler);
        zoomSlider.draw();
    }

    private void lineStyles() {

        drawPane2 = new DrawPane();
        drawPane2.setHeight(200);
        drawPane2.setWidth(400);
        drawPane2.setTop(650);
        drawPane2.setLeft(25);
        drawPane2.setShowEdges(true);
        drawPane2.setEdgeSize(4);
        drawPane2.setBackgroundColor("papayawhip");
        drawPane2.setOverflow(Overflow.HIDDEN);
        drawPane2.setCursor(Cursor.AUTO);
        drawPane2.setID("DrawPane2");
        drawPane2.draw();
        
        SpinnerItem lineWidthSpinner = new SpinnerItem();
        lineWidthSpinner.setName("lineWidth");
        lineWidthSpinner.setTitle("Line Width");
        lineWidthSpinner.setDefaultValue(5);
        lineWidthSpinner.setMin(1);
        lineWidthSpinner.setMax(10);
        lineWidthSpinner.setStep(1);
        lineWidthSpinner.setWidth(150);
        
        LinkedHashMap<String, String> lineStyleValues = new LinkedHashMap<String, String>();
        lineStyleValues.put(LinePattern.SOLID.getValue(),"Solid");
        lineStyleValues.put(LinePattern.DOT.getValue(),"Dot");
        lineStyleValues.put(LinePattern.DASH.getValue(),"Dash");
        lineStyleValues.put(LinePattern.SHORTDOT.getValue(),"Short dot");
        lineStyleValues.put(LinePattern.SHORTDASH.getValue(),"Short dash");
        lineStyleValues.put(LinePattern.LONGDASH.getValue(),"Long dash");
        
        SelectItem lineStyleSelect = new SelectItem();
        lineStyleSelect.setName("lineStyle");
        lineStyleSelect.setTitle("Line Style");
        lineStyleSelect.setWidth(150);
        lineStyleSelect.setDefaultValue("solid");
        lineStyleSelect.setValueMap(lineStyleValues);
        
        LinkedHashMap<String, String> arrowheadStyleValues = new LinkedHashMap<String, String>();
        arrowheadStyleValues.put(LineCap.ROUND.getValue(),"Round");
        arrowheadStyleValues.put(LineCap.SQUARE.getValue(),"Square");
        arrowheadStyleValues.put(LineCap.BUTT.getValue(),"Butt");
        
        SelectItem arrowStyleSelect = new SelectItem();
        arrowStyleSelect.setName("arrowheadStyle");
        arrowStyleSelect.setTitle("Line Cap Style");
        arrowStyleSelect.setWidth(150);
        arrowStyleSelect.setDefaultValue("round");
        arrowStyleSelect.setValueMap(arrowheadStyleValues);

        ItemChangedHandler lineStyleItemChangedHandler = new ItemChangedHandler() {
            @Override
            public void onItemChanged(ItemChangedEvent event) {
                drawLines(drawPane2);
            }
        };
        
        lineStyleDynamicForm = new DynamicForm();
        lineStyleDynamicForm.setID("dynamicForm");
        lineStyleDynamicForm.setWidth(300);
        lineStyleDynamicForm.setHeight(100);
        lineStyleDynamicForm.setLeft(25);
        lineStyleDynamicForm.setTop(850);
        lineStyleDynamicForm.setFields(lineWidthSpinner,lineStyleSelect,arrowStyleSelect);
        lineStyleDynamicForm.addItemChangedHandler(lineStyleItemChangedHandler);
        lineStyleDynamicForm.draw();
        
        drawLines(drawPane2);
    }

    /**
     * This method is called to re-render the lines within the second draw pane to
     *  demonstrate the different line types and shapes.
     */
    private void drawLines(DrawPane drawPane) {


        drawPane.erase();

        int    lineWidth      = (Integer) lineStyleDynamicForm.getValue("lineWidth");
        String lineStyle      = (String) lineStyleDynamicForm.getValue("lineStyle");
        String arrowheadStyle = (String) lineStyleDynamicForm.getValue("arrowheadStyle");
        
        LineCap lineCap = null;
        
        if(LineCap.BUTT.getValue().equals(arrowheadStyle)){
            lineCap = LineCap.BUTT;
        } else if(LineCap.ROUND.getValue().equals(arrowheadStyle)){
            lineCap = LineCap.ROUND;
        } else if(LineCap.SQUARE.getValue().equals(arrowheadStyle)){
            lineCap = LineCap.SQUARE;
        }
        
        LinePattern linePattern = null;
        
        if(LinePattern.DASH.getValue().equals(lineStyle)){
            linePattern = LinePattern.DASH;
        } else if(LinePattern.DOT.getValue().equals(lineStyle)){
            linePattern = LinePattern.DOT;
        } else if(LinePattern.LONGDASH.getValue().equals(lineStyle)){
            linePattern = LinePattern.LONGDASH;
        } else if(LinePattern.SHORTDASH.getValue().equals(lineStyle)){
            linePattern = LinePattern.SHORTDASH;
        } else if(LinePattern.SHORTDOT.getValue().equals(lineStyle)){
            linePattern = LinePattern.SHORTDOT;
        } else if(LinePattern.SOLID.getValue().equals(lineStyle)){
            linePattern = LinePattern.SOLID;
        } 
        
        
        DrawLine drawLine = new DrawLine();
        drawLine.setDrawPane(drawPane);
        drawLine.setStartPoint(new Point(20,30));
        drawLine.setEndPoint(new Point(180,160));
        drawLine.setLineWidth(lineWidth);
        drawLine.setLineCap(lineCap);
        drawLine.setLinePattern(linePattern);
        drawLine.draw();
        
        DrawLinePath drawLinePath = new DrawLinePath();
        drawLinePath.setDrawPane(drawPane);
        drawLinePath.setStartTop(40);
        drawLinePath.setStartLeft(170);
        drawLinePath.setEndLeft(340);
        drawLinePath.setEndTop(160);
        drawLinePath.setLineWidth(lineWidth);
        drawLinePath.setLineCap(lineCap);
        drawLinePath.setLinePattern(linePattern);
        drawLinePath.draw();
    }
    
    private void simpleGradient(){

        /* ------------------------------------------- */
        
        drawPane3 = new DrawPane();
        drawPane3.setID("drawPane3");
        drawPane3.setTop(950);
        drawPane3.setHeight(400);
        drawPane3.setLeft(25);
        drawPane3.setWidth(400);
        drawPane3.setShowEdges(true);
        drawPane3.setEdgeSize(4);
        drawPane3.setBackgroundColor("papayawhip");
        drawPane3.setOverflow(Overflow.HIDDEN);
        drawPane3.setCursor(Cursor.AUTO);

        ItemChangedHandler simpleGradientItemChangedHandler = new ItemChangedHandler() {
            @Override
            public void onItemChanged(ItemChangedEvent event) {
                simpleGradientShapesDraw(drawPane3);
            }

        };
        ColorPickerItem startColorColorPicker = new ColorPickerItem("startColor","Start Color");
        ColorPickerItem endColorColorPicker   = new ColorPickerItem("endColor","End Color");
        SpinnerItem     directionSpinnerItem  = new SpinnerItem("direction","Direction");

        startColorColorPicker.setDefaultValue("#0000ff");
        endColorColorPicker.setDefaultValue("#00ff00");
        directionSpinnerItem.setDefaultValue(45);
        directionSpinnerItem.setMin(0);
        directionSpinnerItem.setMax(360);
        directionSpinnerItem.setStep(1);

        simpleGradientDynamicForm = new DynamicForm();
        simpleGradientDynamicForm.setID("simpleGradientDynamicForm");
        simpleGradientDynamicForm.setWidth(250);
        simpleGradientDynamicForm.setLeft(475);
        simpleGradientDynamicForm.setTop(950);
        simpleGradientDynamicForm.addItemChangedHandler(simpleGradientItemChangedHandler);
        simpleGradientDynamicForm.setFields(startColorColorPicker,endColorColorPicker,directionSpinnerItem);
        simpleGradientDynamicForm.draw();
        
        simpleGradientShapesDraw(drawPane3);
        drawPane3.draw();
    }
    
    /**
     * This method is called to re-render the pane that contains the shapes with
     *  a defined gradient in them.
     */
    private void simpleGradientShapesDraw(DrawPane drawPane) {

        drawPane.erase();
        
        SimpleGradient simpleGradient = new SimpleGradient();
        simpleGradient.setDirection( (Integer) simpleGradientDynamicForm.getValue("direction") );
        simpleGradient.setEndColor( (String) simpleGradientDynamicForm.getValue("endColor") );
        simpleGradient.setStartColor( (String) simpleGradientDynamicForm.getValue("startColor") );
        
        DrawTriangle drawTriangle = new DrawTriangle();
        drawTriangle.setDrawPane(drawPane);
        drawTriangle.setFillGradient(simpleGradient);
        drawTriangle.setPoints(new Point(100,50), new Point(150,150), new Point(50,150));
        drawTriangle.draw();
        
        DrawCurve drawCurve = new DrawCurve();
        drawCurve.setDrawPane(drawPane);
        drawCurve.setFillGradient(simpleGradient);
        drawCurve.setStartPoint(new Point(200,50));
        drawCurve.setEndPoint(new Point(340,150));
        drawCurve.setControlPoint1(new Point(270,0));
        drawCurve.setControlPoint2(new Point(270,200));
        drawCurve.draw();
        
        DrawOval drawOval = new DrawOval();
        drawOval.setDrawPane(drawPane);
        drawOval.setFillGradient(simpleGradient);
        drawOval.setLeft(50);
        drawOval.setTop(200);
        drawOval.setWidth(100);
        drawOval.setHeight(150);
        drawOval.draw();
        
        DrawRect drawRect = new DrawRect();
        drawRect.setDrawPane(drawPane);
        drawRect.setFillGradient(simpleGradient);
        drawRect.setLeft(200);
        drawRect.setTop(225);
        drawRect.setWidth(150);
        drawRect.setHeight(100);
        drawRect.draw();
               
    }

    private void linearGradient() {

        drawPane4 = new DrawPane();
        drawPane4.setTop(1400);
        drawPane4.setHeight(400);
        drawPane4.setLeft(25);
        drawPane4.setWidth(400);
        drawPane4.setShowEdges(true);
        drawPane4.setEdgeSize(4);
        drawPane4.setBackgroundColor("papayawhip");
        drawPane4.setOverflow(Overflow.HIDDEN);
        drawPane4.setCursor(Cursor.AUTO);
        drawPane4.draw();

        ItemChangedHandler linnearGradientItemChangedHandler = new ItemChangedHandler() {
            @Override
            public void onItemChanged(ItemChangedEvent event) {
                linearGradientShapesDraw(drawPane4);
            }

        };
        
        ColorPickerItem startColorColorPicker = new ColorPickerItem("startColor","Start Color");
        ColorPickerItem firstStopColorPicker  = new ColorPickerItem("firstStop","First Stop Color");
        ColorPickerItem secondStopColorPicker = new ColorPickerItem("secondStop","Second Stop Color");
        ColorPickerItem endColorColorPicker   = new ColorPickerItem("endColor","End Color");

        startColorColorPicker.setDefaultValue("#ff0000");
        firstStopColorPicker.setDefaultValue("#ffff00");
        secondStopColorPicker.setDefaultValue("#00ff00");
        endColorColorPicker.setDefaultValue("#0000ff");

        linearGradientDynamicForm = new DynamicForm();
        linearGradientDynamicForm.setWidth(250);
        linearGradientDynamicForm.setHeight(100);
        linearGradientDynamicForm.setLeft(475);
        linearGradientDynamicForm.setTop(1400);
        linearGradientDynamicForm.setFields(startColorColorPicker,firstStopColorPicker,secondStopColorPicker,endColorColorPicker);
        linearGradientDynamicForm.draw();
        linearGradientDynamicForm.addItemChangedHandler(linnearGradientItemChangedHandler);

        SliderItem x1Slider = new SliderItem("x1");
        SliderItem y1Slider = new SliderItem("y1");
        SliderItem x2Slider = new SliderItem("x2");
        SliderItem y2Slider = new SliderItem("y2");
        
        x1Slider.setMinValue(0);
        x1Slider.setMaxValue(100);
        x1Slider.setDefaultValue(20);
        x1Slider.setHeight(20);
        
        y1Slider.setMinValue(0);
        y1Slider.setMaxValue(100);
        y1Slider.setDefaultValue(20);
        y1Slider.setHeight(20);
        
        x2Slider.setMinValue(0);
        x2Slider.setMaxValue(100);
        x2Slider.setDefaultValue(80);
        x2Slider.setHeight(20);
        
        y2Slider.setMinValue(0);
        y2Slider.setMaxValue(100);
        y2Slider.setDefaultValue(80);
        y2Slider.setHeight(20);

        linearGradientSlidersDynamicForm = new DynamicForm();
        linearGradientSlidersDynamicForm.setTitleWidth(20);
        linearGradientSlidersDynamicForm.setWidth(290);
        linearGradientSlidersDynamicForm.setLeft(475);
        linearGradientSlidersDynamicForm.setTop(1500);
        linearGradientSlidersDynamicForm.setFields(x1Slider,y1Slider,x2Slider,y2Slider);
        linearGradientSlidersDynamicForm.draw();
        linearGradientSlidersDynamicForm.addItemChangedHandler(linnearGradientItemChangedHandler);
        
        linearGradientShapesDraw(drawPane4);
    }

    private void linearGradientShapesDraw(DrawPane drawPane) {
        drawPane.erase();

        ColorStop colorStop1 = new ColorStop();
        colorStop1.setColor(linearGradientDynamicForm.getValueAsString("startColor"));
        colorStop1.setOffset(0.0f);

        ColorStop colorStop2 = new ColorStop();
        colorStop2.setColor(linearGradientDynamicForm.getValueAsString("firstStop"));
        colorStop2.setOffset(0.33f);

        ColorStop colorStop3 = new ColorStop();
        colorStop3.setColor(linearGradientDynamicForm.getValueAsString("secondStop"));
        colorStop3.setOffset(0.66f);

        ColorStop colorStop4 = new ColorStop();
        colorStop4.setColor(linearGradientDynamicForm.getValueAsString("endColor"));
        colorStop4.setOffset(1.0f);
        
        int x1 = (Integer) linearGradientSlidersDynamicForm.getValue("x1");
        int y1 = (Integer) linearGradientSlidersDynamicForm.getValue("y1");
        int x2 = (Integer) linearGradientSlidersDynamicForm.getValue("x2");
        int y2 = (Integer) linearGradientSlidersDynamicForm.getValue("y2");
        
        LinearGradient linearGradient = new LinearGradient();
        linearGradient.setX1(x1+"%");
        linearGradient.setY1(y1+"%");
        linearGradient.setX2(x2+"%");
        linearGradient.setY2(y2+"%");
        linearGradient.setColorStops(colorStop1,colorStop2,colorStop3,colorStop4);
        
        drawPane.createLinearGradient("lg", linearGradient); 
       
        DrawTriangle drawTriangle = new DrawTriangle();
        drawTriangle.setDrawPane(drawPane);
        drawTriangle.setFillGradient(linearGradient);
        drawTriangle.setPoints(new Point(100,50),new Point(150,150),new Point(50,150));
        drawTriangle.draw();
        
        DrawCurve drawCurve = new DrawCurve();
        drawCurve.setDrawPane(drawPane);
        drawCurve.setFillGradient(linearGradient);
        drawCurve.setStartPoint(new Point(200,50));
        drawCurve.setEndPoint(new Point(340,150));
        drawCurve.setControlPoint1(new Point(270,0));
        drawCurve.setControlPoint2(new Point(270,200));
        drawCurve.draw();
        
        DrawOval drawOval = new DrawOval();
        drawOval.setDrawPane(drawPane);
        drawOval.setFillGradient(linearGradient);
        drawOval.setLeft(50);
        drawOval.setTop(200);
        drawOval.setWidth(100);
        drawOval.setHeight(150);
        drawOval.draw();
        
        DrawRect drawRect = new DrawRect();
        drawRect.setDrawPane(drawPane);
        drawRect.setFillGradient(linearGradient);
        drawRect.setLeft(200);
        drawRect.setTop(225);
        drawRect.setWidth(150);
        drawRect.setHeight(100);
        drawRect.draw();
    }
    
    private void radialGradient() {

        drawPane5 = new DrawPane();
        drawPane5.setTop(1850);
        drawPane5.setHeight(400);
        drawPane5.setLeft(25);
        drawPane5.setWidth(400);
        drawPane5.setShowEdges(true);
        drawPane5.setEdgeSize(4);
        drawPane5.setBackgroundColor("papayawhip");
        drawPane5.setOverflow(Overflow.HIDDEN);
        drawPane5.setCursor(Cursor.AUTO);
        drawPane5.draw();

        ColorPickerItem startColorColorPicker = new ColorPickerItem("startColor","Start Color");
        ColorPickerItem firstStopColorPicker  = new ColorPickerItem("firstStop","First Stop Color");
        ColorPickerItem secondStopColorPicker = new ColorPickerItem("secondStop","Second Stop Color");
        ColorPickerItem endColorColorPicker   = new ColorPickerItem("endColor","End Color");

        startColorColorPicker.setDefaultValue("#ff0000");
        firstStopColorPicker.setDefaultValue("#ffff00");
        secondStopColorPicker.setDefaultValue("#00ff00");
        endColorColorPicker.setDefaultValue("#0000ff");

        ItemChangedHandler radialGradientItemChangedHandler = new ItemChangedHandler() {
            @Override
            public void onItemChanged(ItemChangedEvent event) {
                radialGradientShapesDraw(drawPane5);
            }


        };
        
        radialGradientDynamicForm = new DynamicForm();
        radialGradientDynamicForm.setTop(1850);
        radialGradientDynamicForm.setWidth(270);
        radialGradientDynamicForm.setLeft(475);
        radialGradientDynamicForm.setFields(startColorColorPicker,firstStopColorPicker,secondStopColorPicker,endColorColorPicker);
        radialGradientDynamicForm.addItemChangedHandler(radialGradientItemChangedHandler);
        radialGradientDynamicForm.draw();
        

        SliderItem rSlider = new SliderItem("r");
        rSlider.setDefaultValue(100);
        rSlider.setMinValue(0);
        rSlider.setMaxValue(100);
        rSlider.setHeight(20);
        
        radielGradientSliderDynamicForm = new DynamicForm();
        radielGradientSliderDynamicForm.setTop(1950);
        radielGradientSliderDynamicForm.setWidth(270);
        radielGradientSliderDynamicForm.setLeft(475);
        radielGradientSliderDynamicForm.setFields(rSlider);
        radielGradientSliderDynamicForm.addItemChangedHandler(radialGradientItemChangedHandler);
        radielGradientSliderDynamicForm.draw();

        radialGradientShapesDraw(drawPane5);
        
    }

    private void radialGradientShapesDraw(DrawPane drawPane) {
        drawPane.erase();

        ColorStop colorStop1 = new ColorStop();
        colorStop1.setColor(radialGradientDynamicForm.getValueAsString("startColor"));
        colorStop1.setOffset(0.0f);

        ColorStop colorStop2 = new ColorStop();
        colorStop2.setColor(radialGradientDynamicForm.getValueAsString("firstStop"));
        colorStop2.setOffset(0.33f);

        ColorStop colorStop3 = new ColorStop();
        colorStop3.setColor(radialGradientDynamicForm.getValueAsString("secondStop"));
        colorStop3.setOffset(0.66f);

        ColorStop colorStop4 = new ColorStop();
        colorStop4.setColor(radialGradientDynamicForm.getValueAsString("endColor"));
        colorStop4.setOffset(1.0f);
        
        int r = (Integer) radielGradientSliderDynamicForm.getValue("r");
        
        RadialGradient radialGradient = new RadialGradient();
        radialGradient.setCx("0%");
        radialGradient.setCy("0%");
        radialGradient.setR(r+"%");
        radialGradient.setFx("0%");
        radialGradient.setFy("0%");
        radialGradient.setColorStops(colorStop1,colorStop2,colorStop3,colorStop4);
               
        DrawTriangle drawTriangle = new DrawTriangle();
        drawTriangle.setDrawPane(drawPane);
        drawTriangle.setFillGradient(radialGradient);
        drawTriangle.setPoints(new Point(100,50),new Point(150,150),new Point(50,150));
        drawTriangle.draw();
        
        DrawCurve drawCurve = new DrawCurve();
        drawCurve.setDrawPane(drawPane);
        drawCurve.setFillGradient(radialGradient);
        drawCurve.setStartPoint(new Point(200,50));
        drawCurve.setEndPoint(new Point(340,150));
        drawCurve.setControlPoint1(new Point(270,0));
        drawCurve.setControlPoint2(new Point(270,200));
        drawCurve.draw();
        
        DrawOval drawOval = new DrawOval();
        drawOval.setDrawPane(drawPane);
        drawOval.setFillGradient(radialGradient);
        drawOval.setLeft(50);
        drawOval.setTop(200);
        drawOval.setWidth(100);
        drawOval.setHeight(150);
        drawOval.draw();
        
        DrawRect drawRect = new DrawRect();
        drawRect.setDrawPane(drawPane);
        drawRect.setFillGradient(radialGradient);
        drawRect.setLeft(200);
        drawRect.setTop(225);
        drawRect.setWidth(150);
        drawRect.setHeight(100);
        drawRect.draw();
        
    }
}
