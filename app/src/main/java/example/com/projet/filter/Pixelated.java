package example.com.projet.filter;

import android.graphics.Color;

import example.com.projet.Filter;
import example.com.projet.Image;
import example.com.projet.MainActivity;

public class Pixelated extends Filter {

    private int length;

    public Pixelated(MainActivity main, Image image, int val) {
        super(main, image);
        this.length = val;
    }

    @Override
    protected void applyRenderScript(){
        main.showMessage("Not avaible in RenderScript");
        //showAlert();
    }

    @Override
    protected void applyJava() {
        int n = (length - 1) / 2;
        int[] pixels = this.imageSrc.getPixels();
        int[] out = this.imageOut.getPixels();
        for (int y = n; y < imageSrc.getHeight() + length/2; y += length) {
            for (int x = n; x < imageSrc.getWidth() + length/2; x += length) {
                putValue(getValue(pixels, x, y), x, y, out);
            }
        }
        this.imageOut.setPixels(out);

    }

    //get the average color of the nearest pixels with path of 1 (7x7 area)
    private int getValue(int[] pixels, int indexX, int indexY) {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int n = (length - 1) / 2;
        indexX -= this.length / 2;
        indexY -= this.length / 2;

        int red = 0, green = 0, blue = 0;
        for (int x = 0; x < this.length; x++) {
            for (int y = 0; y < this.length; y++) {
                int localX = indexX + x;
                int localY = indexY + y;
                int X = Math.abs(localX - (localX / (width - 1)) * (localX % (width - 1)) * 2);
                int Y = Math.abs(localY - (localY / (height - 1)) * (localY % (height - 1)) * 2);

                int index = X + Y * width;
                red += Color.red(pixels[index]);
                green += Color.green(pixels[index]);
                blue += Color.blue(pixels[index]);
            }
        }
        double averageR = red / (float)(length * length);
        double averageG = green / (float)(length * length);
        double averageB = blue / (float)(length * length);
        return Color.argb(255, (int) averageR, (int) averageG, (int) averageB);
    }

    private void putValue(int color, int indexX, int indexY, int[] pixels) {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int n = (length - 1) / 2;
        indexX -= this.length / 2;
        indexY -= this.length / 2;

        for (int x = 0; x < this.length; x++) {
            for (int y = 0; y < this.length; y++) {
                int localX = indexX + x;
                int localY = indexY + y;
                int X = Math.abs(localX - (localX / (width - 1)) * (localX % (width - 1)) * 2);
                int Y = Math.abs(localY - (localY / (height - 1)) * (localY % (height - 1)) * 2);

                pixels[X + Y * width] = color;
            }
        }
    }

    public void setLength(int length) {
        this.length = length;
    }
}
