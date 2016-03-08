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

import static java.lang.Math.abs;
import static java.lang.Math.pow;
/*
Known Values
tile width = 256 pixels
World has 360 degrees, at each progressive zoom level, there are 
*/

public class MapMath {
    //Gives the degree width of every
    public double zoomToDegreesPerTile(int zoom){
        return 360/pow(2,zoom);
    }
    
    //Amount of tiles wide an image is
    public double tilesOnMap(int width){
        return width/256;
    }
    
    //Depreciated, only accurate at equator. 
    public double mapsizeToDegrees(int zoom, int width){
        double degreePerTile = zoomToDegreesPerTile(zoom);
        double tilesWide = tilesOnMap(width);
        return degreePerTile*tilesWide;
    }
    
    public double corrdinateToDegreesWidth(double topCord, double bottomCord){
        return abs(topCord-bottomCord);
    }
    
    //Normalize value (that is between VMax and Vmin) between 0 and a max. 
    //newvalue= (max'-min')/(max-min)*(value-max)+max'

    //Normalizes a value to between max and 0
    public double normalizerZeroToMax(double value, int max, double vMax, double vMin){
        return (((max-0)*(value-vMin))/(vMax-vMin));
    }
    
    //Normalizes a value to between max and min
    public double normalizerMaxToMin(double value, double max, double min, double vMax, double vMin){
        return ((min)+(((value-vMin)*(max-min))/(vMax-vMin)));
        
    }
        
    //Takes the degrees width of overlay and converts to pixles
    public double degreesToPixels(int zoom, double topCord, double bottomCord){
        double degreesPerTile = zoomToDegreesPerTile(zoom);
        double degreeWidth = corrdinateToDegreesWidth(topCord,bottomCord);
        double tiles = degreeWidth/degreesPerTile;
        return (tiles*256);
 
    }
    
    //  newvalue= (max'-min')/(max-min)*(value-max)+max'
    public double normalizeDouble(double maxValue, double minValue, double value){
        return ((maxValue-minValue)/(((maxValue-minValue)*(value-maxValue))+maxValue));
    }
    
    
    //Adapted from formula on http://www.movable-type.co.uk/scripts/latlong.html
    //Uses haversine formula to find distance in Meters between two points on 
    //earth with some degree of accuracy varriying between longitudes
    public double getDistanceFromLatLonInM(double lat1,double lon1, double lat2,double lon2) {
        double R = 6372.7982; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double  dLon = deg2rad(lon2-lon1); 
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2); 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        double d = R * c; // Distance in km
        return d*1000;
    }
    
    //Converts degrees to rads
    public double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
    
    //gives how many meteres per pixel there are at differnt latitudes and 
    //zoom levels
    public double metersPerPixel(int zoom, double latitude){
        double C = (2*Math.PI*(6372.7982))*1000; // Circumfrence of the earth in m
        double cos = Math.cos(deg2rad(latitude));
        return (C*cos)/(Math.pow(2, zoom+8));
        
    }
}
