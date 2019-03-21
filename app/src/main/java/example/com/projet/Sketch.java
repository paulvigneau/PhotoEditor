package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Invert;

import example.com.projet.utils.ColorTools;


public class Sketch extends Filter {

    private int[] img1;
    private int[] img2;

    public Sketch(MainActivity main, Image image) {
        super(main, image);
        //passe en gris (image grise dans grey.imageOut)
        Grey grey = new Grey(super.main, super.imageSrc);
        grey.apply();
        this.img1 = grey.imageOut.getPixels();

        //inversion couleur
        Invert invert = new Invert(super.main, grey.imageOut);
        invert.apply();

        // gaussien (image a combiner dans convo.imageOut)
        Convolution convo = new Convolution(super.main, invert.imageOut);
        convo.setMatrix(Matrix.GAUSSIAN);
        convo.setLength(11);
        convo.apply();
        this.img2 = convo.imageOut.getPixels();
    }

    @Override
    public void apply() {
        int[] out = super.imageOut.getPixels();

        int red, green, blue, ind, alpha;
        for (int y = 0; y < imageOut.getHeight(); y++) {
            for (int x = 0; x < imageOut.getWidth(); x++) {
                ind = x + y * imageOut.getWidth();

                int r = Color.red(img1[ind]);
                int g = Color.green(img1[ind]);
                int b = Color.blue(img1[ind]);
                int a = Color.alpha(img1[ind]);

                red = (r!=255)? Color.red(img2[ind])*255 / (255-r) : 255;
                green = (g!=255)? Color.green(img2[ind])*255 / (255-g) : 255;
                blue = (b!=255)? Color.blue(img2[ind])*255 / (255-b) : 255;
                alpha = (a!=255)? Color.alpha(img2[ind])*255 / (255-a) : 255;


                if(red>255) red =255;
                if(green>255) green =255;
                if(blue>255) blue =255;
                if(alpha>255) alpha =255;


                out[ind] = Color.argb(alpha, red, green, blue);
            }
        }
        imageOut.setPixels(out);
    }

}
