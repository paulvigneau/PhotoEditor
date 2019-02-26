package example.com.projet;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Brightness;

import example.com.projet.utils.ColorTools;

public class Brightness extends Filter {

    private float intensity;

    public Brightness(MainActivity main, Image image) {
        super(main, image);
        this.intensity = 0.5f;
    }

    @Override
    public void apply() {
        int width = super.imageSrc.getWidth();
        int height = super.imageSrc.getHeight();

        int[] newPixels = new int[width * height];

        applyRenderScript(newPixels);

    }

    private void applyJava(int[] newPixels){
        int[] oldPixels = super.imageSrc.getPixels();
        float hsv[] = new float[3];
        for(int index = 0; index < oldPixels.length; index++){
            //Color.colorToHSV(oldPixels[index], hsv);      Pour test performance
            ColorTools.RGBToHSV(oldPixels[index], hsv);
            //hsv[2] = this.intensity;
            newPixels[index] = ColorTools.HSVToRGB(hsv);
            //newPixels[index] = Color.HSVToColor(hsv);     Pour test performance
        }
        super.imageOut.setPixels(newPixels);
    }

    private void applyRenderScript(int[] newPixels){
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Brightness coloriseScript = new ScriptC_Brightness(rs);

        coloriseScript.set_brightness(this.intensity);

        coloriseScript.forEach_Brightness(input, output);

       /*System.out.println("X : " + coloriseScript.get_h());
        System.out.println("Y : " + coloriseScript.get_s());
        System.out.println("Z : " + coloriseScript.get_l());*/

        System.out.println("C : " + coloriseScript.get_c());
        System.out.println("X : " + coloriseScript.get_x());
        System.out.println("M : " + coloriseScript.get_m());

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        coloriseScript.destroy();
        rs.destroy();
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
