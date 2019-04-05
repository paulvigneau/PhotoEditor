package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;
import example.com.projet.utils.ColorType;

public class Contrast extends Filter {
    private int[] interval;

    public Contrast(MainActivity main, Image image) {
        super(main, image);
        this.interval = new int[]{0, 255};
    }

    /*@Override
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
            //Color.colorToHSV(oldPixels[index], hsv);      //Pour test performance
            ColorTools.RGBToHSV(oldPixels[index], hsv); // #Ne fonctionne pas pour hsv[2], revoir le calcul.
            hsv[2] = modifiedHSV(hsv[2]);
            newPixels[index] = ColorTools.HSVToRGB(hsv);// #Ne fonctionne pas pour hsv[2], revoir le calcul.
            //newPixels[index] = Color.HSVToColor(hsv);     //Pour test performance
        }
        super.imageOut.setPixels(newPixels);
    }

    private float modifiedHSV(float hsv){
        hsv -= -(this.intensity/100f - 0.5);
        if(hsv < 0.01f)
            hsv = 0.01f;
        if(hsv > 0.99f)
            hsv = 0.99f;

        return hsv;
    }*/

    @Override
    public void apply() {
        int[] oldPixels = imageSrc.getPixels();
        int[] newPixels = imageOut.getPixels();

        int[] redMinMax = ColorTools.getMinMax(oldPixels,ColorType.RED);
        int[] greenMinMax = ColorTools.getMinMax(oldPixels,ColorType.GREEN);
        int[] blueMinMax = ColorTools.getMinMax(oldPixels,ColorType.BLUE);

        int[] globalMinMax = redMinMax;
        if(greenMinMax[0] < globalMinMax[0]){
            globalMinMax[0] = greenMinMax[0];
        }
        if(blueMinMax[0] < globalMinMax[0]){
            globalMinMax[0] = blueMinMax[0];
        }

        if(greenMinMax[1] > globalMinMax[1]){
            globalMinMax[1] = greenMinMax[1];
        }
        if(blueMinMax[1] > globalMinMax[1]){
            globalMinMax[1] = blueMinMax[1];
        }

        int[] Lut = getLUT(globalMinMax[0],globalMinMax[1]);

        for(int ind =0; ind < imageSrc.getHeight()* imageSrc.getWidth(); ind++){
            int r = Color.red(oldPixels[ind]);
            int g = Color.green(oldPixels[ind]);
            int b = Color.blue(oldPixels[ind]);
            newPixels[ind] = Color.argb(255, Lut[r], Lut[g], Lut[b]);
        }

        imageOut.setPixels(newPixels);
    }

    private int[] getLUT(int min, int max){
        int[] lut = new int[256];

        for(int index=0; index<256; index++){
            lut[index] = (255 * (index - min))/(max - min);
        }
        return lut;
    }

}
