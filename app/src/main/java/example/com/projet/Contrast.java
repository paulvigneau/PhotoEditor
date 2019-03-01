package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;
import example.com.projet.utils.ColorType;

public class Contrast extends Filter {
    //private int[] interval;
    private int intensity;      //Valeur comprise entre 0 et 255

    public Contrast(MainActivity main, Image image) {
        super(main, image);
        //this.interval = new int[]{0, 255};
        this.intensity = 10;
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

        int[] redLut = getLUT(redMinMax[0],redMinMax[1]);
        int[] greenLut = getLUT(greenMinMax[0],greenMinMax[1]);
        int[] blueLut = getLUT(blueMinMax[0],blueMinMax[1]);

        for(int ind =0; ind < imageSrc.getHeight()* imageSrc.getWidth(); ind++){
            int r = Color.red(pixels[ind]);
            int g = Color.green(pixels[ind]);
            int b = Color.blue(pixels[ind]);
            pixels2[ind] = Color.argb(255, redLut[r], greenLut[g], blueLut[b]);
        }

        imageOut.setPixels(pixels2);
    }
*/
   /* public void setInterval(int min, int max){
        this.interval[0]=min;
        this.interval[1]=max;
    }*/


   public void setIntensity(int intensity){
       this.intensity = intensity;
   }

    private int[] getLUT(int min, int max){
        int[] lut = new int[256];

        for(int index=0; index<256; index++){
            lut[index] = (255 * (index - min))/(max - min);
        }
        return lut;
    }

}
