package example.com.projet.filter;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Sepia;

import example.com.projet.Image;
import example.com.projet.MainActivity;
import example.com.projet.utils.ColorTools;

/**
 * Represent the Sepia filter
 */
public class Sepia extends Filter {

    /**
     * The Sepia constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Sepia(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    protected void applyJava() {

        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        for (int y = 0; y < super.imageSrc.getHeight(); y++) {
            for (int x = 0; x < super.imageSrc.getWidth(); x++) {
                int ind = x + y * super.imageSrc.getWidth();
                out[ind] = ColorTools.sepia(pixels[ind]);
            }
        }
        super.imageOut.setPixels(out);
    }

    @Override
    protected void applyRenderScript() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Sepia sepiaScript = new ScriptC_Sepia(rs);

        sepiaScript.forEach_Sepia(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        sepiaScript.destroy();
        rs.destroy();
    }
}
