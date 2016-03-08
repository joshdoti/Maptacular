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

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Josh
 * Maps array values to scale colors. 
 */
public class ArrayToColourArray{
    MapMath math;
    int arrayHeight;        //"height" of array [width][height]
    int arrayWidth;       //"width" of array
    double[][] array;  //[width][height]
    double[] colour;
    int[] scale;
    double standardDeviation;
    double deviationsPerScale;
    double standardDeviationScaled;
    double average;
    int centerOn;
    Image picture;
    
    ArrayToColourArray(int arrayHeight, int arrayWidth,double[][] array, double standardDeviation, double average, int[] scale, double deviationsPerScale, int centerOn){
        this.arrayHeight = arrayHeight;
        this.arrayWidth = arrayWidth;
        //this.array = new double[arrayWidth+1][arrayHeight+1];
        this.array = array;
        this.scale = scale;
        this.standardDeviation = standardDeviation;
        this.deviationsPerScale = deviationsPerScale;
        this.standardDeviationScaled = standardDeviation*deviationsPerScale;
        this.average = average;
        this.centerOn = centerOn;
        //System.out.println(deviationsPerScale);
        //System.out.println(centerOn);
        convert();
        saveArrayAsImage();
       
    }
    
    /*
    *Returns the array
    */
    public double[][] getArray(){
        return array;
    }
    
    /*
    *Maps array values to scale colors
    *Converts each value to its Z value ish using standard deviation
    *and average values in array. 
    */
    private void convert() {
        int stdDeviations;
        int onScale;
        
        
        for(int z = 0; z < arrayHeight-1; z++){
            //Goes through each collum in row
            for(int i = 0;  i < arrayWidth-1; i++){
                stdDeviations = (int) valueToColor(array[i][z]);
                if(array[i][z] <= 0){
                    int rgb= new Color(0,0,0).getRGB();
                    array[i][z] = rgb;
                }
                else if((stdDeviations+centerOn) >= scale.length){
                    array[i][z] = scale[scale.length-1];
                }
                else if(stdDeviations+centerOn <= 0){
                    int rgb= new Color(0,0,0).getRGB();
                    array[i][z] = rgb;
                }
                else{
                    //System.out.println(stdDeviations+centerOn);
                    //System.out.println(i + "," + z);
                    array[i][z] = scale[stdDeviations+centerOn];
                }
            }
        }
        array[0][0] = scale[scale.length-1];

    }
    
    //Return how many standard deviations aways from average a value is
    private double valueToColor(double value){
        double divides;
        double difference;
        difference = value-average;
        divides = difference / standardDeviationScaled;
        return divides;
        
    }
    
    private Image getColoredImage(){
        return picture;
    }
    
    //Saves color array to image
    //Saves in working directory as color.jpg
    private void saveArrayAsImage(){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(arrayWidth, arrayHeight, 
        BufferedImage.TYPE_INT_RGB);

        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < arrayWidth; x++) {
            for (int y = 0; y < arrayHeight; y++) {
                bufferedImage.setRGB(x, y, (int) array[x][y]);
            }
        }
        
        picture = bufferedImage;
        File f = new File("color.jpg");
        try {
            ImageIO.write(bufferedImage, "JPG", f);
        } catch (IOException ex) {
            Logger.getLogger(GetMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //FOR TESTING ONLY...WILL PRINT LARGE ARRAY
    private void print() {
        for(int i = 0;  i < arrayWidth; i++){
            System.out.println(Arrays.toString(array[i]));
        }
    }
    
}
