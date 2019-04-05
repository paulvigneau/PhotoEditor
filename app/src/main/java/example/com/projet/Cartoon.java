package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class Cartoon extends Filter {

    public Cartoon(MainActivity main, Image image) {
        super(main, image);
    }


    public int[] createReducLUT(int nbColor) {
        int[] LUT = new int[256];
        int decalage = 256 / nbColor;
        int threesold = decalage;
        int newValue = decalage / 2;
        for (int ind = 0; ind < 256; ind++) {
            LUT[ind] = newValue;
            if (ind == threesold) {
                threesold += decalage;
                newValue += decalage;
            }
        }
        return LUT;
    }

    public void apply() {

        int[] pixels = imageSrc.getPixels();
        int[] tabgrey = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        int[] out = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        // out : reduce nb of colors
        // greytab = grey scale
        int[] LUT = createReducLUT(5);
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {

            int red = LUT[Color.red(pixels[i])];
            int green = LUT[Color.green(pixels[i])];
            int blue = LUT[Color.blue((pixels[i]))];

            out[i] = Color.argb(Color.alpha(pixels[i]), red, green, blue);
            int grey = ColorTools.getGreyColor(pixels[i]);
            tabgrey[i] = Color.argb(Color.alpha(pixels[i]), grey, grey, grey);
        }
        // edge of the gray scale in greyTab
        imageOut.setPixels(tabgrey);
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        convo.apply();
        tabgrey = convo.imageOut.getPixels();
        int[] newTab = new int[imageSrc.getWidth()* imageSrc.getHeight()];

        //epaississement des contours
        for(int y =2; y< imageSrc.getHeight()-2; y++){
            for(int x=2; x<imageSrc.getWidth()-2; x++){
                int i = x+y*imageSrc.getWidth();
                int red = Color.red(tabgrey[i]);
                int green = Color.green(tabgrey[i]);
                int blue = Color.blue((tabgrey[i]));
                if(red > 127 && green > 127 && blue > 127){

                    newTab[i] = tabgrey[i];
                    newTab[i-1]=tabgrey[i];
                    newTab[i+1]=tabgrey[i];

                    newTab[i-1-imageSrc.getWidth()]=tabgrey[i];
                    newTab[i+1-imageSrc.getWidth()]=tabgrey[i];
                    newTab[i-imageSrc.getWidth()]=tabgrey[i];

                    newTab[i-1+imageSrc.getWidth()]=tabgrey[i];
                    newTab[i+1+imageSrc.getWidth()]=tabgrey[i];
                    newTab[i+imageSrc.getWidth()]=tabgrey[i];

                }else{
                    newTab[i]= tabgrey[i];
                }
            }
        }
        tabgrey =newTab;
        // threasold image to binary in greytab
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {
            int red = Color.red(tabgrey[i]);
            int green = Color.green(tabgrey[i]);
            int blue = Color.blue((tabgrey[i]));
            if (red < 127 && green < 127 && blue < 127) {
                red = Color.red(out[i]);
                green = Color.green(out[i]);
                blue = Color.blue(out[i]);
            } else {
                red = 0;
                green = 0;
                blue = 0;
            }
            out[i] = Color.argb(Color.alpha(out[i]), red, green, blue);
        }
        imageOut.setPixels(out);
    }
}
