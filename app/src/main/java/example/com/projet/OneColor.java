package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class OneColor extends Filter {

    private int color;
    private int threshold;

    public OneColor(MainActivity main, Image image) {
        super(main, image);
        this.color = 0;
        this.threshold = 50;
    }

    @Override
    public void apply() {
        int[] oldpixels = imageSrc.getPixels();
        int[] pixels = imageOut.getPixels();

        int col, red, green, blue;
        double distance;

        for (int index = 0; index < imageSrc.getWidth() * imageSrc.getHeight(); index++) {
            col = oldpixels[index];

            red = Color.red(col) - Color.red(this.color);
            green = Color.green(col) - Color.green(this.color);
            blue = Color.blue(col) - Color.blue(this.color);

            distance = Math.sqrt(red * red + green * green + blue * blue);

            if (distance <= threshold) {
                pixels[index] = col;
            } else {
                int grey = ColorTools.getGreyColor(col);
                pixels[index] = Color.argb(255, grey, grey, grey);
            }
        }

        imageOut.setPixels(pixels);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
