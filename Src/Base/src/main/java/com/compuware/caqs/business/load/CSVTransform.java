/*
 * CSVTransform.java
 *
 * Created on August 10, 2004, 2:10 PM
 */

package com.compuware.caqs.business.load;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class CSVTransform {
    Object[][] mCSVData;
    
    /** Creates a new instance of CSVTransform */
    public CSVTransform() {        
    }
    
    public void setData(Object[][] data ){
        this.mCSVData = data;
    }
    
    private List<Object> getSingleElementsFromArrayOfArray(int identifierColumn,int startLineInSourceFile){
        List<Object> out = new ArrayList<Object>();
        int startAtLine = startLineInSourceFile;
        for (int line = startAtLine ; line < this.mCSVData.length ; line++){
            Object[] currentLine = this.mCSVData[line];
            if (currentLine != null){
                if(currentLine.length >= identifierColumn){
                    Object id = currentLine[identifierColumn];
                    if(out.contains(id)){
                        System.out.println("already have this identifier : " + id);
                    }
                    else{
                        System.out.println("adding "+id);
                        out.add(id);
                    }
                }
                else{
                    System.out.println("can't find identifier column");
                }
            }
            else{
                System.out.println("line is null");
            }
        }
        return out;
    }

    /**
     * Consolide les données dans le format standard.
     * @param eltIdentifierColumn
     * @param metIdentifierColumn
     * @param startLineInSourceFile
     * @return les données consolidées.
     */
    public Object[][] getTransformedData(int eltIdentifierColumn, int metIdentifierColumn,int startLineInSourceFile){
        //startLineInSourceFile 0 if no header / 1 if header.
        Object[][] out = null;
        
        int elementIdentifierColumn = eltIdentifierColumn;
        int metricIdentifierColumn = metIdentifierColumn;
        
        //creates Element identifiers collection
        List eltIdentifiers = this.getSingleElementsFromArrayOfArray(elementIdentifierColumn,startLineInSourceFile);
        //creates Metrics identifiers collection
        List metricIdentifiers = this.getSingleElementsFromArrayOfArray(metricIdentifierColumn,startLineInSourceFile);
        
        out = new Object[eltIdentifiers.size()+1][metricIdentifiers.size()+3];
        
        //init first line
        out[0][0]= "eltIdentifier";
        out[0][1]= "eltSrcPath";
        out[0][2]= "eltSrcLine";
        for (int i = 0 ; i < metricIdentifiers.size(); i++){
            out[0][i+3] = metricIdentifiers.get(i);
        }
        //init first column
        for (int i = 0 ; i < eltIdentifiers.size(); i++){
            out[i+1][0] = eltIdentifiers.get(i);
            out[i+1][1] = eltIdentifiers.get(i);
            out[i+1][2] = new Integer(0);
        }
        //init metrics      
        for (int line = 1 ; line < out.length ; line ++){           
            for (int column = 3; column < out[line].length ; column++){
                out[line][column] = new Integer(0);
            }
        }        
        
        for (int line = 1 ; line < this.mCSVData.length ; line++){
            Object[] currentLine = this.mCSVData[line];
            Object metricId = currentLine[metricIdentifierColumn];
            Object eltId = currentLine[elementIdentifierColumn];       
            int metricIndex = metricIdentifiers.indexOf(metricId) + 3;
            int eltIndex = eltIdentifiers.indexOf(eltId) + 1;
            System.out.println(eltIndex + " " + metricIndex);
            Integer value = (Integer) out[eltIndex][metricIndex];
            int intValue = value.intValue() + 1;
            out[eltIndex][metricIndex] = new Integer(intValue);
        }
        return out;
    }
    
    private static  void printData(Object[][] data){
        for (int i = 0 ; i < data.length ; i++){
            Object[] line = data[i];
            for (int j = 0 ; j < line.length ; j++){
                System.out.print(line[j]+"\t");
            }
            System.out.println();
        }
    }
    
    
    
    private static Object[][] createFakeCSV(){
        Object[][] csvData = new Object[11][2];
        csvData[0][0] = "eltName";
        csvData[0][1] = "eltError";
        csvData[1][0] = "a";
        csvData[1][1] = "r1";
        csvData[2][0] = "a";
        csvData[2][1] = "r1";
        csvData[3][0] = "a";
        csvData[3][1] = "r3";
        csvData[4][0] = "b";
        csvData[4][1] = "r1";
        csvData[5][0] = "c";
        csvData[5][1] = "r1";
        csvData[6][0] = "c";
        csvData[6][1] = "r3";
        csvData[7][0] = "d";
        csvData[7][1] = "r2";
        csvData[8][0] = "e";
        csvData[8][1] = "r1";
        csvData[9][0] = "f";        
        csvData[9][1] = "r3";
        csvData[10][0] = "f";
        csvData[10][1] = "r4";
        return csvData;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CSVTransform csvTrans = new CSVTransform();
        csvTrans.setData(CSVTransform.createFakeCSV());
        printData(csvTrans.getTransformedData(0,1,1));
    }
    
}
