/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Utility class for images. This class contains commonly used actions on uploaded images by users, 
 * to modify them according to the application's needs before saving to database.
 *
 * @author Qussay Najjar
 * @version 1.1 2011/07/15
 * @link http://qussay.me/2011/07/15/resizing-java-awt-image-with-constraining-proportions-using-imageutil/
 */
public class ImageUtil implements Serializable {

    /**
     * Return your resized image after applying the arguments invoked.
     * The method resizes your image with constrain proportions, it takes in consider if the image is: 
     * Horizontal: Applies the 'fixedSide' argument as the new width, and calculates the new height in order
     * to maintain the image scale.
     * Vertical: Applies the 'fixedSide' argument as the new height, and calculates the new width.
     * Squared: When image's width matches the height, the 'fixedSide' is used for both width and height.
     * @param originalImage Your image as array of bytes, mostly retrieved from file uploading.
     * @param fixedSide The side length in pixels which image will be resized according to (ex. 240 = 240px).
     * If you want to keep the image original dimensions invoke fixedSide = 0.
     * @param quality The percentage of the newly created image's quality compared to the original one.
     * The number should be between 0 and 100, passing 100 maintain the image's original quality.
     * @throws InterruptedException If something fails in MediaTracker#waitForID
     * @throws IOException If something fails in JPEGImageEncoder#encode or ByteArrayOutputStream#close
     */
    public  byte[] resizeImage(byte[] originalImage, int fixedSide, int quality) 
                    throws InterruptedException, IOException {
        
        // This 'Image' instance will help in retrieving image dimensions, and will hold modifications passed from 'BufferedImage'
        Image image = Toolkit.getDefaultToolkit().createImage(originalImage);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        //Load and track 'image'
        mediaTracker.addImage(image, 1);
        mediaTracker.waitForID(1);
        BufferedImage bufferedImage = null;
        
        //Making sure arguments are passed as expected
        if (originalImage == null || originalImage.length == 0 || fixedSide < 0 || quality < 0 || quality > 100)
            throw new IllegalArgumentException("Make sure your originalImage is not null, fixedSide is more than zero, and quality is between 0 and 100.");
        
        
        //Keep the image original dimensions
        if (fixedSide == 0) {
            bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
        }
        
        //If your invoked image is squared
        else if(image.getHeight(null) == image.getWidth(null)) {
            //Use 'fixedSide' value for both width, and height
            bufferedImage = new BufferedImage(fixedSide, fixedSide, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(image, 0, 0, fixedSide, fixedSide, null);
            
        } 
        //If your invoked image is horziontal
        else if (image.getWidth(null) > image.getHeight(null)) {
            //Use 'fixedSide' for the new width, and calculate the 'newHeight' to maintain the image's scale
            int newHeight = (fixedSide * image.getHeight(null)) / image.getWidth(null);
            bufferedImage = new BufferedImage(fixedSide, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(image, 0, 0, fixedSide, newHeight, null);

        }
        //If your invoked image is vertical
        else if (image.getWidth(null) < image.getHeight(null)) {
            //Use 'fixedSide' for the new height, and calculate the 'newWidth' to maintain the image's scale
            int newWidth = (fixedSide * image.getWidth(null)) / image.getHeight(null);
            bufferedImage = new BufferedImage(newWidth, fixedSide, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(image, 0, 0, newWidth, fixedSide, null);
            
        } else
            return null;
        
        //Save 'image' to ByteArrayOutputStream, and create a JPEGImageEncoder to encode the 'bufferedImage'
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JPEGImageEncoder imageEncoder = JPEGCodec.createJPEGEncoder(outputStream);
        JPEGEncodeParam encodeParam = imageEncoder.getDefaultJPEGEncodeParam(bufferedImage);
        
        //Modify the resized image quality according the original one
        encodeParam.setQuality((float)quality / 100.0f, false);
        imageEncoder.setJPEGEncodeParam(encodeParam);
        imageEncoder.encode(bufferedImage);
        
        //resizedImage[] is the final result after all modifications
        byte[] resizedImage = outputStream.toByteArray();
        outputStream.close();
        
        return resizedImage;
    }
}