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

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 *
 * @author Josh
 */
public class Maptacular1 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        
        SwingUtilities.invokeLater(() -> {
                MainGui gui = new MainGui();
                //SaveFile1 save = new SaveFile1();
                MainController controller = new MainController(gui);
                controller.showGUI();
        });
    }
    
        
    
    
    
    
    
    
    
    
    
    
    
    /* IGNORE ALL THIS, IT IS ONLY FOR TESTING
        
        //double lat1,double lon1, double lat2,double lon2
        //System.out.println(mathing.getDistanceFromLatLonInM(53.7151493212817, -113.278434908578, 53.396060580234, -113.278434908578));
        ///System.out.println(mathing.getDistanceFromLatLonInM(53.7151493212817, -113.278434908578, 53.7151493212817, -113.712997296405));
        //System.out.println(mathing.metersPerPixel(12, 53.396060580234));
        int zoom = 13;
        int blur = 10;
        
        LoadColourScale scale = new LoadColourScale("scale2.gif");
        CvsToArray reader = new CvsToArray(zoom, "\\C:\\Users\\Josh\\Documents\\NetBeansProjects\\Maptacular1\\file.csv\\");
        System.out.println(Arrays.toString(reader.getHeaders()));

        reader.setlatIndex(8);
        reader.setlongIndex(9);
        reader.setvalueIndex(4);
        reader.setZoom(zoom);
        
        /*CvsToArray reader = new CvsToArray(zoom, "\\C:\\Users\\Josh\\Downloads\\assessment2015.csv\\");
        System.out.println(Arrays.toString(reader.getHeaders()));
        reader.setlatIndex(13);
        reader.setlongIndex(14);
        reader.setvalueIndex(12);
        
        reader.getMaxMin();
        reader.dataToArrayMode1();
    
    
        GetMap url = new GetMap(reader.getTopLat(), reader.getRightLong(), reader.getBotLat(), reader.getLeftLong(), zoom);
        System.out.println(url.urlString());
        url.saveImage("FINAL3.jpg");

        
        
        BlurArray blurredArray = new BlurArray(blur,reader.getArrayWidth(), 
                reader.getArrayHeight(), reader.getDataArray());
        
        ArrayToColourArray scaledArray = new ArrayToColourArray(reader.getArrayHeight(), reader.getArrayWidth(), 
              blurredArray.getArray(), reader.getStdDeviation(), reader.getAverage(), scale.getColorHEXScale(), .25, 10);
       
        ReColorImage reColor = new ReColorImage(url.getImage(), scaledArray.getArray());
        reColor.overlayScale(scale.getImage());*/
    //}

}
