package example.com.projet.filter;

import example.com.projet.Image;
import example.com.projet.MainActivity;

/**
 * Represent the filter
 */
public abstract class Filter {
    protected MainActivity main;
    protected Image imageSrc;
    protected Image imageOut;

    /**
     * The filter constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The image source
     */
    public Filter(MainActivity main, Image image) {
        this.main = main;

        this.imageSrc = image;
        this.imageOut = new Image(image);
    }

    /**
     * Apply the filter on output image
     *
     * @param inRS
     *      TRUE if aplly in RenderScript
     */
    public void apply(boolean inRS){
        if(inRS){
            applyRenderScript();
        }else{
            applyJava();
        }
    }

    /**
     * Apply the filter on output image in Java
     */
    protected abstract void applyJava();

    /**
     * Apply the filter on putput image in RenderScript
     */
    protected abstract void applyRenderScript();

    /**
     * Return the source image
     *
     * @return
     *      The source image
     */
    public Image getImageSrc(){
        return this.imageSrc;
    }

    /**
     * Return the output image
     *
     * @return
     *      The output image
     */
    public Image getImageOut(){
        return this.imageOut;
    }

    /**
     * Set the source image
     *
     * @param source
     *      The new source image
     */
    public void setImageSrc(Image source){
        this.imageSrc = source;
    }
}
