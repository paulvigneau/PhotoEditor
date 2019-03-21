package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class Grey extends Filter {

    public Grey(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    public void apply() {
        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        for (int y = 0; y < super.imageSrc.getHeight(); y++) {
            for (int x = 0; x < super.imageSrc.getWidth(); x++) {
                int ind = x + y * super.imageSrc.getWidth();
                int grey = ColorTools.getGreyColor(pixels[ind]);
                out[ind] = Color.argb(Color.alpha(pixels[ind]), grey, grey, grey);
            }
        }
        super.imageOut.setPixels(out);
    }
}
