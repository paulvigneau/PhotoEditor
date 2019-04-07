package example.com.projet;


import android.graphics.Color;

import example.com.projet.utils.*;


public class Histogram {
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
