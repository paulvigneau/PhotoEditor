package example.com.projet.utils;

import android.graphics.Color;

public class ColorTools {
    public static int getGreyColor(int color){
        return (int)(0.3 * Color.red(color) + 0.59 * Color.green(color) + 0.11 * Color.blue(color));
    }

    public static int[] getHSV(int color){
        int[] hsv = new int[3];

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int Cmax = Math.max(r, Math.max(g, b));
        int Cmin = Math.min(r, Math.min(g, b));

        // Define H
        if(Cmax == r)
            hsv[0] = (int)(60*(g-b)/(Cmax - Cmin) + 360) %360;
        if(Cmax == r)
            hsv[0] = (int)(60*(b-r)/(Cmax - Cmin) + 120);
        if(Cmax == r)
            hsv[0] = (int)(60*(r-g)/(Cmax - Cmin) + 240);

        //Define S
        if(Cmax == 0)
            hsv[1] = 0;
        else
            hsv[1] = 1 - (Cmin/Cmax);

        //Define V
        hsv[2] = Cmax;

        return hsv;
    }

    public static int getRGB(int[] hsv){
        int t = (int)(hsv[0] / 60)%60;
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
        }

        return 0;
    }
}
