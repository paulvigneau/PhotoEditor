package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Brightness;


public class Brightness extends Filter {

    private int intensity;      //Valeur comprise entre 0 et 100

    public Brightness(MainActivity main, Image image) {
        super(main, image);
        this.intensity = 50; // /!\ Commencer curseur à la moyenne HSV[2] de l'image /!\
    }

    @Override
    public void apply() {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int[] newPixels = new int[width * height];

        applyRenderScript();
        //applyJava(newPixels);

    }

    private void applyJava(int[] newPixels) {
        int[] oldPixels = super.imageSrc.getPixels();
        float hsv[] = new float[3];

        for (int i = 0; i < oldPixels.length; i++) {
            Color.colorToHSV(oldPixels[i], hsv);
            hsv[2] = modifyHSV(hsv[2]);
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


    private void applyRenderScript() {
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


    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
