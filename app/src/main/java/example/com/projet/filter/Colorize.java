package example.com.projet.filter;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.*;

import example.com.projet.Image;
import example.com.projet.MainActivity;

/**
 * Represent the colorize filter
 */
public class Colorize extends Filter {

    private int hue;

    /**
     * The colorize constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Colorize(MainActivity main, Image image) {
        super(main, image);
        this.hue = (int) (Math.random() * 360);
    }

    @Override
    public void applyJava(){
        main.showMessage("Not available in Java");
    }

    @Override
    public void applyRenderScript() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Colorize colorizeScript = new ScriptC_Colorize(rs);

        colorizeScript.set_hue(this.hue);

        colorizeScript.forEach_Colorize(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        colorizeScript.destroy();
        rs.destroy();
    }

    /**
     * Set the hue value
     *
     * @param hue
     *      The new hue value.
     */
    public void setHue(int hue) {
        this.hue = hue;
    }

}
