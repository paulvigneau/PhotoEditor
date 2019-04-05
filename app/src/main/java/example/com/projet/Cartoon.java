package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class Cartoon extends Filter {

    public Cartoon(MainActivity main, Image image) {
        super(main, image);
    }

    public void apply() {

        int[] pixels = imageSrc.getPixels();
        int[] tabgrey = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        int[] out = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        // out : reduce nb of colors
        // greytab = grey scale
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue((pixels[i]));
            if (red % 2 == 1) red--;
            if (blue % 2 == 1) blue--;
            if (green % 2 == 1) green--;
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

        // threasold image to binary in greytab
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {
            int red = Color.red(tabgrey[i]);
            int green = Color.green(tabgrey[i]);
            int blue = Color.blue((tabgrey[i]));
            if (red >0)  red = 255;
            if (green >0)  green = 255;
            if (blue >0)  blue = 255;

            tabgrey[i] = Color.argb(Color.alpha(tabgrey[i]), red, green, blue);
        }

        // combine greytab and out
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {
            int newRed = Math.max(Color.red(tabgrey[i]),Color.red(out[i]) ) ;
            int newGreen = Math.max(Color.green(tabgrey[i]),Color.green(out[i]))  ;
            int newBlue = Math.max(Color.blue(tabgrey[i]),Color.blue(out[i]) ) ;
          /*  if(newRed>255) newRed=255;
            if(newGreen>255) newGreen=255;
            if(newBlue>255) newBlue=255;*/
            out[i]=Color.argb(255,newRed,newGreen,newBlue);

        }
        imageOut.setPixels(out);
    }
}
