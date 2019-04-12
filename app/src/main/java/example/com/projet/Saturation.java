package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Saturation;

public class Saturation extends Filter {

    private int intensity;

    public Saturation(MainActivity main, Image image) {
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
            hsv[1] = modifyHSV(hsv[1]);
            newPixels[i] = Color.HSVToColor(hsv);
        }
        super.imageOut.setPixels(newPixels);
    }

    private float modifyHSV(float value) {
        value += (this.intensity / 100f - 0.5);
        if (value < 0.01f)
            value = 0.01f;
        if (value > 0.99f)
            value = 0.99f;

        return value;
    }

    public void setIntensity(int sat){ this.intensity =sat;}

    @Override
    protected void applyRenderScript() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Saturation saturationScript = new ScriptC_Saturation(rs);

        saturationScript.set_saturation(this.intensity);

        saturationScript.forEach_Saturation(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        saturationScript.destroy();
        rs.destroy();
    }

}
