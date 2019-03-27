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
        int[] img2 = new int[this.imageSrc.getWidth() * imageSrc.getHeight()];

        int red, green, blue, ind, alpha;
        int[] pixels = imageSrc.getPixels();
        for (int y = 0; y < imageSrc.getHeight(); y++) {
            for (int x = 0; x < imageSrc.getWidth(); x++) {
                ind = x + y * imageSrc.getWidth();
                int grey = ColorTools.getGreyColor(pixels[ind]);
                img1[ind] = Color.argb(255, grey, grey, grey);
                img2[ind] = ColorTools.invert(img1[ind]);

            }
        }
        imageOut.setPixels(img2);

        // gaussien (image a combiner dans convo.imageOut)
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.GAUSSIAN);
        convo.setLength(21);
        convo.apply();
        img2 = convo.imageOut.getPixels();

        int[] out = super.imageOut.getPixels();

        for (int y = 0; y < imageOut.getHeight(); y++) {
            for (int x = 0; x < imageOut.getWidth(); x++) {
                ind = x + y * imageOut.getWidth();

                red =  Color.red(img2[ind]) + Color.red(img1[ind]);
                green =Color.green(img2[ind]) + Color.green(img1[ind]);
                blue = Color.blue(img2[ind]) +  Color.blue(img1[ind]);
                alpha =  Color.alpha(img2[ind]) +  Color.alpha(img1[ind]);

                if(red>255) red =255;
                if(green>255) green =255;
                if(blue>255) blue =255;
                if(alpha>255) alpha =255;

                //desaturation
                out[ind] = ColorTools.desaturate(Color.argb(alpha, red, green, blue));
            }
        }
        imageOut.setPixels(out);
    }

}
