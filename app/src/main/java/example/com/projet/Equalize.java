package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorType;

public class Equalize extends Filter {


    private Histogram histogramSRC;

    public Equalize(MainActivity main, Image image) {
        super(main, image);
        histogramSRC = new Histogram(image,ColorType.GREY);
    }

    @Override
    public void apply() {

        int[] CumuledHistogram = new int[256];
        for(int ind =0; ind< 256; ind++){
            for (int ind2 =0; ind2<=ind; ind2++){
                CumuledHistogram[ind]+=histogramSRC.getHistogram()[ind2];
            }
        }
        int new_colorR, new_colorG,new_colorB;
        int[] pixels = imageSrc.getPixels();
        int size = imageSrc.getWidth()*imageSrc.getHeight();
        int[] pixels2 = imageOut.getPixels();
        for(int y =0; y< imageSrc.getHeight(); y++){
            for(int x=0; x < imageSrc.getWidth(); x++){
                new_colorR = (int)((long)(CumuledHistogram[Color.red(pixels[x+y*imageSrc.getWidth()])] * 255) / size);
                new_colorG = (int)((long)(CumuledHistogram[Color.green(pixels[x+y*imageSrc.getWidth()])] * 255) / size);
                new_colorB = (int)((long)(CumuledHistogram[Color.blue(pixels[x+y*imageSrc.getWidth()])] * 255) / size);
                pixels2[x+y*imageSrc.getWidth()] = Color.argb(255, new_colorR, new_colorG, new_colorB);
            }
        }
        imageOut.setPixels(pixels2);
    }

}
