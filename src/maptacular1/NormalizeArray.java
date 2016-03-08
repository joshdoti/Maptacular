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

/**
 *
 * @author Josh
 * 
 * 
 * Depreciated, works but has been replaced by standard deviation math. 
 * Would scale images to a normal value using logs. 
 * 
 * 
 * 
 */
public class NormalizeArray {
    MapMath math;
    int arrayHeight;        //"height" of array [width][height]
    int arrayWidth;       //"width" of array
    double highestValue; //highest number found in array
    double lowestValue; //lowest number found in array
    double betweenHigh; //Highest normalization value
    double betweenLow;  //Lowest normalization value
    double[][] array;  //[width][height]
    
    
    
    NormalizeArray(int arrayHeight, int arrayWidth, double highestValue, double lowestValue, double betweenHigh, double betweenLow, double[][] array){
        math  = new MapMath();
        this.arrayHeight = arrayHeight;
        this.arrayWidth = arrayWidth;
        
        //Faster to pass in highest and lowest instead of scanning through array
        this.highestValue = highestValue;
        this.lowestValue = lowestValue;
        
        this.betweenHigh = betweenHigh;
        this.betweenLow = betweenLow;
        
        this.array = new double[arrayWidth+1][arrayHeight+1];
        this.array = array;
        normalizeArray();
        //System.out.println(highestValue);
        //SSystem.out.println(lowestValue);
        
        //print();
    }
    
    NormalizeArray(int arrayHeight, int arrayWidth, double inHighestValue, double inLowestValue, double betweenHigh, double betweenLow, double[][] array, boolean log){
        math  = new MapMath();
        //System.out.println("Normalize array");
        this.arrayHeight = arrayHeight;
        this.arrayWidth = arrayWidth;
        
        //Faster to pass in highest and lowest instead of scanning through array
        this.highestValue = Math.log(inHighestValue);
        if(lowestValue == 0){
            this.lowestValue = 0;
        }else{
            this.lowestValue = Math.log(inLowestValue);
        }
        
        this.betweenHigh = betweenHigh;
        this.betweenLow = betweenLow;
        
        this.array = new double[arrayWidth+1][arrayHeight+1];
        this.array = array;
        
        logNormalizeArray();
        System.out.println(highestValue);
        System.out.println(lowestValue);
        
        print();
    }

    //normalizerMaxToMin(double value, int max, int min, double vMax, double vMin){
    private void normalizeArray() {
        for(int z = 0; z < arrayHeight; z++){
            //Goes through each collum in row
            for(int i = 0;  i < arrayWidth; i++){
                array[i][z] = math.normalizerMaxToMin(array[i][z], betweenHigh,
                        betweenLow, highestValue, lowestValue);
            }
        }
    }
    
    
    //Normalize the array using averages and standard deviation
    //
    private void logNormalizeArray(){
        for(int z = 0; z < arrayHeight; z++){
            //Goes through each collum in row
            for(int i = 0;  i < arrayWidth; i++){
                array[i][z] = math.normalizerMaxToMin(Math.log(array[i][z]), betweenHigh,
                        betweenLow, highestValue, lowestValue);
            }
        }
    }

    //FOR TESTING ONLY...WILL PRINT LARGE ARRAY
    private void print() {
        for(int i = 0;  i < arrayWidth; i++){
            System.out.println(Arrays.toString(array[i]));
        }
    }
}
