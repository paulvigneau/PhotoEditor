package example.com.projet;

import android.graphics.Color;

import example.com.projet.lut.LUT;
import example.com.projet.utils.ColorTools;
import example.com.projet.utils.ColorType;

public class Contrast extends Filter {
    private int[] interval;



    public Contrast(MainActivity main, Image image, int[] interval) {
        super(main, image);
        this.interval = interval;
    }

    @Override
    public void apply() {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int[] newPixels = new int[width * height];

        applyJava(newPixels);

    }

    private void applyJava(int[] newPixels){
        int[] oldPixels = super.imageSrc.getPixels();
        float hsv[] = new float[3];
        for(int index = 0; index < oldPixels.length; index++){
            //Color.colorToHSV(oldPixels[index], hsv);      Pour test performance
            ColorTools.RGBToHSV(oldPixels[index], hsv);
            hsv[2] = modifiedHSV(hsv[2]);;
            newPixels[index] = ColorTools.HSVToRGB(hsv);
            //newPixels[index] = Color.HSVToColor(hsv);     Pour test performance
        }
        super.imageOut.setPixels(newPixels);
    }

    private float modifiedHSV(float hsv){

       /* int bar_value = ...;
        hsv -= (50-nb)/100;
        if(hsv < 0)
            hsv = 0;
        if(hsv > 1)
            hsv = 1;*/

       hsv += 0.1;
       if(hsv > 1)
           hsv = 1;

        return hsv;
    }

    /*@Override
    public void apply() {
        int[] pixels = imageSrc.getPixels();
        int[] pixels2 = imageOut.getPixels();

        int[] redMinMax = interval;
        int[] greenMinMax = interval ;
        int[] blueMinMax = interval ;

        if(interval[0]==interval[1]){
            redMinMax = ColorTools.getMinMax(pixels,ColorType.RED);
            greenMinMax = ColorTools.getMinMax(pixels,ColorType.GREEN);
            blueMinMax = ColorTools.getMinMax(pixels,ColorType.BLUE);

        }

        int[] redLut = LUT.CONTRAST_LINEAR.getLUT(redMinMax[0],redMinMax[1]);
        int[] greenLut = LUT.CONTRAST_LINEAR.getLUT(greenMinMax[0],greenMinMax[1]);
        int[] blueLut = LUT.CONTRAST_LINEAR.getLUT(blueMinMax[0],blueMinMax[1]);

        for(int ind =0; ind < imageSrc.getHeight()* imageSrc.getWidth(); ind++){
            int r = Color.red(pixels[ind]);
            int g = Color.green(pixels[ind]);
            int b = Color.blue(pixels[ind]);
            pixels2[ind] = Color.argb(255, redLut[r], greenLut[g], blueLut[b]);
        }

        imageOut.setPixels(pixels2);
    }

    public void setInterval(int min, int max){
        this.interval[0]=min;
        this.interval[1]=max;
    }*/

}
