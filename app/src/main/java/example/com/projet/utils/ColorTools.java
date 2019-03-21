package example.com.projet.utils;

import android.graphics.Color;

public class ColorTools {
    public static int getGreyColor(int color){
        return (int)(0.3 * Color.red(color) + 0.59 * Color.green(color) + 0.11 * Color.blue(color));
    }

    public static void RGBToHSV(int color, float[] hsvOutput){
        float r = Color.red(color) / 255.0f;
        float g = Color.green(color) / 255.0f;
        float b = Color.blue(color) / 255.0f;

        float Cmax = Math.max(r, Math.max(g, b));
        float Cmin = Math.min(r, Math.min(g, b));

        float delta = Cmax - Cmin;

        // Define H
        if((int)(delta) == 0)
            hsvOutput[0] = 0;
        else if(Cmax == r)
            hsvOutput[0] = 60 * (((g-b)/delta) % 6);
        else if(Cmax == g)
            hsvOutput[0] = 60 * ((b-r)/delta + 2);
        else if(Cmax == b)
            hsvOutput[0] = 60 * ((r-g)/delta + 4);

        //Define S
        if(Cmax == 0)
            hsvOutput[1] = 0;
        else
            hsvOutput[1] = delta / Cmax;

        //Define V
        hsvOutput[2] = Cmax;

    }

    public static int HSVToRGB(float[] hsv){
        float c = hsv[2] * hsv[1];
        float x = c * (1 - Math.abs(((hsv[0] / 60.0f) % 2) - 1));
        float m = hsv[2] - c;

        int t = (int)(hsv[0] / 60.0f)%60;

        switch (t){
            case 0:
                return Color.argb(255, (int)((c + m) * 255), (int)((x + m) * 255), (int)(m * 255));
            case 1:
                return Color.argb(255, (int)((x + m) * 255), (int)((c + m) * 255), (int)(m * 255));
            case 2:
                return Color.argb(255, (int)(m * 255), (int)((c + m) * 255), (int)((x + m) * 255));
            case 3:
                return Color.argb(255, (int)(m * 255), (int)((x + m) * 255), (int)((c + m) * 255));
            case 4:
                return Color.argb(255, (int)((x + m) * 255), (int)(m * 255), (int)((c + m) * 255));
            case 5:
                return Color.argb(255, (int)((c + m) * 255), (int)(m * 255), (int)((x + m) * 255));
        }


         /*   int t = (int)(hsv[0] / 60.0f)%60;
        float f = (hsv[0] / 60.0f) - t;
        float l = hsv[2] * (1 - hsv[1]);
        float m = hsv[2] * (1 - f * hsv[1]);
        float n = hsv[2] * (1 - (1 - f) * hsv[1]);

        switch (t){
            case 0:
                return Color.argb(255, (int)(hsv[2] * 255), (int)(n*255), (int)(l * 255));
            case 1:
                return Color.argb(255, (int)(m*255), (int)(hsv[2]*255), (int)(l*255));
            case 2:
                return Color.argb(255, (int)(l*255), (int)(hsv[2]*255), (int)(n*255));
            case 3:
                return Color.argb(255, (int)(l*255), (int)(m*255), (int)(hsv[2]*255));
            case 4:
                return Color.argb(255, (int)(n*255), (int)(l*255), (int)(hsv[2]*255));
            case 5:
                return Color.argb(255, (int)(hsv[2]*255), (int)(l*255), (int)(m*255));
        }*/

        return 0;
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
}
