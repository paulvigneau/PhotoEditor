package example.com.projet.filter;

import android.graphics.Color;

import example.com.projet.Image;
import example.com.projet.MainActivity;
import example.com.projet.utils.ColorTools;
import example.com.projet.utils.ColorType;

/**
 * Represent the contrast filter
 */
public class Contrast extends Filter {

    /**
     * The contrast constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Contrast(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    protected void applyRenderScript(){
        main.showMessage("Not avaible in RenderScript");
        //Toast.makeText(main.getApplicationContext(),"Impossible en RenderScript",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void applyJava() {
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

    /**
     * Return the image source LUT
     *
     * @param min
     *      The minimum value
     * @param max
     *      The maximum value
     * @return
     *      The image source LUT
     */
    private int[] getLUT(int min, int max){
        int[] lut = new int[256];

        for(int index=0; index<256; index++){
            lut[index] = (255 * (index - min))/(max - min);
        }
        return lut;
    }

}
