package example.com.projet;

import example.com.projet.utils.ColorTools;

public class Sepia extends Filter {

    public Sepia(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    public void apply() {
        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        for (int y = 0; y < super.imageSrc.getHeight(); y++) {
            for (int x = 0; x < super.imageSrc.getWidth(); x++) {
                int ind = x + y * super.imageSrc.getWidth();
                out[ind] = ColorTools.sepia(pixels[ind]);
            }
        }
        super.imageOut.setPixels(out);
    }
}
