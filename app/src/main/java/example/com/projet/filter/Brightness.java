package example.com.projet.filter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Brightness;

import example.com.projet.Image;
import example.com.projet.MainActivity;

/**
 * Represent the brightness filter.
 */
public class Brightness extends Filter {

    private int intensity;      //between 0 and 100

    /**
     * The brightness constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Brightness(MainActivity main, Image image) {
        super(main, image);
        this.intensity = 50;
    }

    @Override
    protected void applyJava() {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int[] newPixels = new int[width * height];
        int[] oldPixels = super.imageSrc.getPixels();
        float hsv[] = new float[3];

        for (int i = 0; i < oldPixels.length; i++) {
            Color.colorToHSV(oldPixels[i], hsv);
            hsv[2] = modifyHSV(hsv[2]);
            newPixels[i] = Color.HSVToColor(hsv);
        }
        super.imageOut.setPixels(newPixels);
    }

    /**
     * Modify the saturator HSV color
     *
     * @param value
     *      The HSV color
     * @return
     *      The new saturation HSV color
     */
    private float modifyHSV(float value) {
        value += (this.intensity / 100f - 0.5);
        if (value < 0.01f)
            value = 0.01f;
        if (value > 0.99f)
            value = 0.99f;

        return value;
    }

    @Override
    protected void applyRenderScript() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Brightness brightnessScript = new ScriptC_Brightness(rs);

        brightnessScript.set_brightness(this.intensity);

        brightnessScript.forEach_Brightness(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        brightnessScript.destroy();
        rs.destroy();
    }

    /**
     * Set the brightness intensity.
     *
     * @param intensity
     *      The new brightness value
     */
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
