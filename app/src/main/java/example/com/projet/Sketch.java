package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class Sketch extends Filter {



    public Sketch(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    public void apply() {

        int[] img1 = new int[this.imageSrc.getWidth() * imageSrc.getHeight()];
        int ind;
        int[] pixels = imageSrc.getPixels();
       for (int y = 0; y < imageSrc.getHeight(); y++) {
            for (int x = 0; x < imageSrc.getWidth(); x++) {
                ind = x + y * imageSrc.getWidth();
                int grey = ColorTools.getGreyColor(pixels[ind]);
                img1[ind] = Color.argb(255, grey, grey, grey);
            }
        }
        imageOut.setPixels(img1);

        // gaussien (image a combiner dans convo.imageOut)
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        convo.apply();
        int[] img2 = convo.imageOut.getPixels();

        int[] out = super.imageOut.getPixels();

        for (int y = 0; y < imageOut.getHeight(); y++) {
            for (int x = 0; x < imageOut.getWidth(); x++) {
                ind = x + y * imageOut.getWidth();
                out[ind] = ColorTools.invert(img2[ind]);
            }
        }
        imageOut.setPixels(out);
    }

}
