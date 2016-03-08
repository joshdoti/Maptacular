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

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Josh
 * Turns either an array of data or a stream of data (with lat longs) 
 * into a normalizedDataArray
 */
public class BlurArray{
    Scanner scanner;
    CvsToArray cvsData;
    int dataPixelWidth;
    int dataPixelHeight;
    int blur;
    MapMath math;
    double[][] dataArray;
    
    BlurArray(int blur,int dataPixelWidth, int dataPixelHeight, double[][] cvsdataArray){
        this.math = new MapMath();
        
        //Sets the required values
        this.dataPixelWidth = dataPixelWidth;
        this.dataPixelHeight = dataPixelHeight;

        
        this.dataArray = cvsdataArray;
        this.blur = blur;
        blurData();
        
    }
      
    //Implemets a sort of box blur. Averages pixels to same 
    //in an area as wide as distance*2 (distance on both sides of pixel
    private void blurData(){
        double runningTotal;
        double average;
        int zeros;
        double calc;
        double[][] workingArray = new double [dataPixelWidth+1][dataPixelHeight+1];
        
        //Goes through each row
        for(int z = (blur); z < dataPixelHeight-blur; z++){
            //Goes through each collum in row
            for(int i = (blur);  i < dataPixelWidth-blur; i++){
                //Z is ROW up and down
                //i is COLLUM left right
                runningTotal = 0;
                zeros = 0;
                
                //Loops through distance on edge of "pixel" and totals
                for(int j = (-blur);  j < (blur); j++){
                    for(int k = (-blur);  k < (blur); k++){
                        //j is row up down
                        //k is collum left right
                        if(dataArray[i+j][z+k] == 0 || dataArray[i+j][z+k] == -1){zeros++;}
                        runningTotal = runningTotal + (dataArray[i+j][z+k]);}
                }//end total pixels
                //Determine the divisor of average
                calc = ((blur*2)*(blur*2))-zeros;
                //System.out.println(calc); //TESTING
                
                //Calculates average of blur zone,with check to stop divide by 0
                if(calc == 0){calc++;}
                average = runningTotal/calc;
                if(average <= 1){average = 0;}
                //System.out.println(runningTotal); //TESTING
                //System.out.println(zeros);        //TESTING
                //System.out.println(average + ",,"); //TESTING

                //Loops same as above and sets values to average 
                //for(int r = -(distance);  r < (distance); r++){
                    //for(int h = -(distance);  h < (distance); h++){
                workingArray[i][z] = average;
                //}//End set average loop
                   
            }//end collumn loop
        }//End row loop
        this.dataArray = workingArray;
    }//End arrayBlur
    
    
    public double[][] getArray(){
        return dataArray;
    }
    
    
    //For testing purpose only, (will print a huge array of all data)
    private void print(){
        for(int i = 0;  i < dataPixelWidth; i++){
            System.out.println(Arrays.toString(dataArray[i]));
        }
    }
}
