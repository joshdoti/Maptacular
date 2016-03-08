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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

/**
 *
 * @author Josh
 * 
 * Colors images by mixing a colorArray with a picture. 
 * colorArray is mapped to picture. 
 * 
 */
public class ReColorImage{
    double[][] colorArray;
    BufferedImage mapPicture;
    int offsetFromTop;
    int offsetFromLeft;
    
    ReColorImage(BufferedImage mapPicture, double[][] colorArray){
        this.colorArray = new double[colorArray.length][colorArray[1].length];
        this.colorArray = colorArray;
        this.mapPicture = mapPicture;
        this.offsetFromLeft = ((colorArray.length-mapPicture.getWidth())/2);
        this.offsetFromTop = ((colorArray[1].length-mapPicture.getHeight())/2);
        
        /*//For Testing
        System.out.println(mapPicture.getHeight());
        System.out.println(mapPicture.getWidth());
        
        System.out.println("offsetfromleft" + offsetFromLeft);
        System.out.println("pffsetfromtop" + offsetFromTop);
        
        System.out.println("Width of color" + this.colorArray.length);
        System.out.println("Height of color" + this.colorArray[1].length);
        */
        mapFromXtoY();
        saveImage("blah");

        
    }
    
    private void mapFromXtoY(){
        for (int row = 0; row < mapPicture.getHeight(); row++) {
            for (int col = 0; col < mapPicture.getWidth(); col++) {
                //Get pixel data from map
                int pixel = mapPicture.getRGB(row,col);
                //Convert pixel data into red green and blue
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff; 
                
                //Set anything close to white to white
                if((Math.abs(red-255) <=100) && (Math.abs(green-255) <=15)){
                    int rgb= new Color(255,255,255).getRGB();
                    mapPicture.setRGB(row,col, rgb);
                }
                
                //set anything close to black to color from color array
                //Use the fact that black to grey Red=Green=Blue ish
                if(!(Math.abs(red-255) <= 100) && !(Math.abs(green-255) <=15) && (Math.abs(red-green) <= 15) && (Math.abs(green-blue) <= 4)){
                    //run when in bounds od data
                    if((row+offsetFromLeft) >= 0 && (col+offsetFromTop) >= 0){
                    if((row+offsetFromLeft) < colorArray.length && (col+offsetFromTop) < colorArray[1].length){
                            int fromColor = (int) colorArray[row+offsetFromLeft][col+offsetFromTop];
                            mapPicture.setRGB(row,col, fromColor);
                        }
                    }
                }
            }//end col
        }//end row
    }
    
    public BufferedImage getImage(){
        return mapPicture;
    }
    //Start of overalaying a scale image onto picture
    public void overlayScale(BufferedImage scale){
        for(int i = 0; i < scale.getWidth(); i++){
            for(int j = 0; j < 15; j++){
                mapPicture.setRGB(j+20, j+20, scale.getRGB(i, 0));
            }
        }
    }
    
    //will save the final image in working directory as final.jpg
    //Can also take location
    private void saveImage(String location){
        File f = new File("final.jpg");
        try {
            ImageIO.write(mapPicture, "JPG", f);
        } catch (IOException ex) {
            Logger.getLogger(GetMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
