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

/**
 *
 * @author Josh
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public final class GetMap{
    //url points as form (leftLong,topLat)(rightlong,topLat)(leftlong, bottomLat)(rightlong, bottomLat)
    private String imageURL;
    private BufferedImage mapImage;
    private double leftLong;
    private double rightLong;
    private double topLat;
    private double bottomLat;
    private double centerLong;
    private double centerLat;
    private int zoom;
    
    private String APIKEY = "pk.eyJ1Ijoiam9zaGRvdGkiLCJhIjoiY2lnanN4aWF4MDA3NXVha3JkcDE4NGJpYiJ9.gMb5i5FTphQPYmZ2YNvPbg";
    private String URLPREFIX = "https://api.mapbox.com/v4/";
    private String MAPSTYLE = "joshdoti.319500a7";
    private int width = 1280;
    private int height = 1280;
    
    public GetMap(String imageURL) throws IOException{
        this.imageURL = imageURL;
        mapImage = mapFromUrl(imageURL);
    }
    
    //Sets parameters in order to craft url
    public GetMap(double topLat, double rightLong, double bottomLat, double leftLong, int zoom) throws IOException{
        this.topLat = topLat;
        this.bottomLat = bottomLat;
        this.rightLong = rightLong;
        this.leftLong = leftLong;
        this.centerLat = (topLat + bottomLat)/2;
        this.centerLong = (leftLong+rightLong)/2;
        this.zoom = zoom;
            
    }
    
    //Called to do work when needed. 
    protected Void doWork() throws Exception {
        craftURL();
        mapImage = mapFromUrl(imageURL);  
        return null;
    }
    
    //Crafts the url of Map from api
    //uses stringbuilder
    private void craftURL(){
        String workingURL = URLPREFIX;
        StringBuilder URL = new StringBuilder(workingURL);
        URL.append(MAPSTYLE).append("/");
        URL.append(getCenterLong()).append(",").append(getCenterLat()).append(",");
        URL.append(getZoom()).append("/");
        URL.append(width).append("x").append(height).append(".jpg");
        URL.append("?access_token=").append(APIKEY);
        //System.out.println(URL.toString());
        imageURL = URL.toString();
    }

    //turn a url into a buffered image. 
    private BufferedImage mapFromUrl(String imageUrl) throws MalformedURLException, IOException{
        URL url = new URL(imageUrl);
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        image = ImageIO.read(url);
        return image;
    }
    
    public BufferedImage getImage(){
        return mapImage;
    }
    
    public void saveImage(String location){
        File f = new File(location);
        try {
            ImageIO.write(mapImage, "JPG", f);
        } catch (IOException ex) {
            Logger.getLogger(GetMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String urlString(){
        return imageURL;
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

    /**
     * @return the topLat
     */
    public double getTopLat() {
        return topLat;
    }

    /**
     * @return the bottomLat
     */
    public double getBottomLat() {
        return bottomLat;
    }

    /**
     * @return the centerLong
     */
    public double getCenterLong() {
        return centerLong;
    }

    /**
     * @return the centerLat
     */
    public double getCenterLat() {
        return centerLat;
    }

    /**
     * @return the zoom
     */
    public int getZoom() {
        return zoom;
    }

}

/*
https://api.mapbox.com/v4/mapbox.streets/pin-l-park+482(-113.6126,53.3960),pin-l-park+482(-113.3699,53.7151),pin-l-park+482(-113.7133,53.5606),pin-l-park+482(-113.2784,53.6687)/-113.4958763,53.55560495,10/1280x1280.png?access_token=pk.eyJ1Ijoiam9zaGRvdGkiLCJhIjoiY2lnanN4aWF4MDA3NXVha3JkcDE4NGJpYiJ9.gMb5i5FTphQPYmZ2YNvPbg}

hl  pin-l-park+482(53.39606,-113.612654),pin-l-park+482(53.71514,-113.369977),pin-l-park+482(53.56065,-113.713317),pin-l-park+482(53.6687,-113.278434)
ll  pin-l-park+482(53.71514,-113.369977)
llo pin-l-park+482(53.56065,-113.713317)
hlo pin-l-park+482(53.66879,-113.278434)
*/