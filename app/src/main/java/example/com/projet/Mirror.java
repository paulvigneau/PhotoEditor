package example.com.projet;

import example.com.projet.utils.ColorTools;

public class Mirror extends  Filter {
    public Mirror(MainActivity main, Image image) {
        super(main, image);
    }

    //Inverse de haut en bas et de gauche à droite.
    /*@Override
    public void apply() {
        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        for (int x = 0; x < super.imageSrc.getHeight() * super.imageSrc.getWidth() - 1; x++) {
            out[x] = pixels[(super.imageSrc.getHeight() * super.imageSrc.getWidth() - 1) - x];
        }
        super.imageOut.setPixels(out);
    }*/

    //Inverse de haut en bas.
    /*@Override
    public void apply() {
        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        int tmp = 0;
        for (int y = super.imageSrc.getHeight()-1; y >= 0; y--) {
            for (int x = 0; x < super.imageSrc.getWidth(); x++) {
                int ind = x + y * super.imageSrc.getWidth();
                out[tmp] = pixels[ind];
                tmp ++;
            }
        }
        super.imageOut.setPixels(out);
    }*/

    //Inverse de gauche à droite.
    @Override
    public void apply() {
        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        int tmp = super.imageSrc.getHeight() * super.imageSrc.getWidth() - 1;
        for (int y = super.imageSrc.getHeight()-1; y >= 0; y--) {
            for (int x = 0; x < super.imageSrc.getWidth(); x++) {
                int ind = x + y * super.imageSrc.getWidth();
                out[tmp] = pixels[ind];
                tmp --;
            }
        }
        super.imageOut.setPixels(out);
    }
}