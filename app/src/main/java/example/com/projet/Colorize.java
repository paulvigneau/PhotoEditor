package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class Colorize extends Filter {

    private int hue;
    private boolean isRandom;

    public Colorize(Image image) {
        super(image);
        this.hue = 0;
        this.isRandom = true;
    }

    @Override
    public void apply() {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int[] newPixels = new int[width * height];

        int[] oldPixels = super.imageSrc.getPixels();
        float hsv[] = new float[3];
        for(int index = 0; index < oldPixels.length; index++){
            ColorTools.RGBToHSV(oldPixels[index], hsv);
            hsv[0] = this.hue;
            newPixels[index] = ColorTools.HSVToRGB(hsv);
        }
        super.imageOut.setPixels(newPixels);
    }

    public void setHue(int hue){
        this.hue = hue;
    }

    public void setRandom(boolean isRandom){
        this.isRandom = isRandom;
    }

    public int getHue(){
        return this.hue;
    }

    public boolean isRandom(){
        return this.isRandom;
    }
}
