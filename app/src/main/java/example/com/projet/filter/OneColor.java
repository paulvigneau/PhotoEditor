package example.com.projet.filter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_OneColor;

import example.com.projet.Image;
import example.com.projet.MainActivity;
import example.com.projet.utils.ColorTools;

/**
 * Represent the OneColor filter
 */
public class OneColor extends Filter {

    private int color;
    private int threshold;

    /**
     * The OneColor constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public OneColor(MainActivity main, Image image) {
        super(main, image);
        this.color = 0;
        this.threshold = 50;
    }

    @Override
    protected void applyJava(){
        int[] oldPixels = imageSrc.getPixels();
        int[] pixels = imageOut.getPixels();

        int col, red, green, blue;
        double distance;

        for (int index = 0; index < imageSrc.getWidth() * imageSrc.getHeight(); index++) {
            col = oldPixels[index];

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

    @Override
    protected void applyRenderScript(){
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_OneColor oneColorScript = new ScriptC_OneColor(rs);

        oneColorScript.set_red(Color.red(this.color)/255.0f);
        oneColorScript.set_green(Color.green(this.color)/255.0f);
        oneColorScript.set_blue(Color.blue(this.color)/255.0f);
        oneColorScript.set_dist(this.threshold/255.0f);

        oneColorScript.forEach_OneColor(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        oneColorScript.destroy();
        rs.destroy();
    }

    /**
     * Return the selected color
     *
     * @return
     *      The selected color
     */
    public int getColor() {
        return color;
    }

    /**
     * Set the selected color
     *
     * @param color
     *      The new selected color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Set the threshold
     *
     * @param threshold
     *      The new threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
