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

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Josh
 * 
 * 
 * See worker class below for main calls and logic/order of operations
 * 
 * 
 */
public class MainController {
    private DefaultComboBoxModel valueModel;
    private DefaultComboBoxModel latModel;
    private DefaultComboBoxModel longModel;
    private BufferedImage picture;
    
    private JComboBox valueBox;
    private JComboBox latBox;
    private JComboBox longBox;
    private final MainGui gui;
    private LoadColourScale scale;
    private CvsToArray reader;
    private GetMap map;
    
    //private BlurArray blurredArray;
    //private ArrayToColourArray scaledArray;
    private ReColorImage reColor;
    private Image finalMap;
    
    
    MainController(MainGui gui) {
        this.valueBox = gui.valueComboBox();
        this.latBox = gui.latComboBox();
        this.longBox = gui.longComboBox();
        this.gui = gui;
        initComboBoxModels();
        //Set Button listeners
        gui.makeMapButtonListener(new makeMapListener());
        gui.loadCVSButtonListener(new openFileListener());
        gui.saveButtonListener(new saveFileListener());
        //Set slider listeners
        gui.blurSliderListener(new blurSliderListener());
        gui.zoomSliderListener(new zoomSliderListener());
        gui.widthSliderListener(new widthSliderListener());
        gui.deviationsSliderListener(new deviationsSliderListener());
        
        scale = new LoadColourScale("/images/scale2.gif");
        
    }

    public void showGUI(){
        gui.setVisible(true);
    }
    
    private void initComboBoxModels(){
        valueModel = new DefaultComboBoxModel();
        latModel = new DefaultComboBoxModel();
        longModel = new DefaultComboBoxModel();
        valueBox.setModel(valueModel);
        latBox.setModel(latModel);
        longBox.setModel(longModel);
    }
    
    private class makeMapListener implements ActionListener {
                
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Worker doWork = new Worker();
                doWork.execute();
                
            } catch(Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }   
    }
    private class saveFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            SaveFile1 save = new SaveFile1(picture);
            
            //File f = openFile.getSelectedFile();
            
        }
    }

    private class widthSliderListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent ce) {
            gui.setWidthBox(gui.getWidthSlider());
        }
    }

    private class deviationsSliderListener implements ChangeListener{

         @Override
        public void stateChanged(ChangeEvent ce) {
            gui.setDeviatonsBox(gui.getDeviationsSlider());
        }
    }

    private class zoomSliderListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent ce) {
            gui.setZoomBox(gui.getZoomSlider());
        }
    }

    private class blurSliderListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent ce) {
            gui.setBlurBox(gui.getBlurSlider());
        }
    }
    
    private class openFileListener implements ActionListener {
                
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                reader = new CvsToArray(12, gui.getFileLocation());
                latModel.removeAllElements();
                longModel.removeAllElements();
                valueModel.removeAllElements();
                for(String header : reader.getHeaders()) {
                    latModel.addElement(header);
                    longModel.addElement(header);
                    valueModel.addElement(header);
                }
                enableInterface();
                gui.setFileError("File Opened");
                
            } catch (IOException ex) {
                //Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                gui.setFileError("File failed to open or does not exists");
                
            }

        }
        
        public void enableInterface(){
            valueBox.setEnabled(true);
            latBox.setEnabled(true);
            longBox.setEnabled(true);
            gui.enableWidthSlider(true);
            gui.enableDeviationsSlider(true);
            gui.enableBlurSlider(true);
            gui.enableZoomSlider(true);
            gui.setBlurSlider(5);
            gui.setZoomSlider(12);
            gui.setDeviationsSlider(10);
            gui.setWidthSlider(25);
            gui.enableMapButton(true);
        }
    }
    
    
    //Gets called to create map
    private class Worker extends SwingWorker<Image, Image>{      
        Image picture2;
        @Override
        protected Image doInBackground() throws Exception {
        try{
            //Reads data from CVS given
            gui.setProgress(10);
            reader.setlatIndex(latBox.getSelectedIndex());
            reader.setlongIndex(longBox.getSelectedIndex());
            reader.setvalueIndex(valueBox.getSelectedIndex());
            reader.setZoom(gui.getZoomSlider());
            reader.doWork();
            
            gui.setProgress(20);
            //Gets map using MAPBOX API calls and lat long from reader
            map = new GetMap(reader.getTopLat(), reader.getRightLong(), reader.getBotLat(), reader.getLeftLong(), gui.getZoomSlider());
            map.doWork();
            gui.setProgress(35);
            //System.out.println(map.urlString());

            //Blurs the data using given blur value
            BlurArray blurredArray = new BlurArray(gui.getBlurSlider(), reader.getArrayWidth(), reader.getArrayHeight(), reader.getDataArray());
            gui.setProgress(60);
            
            //Creates a colorArray(sort of image) from blurred data
            //Uses colors from Scale object to get colors.
            ArrayToColourArray scaledColourArray = new ArrayToColourArray(reader.getArrayHeight(), reader.getArrayWidth(),
                    blurredArray.getArray(), reader.getStdDeviation(), reader.getAverage(), scale.getColorHEXScale(), gui.getWidthSlider(), gui.getDeviationsSlider());
            gui.setProgress(80);
            
            //Colors APIcalled map from above using data and color array
            ReColorImage reColor = new ReColorImage(map.getImage(),
                    scaledColourArray.getArray());
            gui.setProgress(100);
            
            //Gets the final created image
            picture = reColor.getImage();
            picture2 = reColor.getImage();
            
            return picture2; 
        }catch(Exception ex) {
            gui.setFileError("Failed: Are Headers Correct? Data to large?");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            gui.setProgress(0);

        }
            return null;
        }
        
        @Override
        protected void done(){
            try {
                gui.setImage(get());
                gui.enableSaveButton(true);
                gui.setFileError("Success");
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
