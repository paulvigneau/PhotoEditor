package example.com.projet;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class Image {
    private int[] pixels;
    private int height;
    private int width;
    private Config configBitmap;

    public Image(Bitmap map){
        this.height = map.getHeight();
        this.width = map.getWidth();
        this.configBitmap = map.getConfig();
        this.pixels = new int[this.height * this.width];

        map.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    public int[] getPixels(){
        return this.pixels;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }
}
