package example.com.projet.utils;

import android.graphics.Color;

public class ColorTools {
    public static int getGreyColor(int color){
        return (int)(0.3 * Color.red(color) + 0.59 * Color.green(color) + 0.11 * Color.blue(color));
    }

    public static int[] getMinMax(int[] pixels, ColorType color ){
        int[] minMax = {255,0};
        int colorPixel =0;
        for(int i =0; i<pixels.length;i++){
            switch(color){
                case GREY:
                    colorPixel = ColorTools.getGreyColor(i);
                    break;
                case RED:
                    colorPixel = Color.red(pixels[i]);
                    break;
                case GREEN:
                    colorPixel = Color.green(pixels[i]);
                    break;
                case BLUE:
                    colorPixel = Color.blue(pixels[i]);
                    break;
                default:
                    break;
            }
            if(minMax[0]>colorPixel) minMax[0] = colorPixel;
            if(minMax[1]<colorPixel) minMax[1] = colorPixel;
        }
        return minMax;
    }


    public static int invert(int color){
        int red = 255 - Color.red(color);
        int green = 255 - Color.green(color);
        int blue = 255 - Color.blue(color);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public static int sepia(int color){
        double newRed = 0.393*Color.red(color) + 0.769*Color.green(color) + 0.189*Color.blue(color);
        double newGreen = 0.349*Color.red(color) + 0.686*Color.green(color) + 0.168*Color.blue(color);
        double newBlue = 0.272*Color.red(color) + 0.534*Color.green(color) + 0.131*Color.blue(color);
        if(newRed>255) newRed=255;
        if(newGreen>255) newGreen=255;
        if(newBlue>255) newBlue=255;

        return Color.argb(Color.alpha(color), (int) newRed, (int)newGreen,(int)newBlue);
    }

}
