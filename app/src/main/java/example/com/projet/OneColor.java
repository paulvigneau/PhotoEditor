package example.com.projet;

import android.graphics.Color;

import example.com.projet.utils.ColorTools;

public class OneColor extends Filter {

    private int color;
    private int threshold;

    public OneColor(MainActivity main, Image image, int color, int threshold) {
        super(main, image);
        this.color = color;
        this.threshold = threshold;
    }

    @Override
    public void apply() {

        int[] pixels = imageSrc.getPixels();
        int[] pixels2 = imageOut.getPixels();
        for(int ind =0; ind < imageSrc.getHeight()*imageSrc.getWidth(); ind ++){
            int red = Color.red(pixels[ind])- Color.red(color);
            int green = Color.green(pixels[ind]) - Color.green(color);
            int blue = Color.blue(pixels[ind]) - Color.blue(color);
            int distance = (int) Math.sqrt(red*red+green*green+blue*blue);
            if(distance < threshold){
                pixels2[ind]= Color.argb(255, red, green, blue);
            }else{
                pixels2[ind]=ColorTools.getGreyColor(pixels[ind]);
            }
        }
        imageOut.setPixels(pixels2);

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
