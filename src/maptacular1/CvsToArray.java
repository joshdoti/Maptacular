/*
 *
 * Maptacular
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Joshua Dotinga
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package maptacular1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Josh
 */
public class CvsToArray{
    int latIndex; //Index of lattitude in given cvs
    int longIndex; //Index of longitude in given cvs
    int valueIndex; //Index of value in given cvs
    int scannerLength;
    
    private Scanner cvsScanner;
    File opener;
    private double topLat = -360;
    private double botLat = 360;
    private double leftLong = -360;
    private double rightLong = 360;
    private double maxValue;
    private double minValue;
    private double average;
    private double counter;
    private double stdDeviation;
    private int zoom;
    int dataPixelWidth;
    int dataPixelHeight;

    String[] headers;
    String fileName;
    String headerLin;
    MapMath mapMath;
    double[][] dataArray;
    

    CvsToArray(int zoom, String fileNamer) throws FileNotFoundException, IOException {
        this.fileName = fileNamer;
        this.zoom = zoom;
        mapMath = new MapMath();
        
        //Scanner to read cvs file
        opener = new File(fileName);
        setScanner();
        //print();

    }
    
    public final void setScanner() throws FileNotFoundException{
        if(cvsScanner != null){
                    getCvsScanner().close();
        }
        cvsScanner = new Scanner(opener);      
        headers = getCvsScanner().nextLine().split(",");
        //System.out.println(Arrays.toString(headers));   
    }
    
    protected Void doWork() throws Exception {
        getMaxMin();
        dataToArrayMode1();
        return null;
    }
   
    
    public void getMaxMin() throws IOException, InterruptedException{
            
        double workingLat;
        double workingLong;
        double workingValue;
        double workingAverage = 0;
        double valuesSQRD = 0;
        
        String line;
        String[] splitLine;
        setScanner();
        line = getCvsScanner().nextLine();
        splitLine = line.split(",");
        
        topLat = Double.parseDouble(splitLine[latIndex]);
        botLat = Double.parseDouble(splitLine[latIndex]);
        rightLong = Double.parseDouble(splitLine[longIndex]);
        leftLong = Double.parseDouble(splitLine[longIndex]);
        maxValue = Double.parseDouble(splitLine[valueIndex].replaceAll("[^0-9.]", ""));
        //maxValue = Double.parseDouble(splitLine[valueIndex]);
        minValue = Double.parseDouble(splitLine[valueIndex].replaceAll("[^0-9.]", ""));
        //minValue = Double.parseDouble(splitLine[valueIndex]);
        
        while(getCvsScanner().hasNextLine()){
            line = getCvsScanner().nextLine();
            //System.out.println(line);
            splitLine = line.split(",");
            //if(splitLine[latIndex] == null || splitLine[longIndex] == null || splitLine[valueIndex] == null){
            //    continue;
            //}

            try {
                workingLat = Double.parseDouble(splitLine[latIndex]);
                workingLong = Double.parseDouble(splitLine[longIndex]);
                workingValue = Double.parseDouble(splitLine[valueIndex].replaceAll("[^0-9.]", ""));
            } catch (Exception e ) {
                continue;
            }

            if(workingLat > getTopLat()){
                topLat = workingLat;
                continue;
            }
            if(workingLat < getBotLat()){
                botLat = workingLat;
                continue;
            }
            if(workingLong > getLeftLong()){
                leftLong = workingLong;
                continue;
            }
            if(workingLong < getRightLong()){
                rightLong = workingLong;
                continue;
            }
            if(workingValue > maxValue){
                maxValue = workingValue;
            }
            if(workingValue < minValue){
                minValue = workingValue;
            }
            workingAverage = workingAverage+workingValue;
            valuesSQRD = valuesSQRD +  (workingValue * workingValue);
            counter++;
            
            
        }
        //Work out statistics
        stdDeviation = Math.sqrt((valuesSQRD-((workingAverage * workingAverage)/(getCounter())))/getCounter());
        average = workingAverage/getCounter();
        
        //Test Printing
        //System.out.println("Average" + getAverage() );
        //System.out.println("STD" + getStdDeviation());
        //System.out.println(getTopLat());
        //System.out.println(getBotLat());
        //System.out.println(getLeftLong());
        //System.out.println(getRightLong());
        setScanner();

    }
    
