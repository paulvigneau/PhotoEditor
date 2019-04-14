package example.com.projet;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.Toast;

import com.android.rssample.*;

/**
 * Represent the colorise filter
 */
public class Colorize extends Filter {

    private int hue;

    /**
     * The colorise constructor
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
        main.showMessage("Not avaible in Java");
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
