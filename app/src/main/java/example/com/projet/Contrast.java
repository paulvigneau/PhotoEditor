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
    }

}
