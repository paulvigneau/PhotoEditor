package example.com.projet;

import android.graphics.Color;

public class Pixelate extends Filter {

    private int val;

    public Pixelate(MainActivity main, Image image, int val) {
        super(main, image);
        this.val = val;
    }

    @Override
    public void apply() {
        int n = (val - 1) / 2;
        int[] pixels = this.imageSrc.getPixels();
        int[] out = this.imageOut.getPixels();
        for (int y = n; y < imageSrc.getHeight(); y += val) {
            for (int x = n; x < imageSrc.getWidth(); x += val) {
                if (x + val < imageSrc.getWidth() && y + val < imageSrc.getHeight())
                    putValue(getValue(pixels, x, y), x, y, out);
            }
        }
        this.imageOut.setPixels(out);

    }

    //get the average color of the nearest pixels with path of 1 (7x7 area)
    private int getValue(int[] pixels, int x, int y) {
        int n = (val - 1) / 2;
        int red = 0, green = 0, blue = 0;
        for (int j = y - n; j <= y + n; j++) {
            for (int i = x - n; i <= x + n; i++) {
                red += Color.red(pixels[i + j * this.imageSrc.getWidth()]);
                green += Color.green(pixels[i + j * this.imageSrc.getWidth()]);
                blue += Color.blue(pixels[i + j * this.imageSrc.getWidth()]);
            }
        }
        double averageR = red / (val * val);
        double averageG = green / (val * val);
        double averageB = blue / (val * val);
        return Color.argb(255, (int) averageR, (int) averageG, (int) averageB);
    }

    private void putValue(int color, int x, int y, int[] pixels) {
        int n = (val - 1) / 2;
        for (int j = y - n; j <= y + n; j++) {
            for (int i = x - n; i <= x + n; i++) {
                pixels[i + j * this.imageOut.getWidth()] = color;
            }
        }
    }

    public void setVal(int val) {
        this.val = val;
    }
}
