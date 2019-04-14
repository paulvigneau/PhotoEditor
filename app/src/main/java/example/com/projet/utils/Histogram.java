package example.com.projet.utils;


import android.graphics.Color;

import example.com.projet.Image;
import example.com.projet.utils.*;

/**
 * Represent the histogram function
 */
public class Histogram {
    /**
     * Create and return the histogram values
     *
     * @param image
     *      The source image
     * @param color
     *      The color type
     * @return
     *      The histogram value
     */
    public static int[] createHistogram(Image image, ColorType color){
        int[] histogram = new int[256];
        int[] pixels = image.getPixels();
        int size = image.getHeight() * image.getWidth();
        for(int i=0; i<size; i++){
            switch(color){
                case RED:
                    histogram[Color.red(pixels[i])]++;
                    break;
                case GREEN :
                    histogram[Color.green(pixels[i])]++;
                    break;
                case BLUE :
                    histogram[Color.blue(pixels[i])]++;
                    break;
                case GREY :
                    histogram[ColorTools.getGreyColor(pixels[i])]++;
                    break;
                default :
                    break;
            }
        }
        return histogram;
    }
}
