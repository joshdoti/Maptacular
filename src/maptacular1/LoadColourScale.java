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
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 *
 * @author Josh
 */
public class LoadColourScale {
    private String file;
    private BufferedImage image;
    private Color[] colours;
    private int WIDTHOFIMAGE;
    private Color[] colorArray;
    private int[] intArray;
    
    LoadColourScale(String imageLocation){
        try {
            image = ImageIO.read(getClass().getResource(imageLocation));
        }catch (IOException e) {
        }
        WIDTHOFIMAGE = image.getWidth();
        colorArray = new Color[WIDTHOFIMAGE];
        intArray = new int[WIDTHOFIMAGE];
        
        for(int i = 0; i < WIDTHOFIMAGE; i++){
            colorArray[i] = new Color(image.getRGB(i, 0));
            intArray[i] = image.getRGB(i, 0);
        }
        
        //System.out.println(Arrays.toString(intArray));
        //System.out.println(Arrays.toString(colorArray));
        //System.out.println(intArray.length);
        
       
    }
    public BufferedImage getImage(){
        return image;
    }
    public Color getColorRGB(int index){  
        return colorArray[index];
    }
    
    public int scaleWidth(){
        return WIDTHOFIMAGE;
    }
    
    public int getColorHEX(int index){
        return intArray[index];
    }
    public int[] getColorHEXScale(){
        return intArray;
    }
    
        
}