    //Reads data into an normalized array
    //i.e, stores average values of noramzlied lat/long cordiantes
    //Can reduce data to set amount of pixels
    public void dataToArrayMode1() throws FileNotFoundException{
        String line;
        String[] splitLine;
        double workingLat;
        int intLat;
        double workingLong;
        int intLong;
        double workingValue;
        int workingIntValue;
        setPixelWidths();
        //System.out.println("working");
        
        setScanner();
        while(cvsScanner.hasNextLine()){
            //Reads
            line = cvsScanner.nextLine();
            splitLine = line.split(",");
            try {
                workingLat = Double.parseDouble(splitLine[latIndex]);
                workingLong = Double.parseDouble(splitLine[longIndex]);
                workingValue = Double.parseDouble(splitLine[valueIndex].replaceAll("[^0-9.]", ""));
            } catch (Exception e ) {
                continue;
            }
            
            intLat = (int) mapMath.normalizerZeroToMax(workingLat, dataPixelHeight, topLat, botLat);
            intLong = (int) mapMath.normalizerZeroToMax(workingLong, dataPixelWidth, rightLong, leftLong);
            
            //System.out.println(intLat);
            //System.out.println(intLong);
            if(intLong > dataPixelWidth || intLat > dataPixelHeight){
                continue;
            }
            if(intLong < 0 || intLat < 0){
                continue;
            }
            if(dataArray[dataPixelWidth-intLong][dataPixelHeight-intLat] == 0){
                dataArray[dataPixelWidth-intLong][dataPixelHeight-intLat] = workingValue;
            }
            else{
                dataArray[dataPixelWidth-intLong][dataPixelHeight-intLat] = ((dataArray[dataPixelWidth-intLong][dataPixelHeight-intLat]) + workingValue)/2;      
            } 
        }
        setScanner();
        //print();
    }
    
    private void setPixelWidths(){
        double meterHeight = mapMath.getDistanceFromLatLonInM(topLat, rightLong, botLat, rightLong);
        double meterWidth = mapMath.getDistanceFromLatLonInM(topLat, rightLong, topLat, leftLong);
        double pixelsPerMeter = mapMath.metersPerPixel(zoom, (topLat+botLat)/2);
        
        dataPixelWidth = (int) ((int) meterWidth/pixelsPerMeter);
        dataPixelHeight = (int) ((int) meterHeight/pixelsPerMeter);
        //System.out.println(dataPixelWidth);
        //System.out.println(dataPixelHeight);
        dataArray = new double[dataPixelWidth+1][dataPixelHeight+1];
    }
    
    public String[] getHeaders(){
        return headers;
    }
        
    
    public void setlatIndex(int latIndex){
        this.latIndex = latIndex;
    }
    public void setlongIndex(int longIndex){
        this.longIndex = longIndex;
    }
    public void setvalueIndex(int valueIndex){
        this.valueIndex = valueIndex;
    }
    
    public int getvalueIndex(){
        return this.valueIndex;
    }
    public int getlongIndex(){
        return this.longIndex;
    }
    public int getlatIndex(){
        return this.latIndex;
    }
    public double getMaxValue(){
        return this.maxValue;
    }
    public double getMinValue(){
        return this.minValue;
    }
    
    /**
     * @return the cvsScanner
     */
    public Scanner getCvsScanner() {
        return cvsScanner;
    }

    /**
     * @return the topLat
     */
    public double getTopLat() {
        return topLat;
    }

    /**
     * @return the botLat
     */
    public double getBotLat() {
        return botLat;
    }

    /**
     * @return the leftLong
     */
    public double getLeftLong() {
        return leftLong;
    }

    /**
     * @return the rightLong
     */
    public double getRightLong() {
        return rightLong;
    }
    
    public int getArrayHeight() {
        return dataPixelHeight;
    }
    
    public int getArrayWidth() {
        return dataPixelWidth;
    }
    
    public double[][] getDataArray(){
        return dataArray;
    }
    
    //For testing purpose only, (will print a huge array of all data)
    private void print(){
        for(int i = 0;  i < dataPixelWidth; i++){
            System.out.println(Arrays.toString(dataArray[i]));
        }
    }

    /**
     * @return the average
     */
    public double getAverage() {
        return average;
    }

    /**
     * @return the counter
     */
    public double getCounter() {
        return counter;
    }

    /**
     * @return the stdDeviation
     */
    public double getStdDeviation() {
        return stdDeviation;
    }

    void setZoom(int zoom) {
        this.zoom = zoom;
    }
    
}
