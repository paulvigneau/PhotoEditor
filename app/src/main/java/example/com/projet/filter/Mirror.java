package example.com.projet.filter;

import example.com.projet.Image;
import example.com.projet.MainActivity;

/**
 * Represent the mirror filter
 */
public class Mirror extends Filter {
    private boolean orientation;

    /**
     * The mirror constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Mirror(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    protected void applyRenderScript(){
        main.showMessage("Not available in RenderScript");
    }

    @Override
    protected void applyJava() {
        if(this.orientation){
            applyHorizontal();
        }else{
            applyVertical();
        }
        this.imageSrc = new Image(this.imageOut);
    }

    /**
     * Apply the vertical mirror in output image
     */
    public void applyVertical(){
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
    }

    /**
     * Apply the horizontal mirror in output image
     */
    public void applyHorizontal(){
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

    /**
     * Set the mirror orientation
     *
     * @param isHorizontal
     *      TRUE if is mirror horizontal
     */
    public void setOrientation(boolean isHorizontal) {
        this.orientation = isHorizontal;
    }
}
