package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * Represent the image
 */
public class Image {
    private int[] pixels;
    private int height;
    private int width;
    private Config configBitmap;

    /**
     * The image constructor
     *
     * @param map
     *      The image bitmap
     */
    public Image(Bitmap map){
        Runtime.getRuntime().gc();

        this.height = map.getHeight();
        this.width = map.getWidth();
        this.configBitmap = map.getConfig();
        this.pixels = new int[this.height * this.width];

        map.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    /**
     * The image constructor
     *
     * @param image
     *      The image to copy
     */
    public Image(Image image){
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.configBitmap = image.getConfigBitmap();
        this.pixels = image.getPixels().clone();
    }

    /**
     * Return the image pixels
     *
     * @return
     *      The image pixels
     */
    public int[] getPixels(){
        return this.pixels;
    }

    /**
     * Return the image height
     *
     * @return
     *      The image height
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * Return the image width
     *
     * @return
     *      The image width
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Return the image bitmap configuration
     *
     * @return
     *      The image bitmap configuration
     */
    private Config getConfigBitmap() {
        return this.configBitmap;
    }

    /**
     * Set the image pixels
     *
     * @param pixels
     *      The new image pixels
     */
    public void setPixels(int[] pixels){
        this.pixels = pixels;
    }

    /**
     * Set the image height
     *
     * @param height
     *      The new image height
     */
    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Set the image width
     *
     * @param width
     *      The new image width
     */
    public void setWidth(int width){
        this.width = width;
    }

    /**
     * Create and return the image bitmap
     *
     * @return
     *      The new image bitmap
     */
    public Bitmap getBitmap(){
        return Bitmap.createBitmap(this.pixels, this.width, this.height, this.configBitmap);
    }

    /**
     * Change the image width a bitmap
     *
     * @param map
     *      The image bitmap
     */
    public void setBitmap(Bitmap map){
        this.height = map.getHeight();
        this.width = map.getWidth();
        this.configBitmap = map.getConfig();
        this.pixels = new int[this.width * this.height];

        map.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }
}
