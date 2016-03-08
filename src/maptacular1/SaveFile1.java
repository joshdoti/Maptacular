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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 *
 * @author Josh
 * 
 * Save file gui. 
 * Saves as jpg if file extension is not given. 
 */
public class SaveFile1 {
    
    SaveFile1(BufferedImage mapPic){
        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        int i = fileChooser.showSaveDialog(fileChooser);
        File file = new File("C:/MapPicture.jpg");
        fileChooser.setCurrentDirectory(file);
        if(i == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if(!filePath.endsWith(".jpg")) {
                file = new File(filePath + ".jpg");
            }
            try {
                ImageIO.write(mapPic, "JPG", file);
        } catch (IOException ex) {
            Logger.getLogger(GetMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }
}
