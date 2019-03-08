package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Brightness;

import example.com.projet.utils.ColorTools;

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

        applyRenderScript(newPixels);
        //applyJava(newPixels);

    }

    private void applyJava(int[] newPixels){
        int[] oldPixels = super.imageSrc.getPixels();
        float hsv[] = new float[3];

        for (int i = 0; i < oldPixels.length; i++){
            Color.colorToHSV(oldPixels[i], hsv);      //Pour test performance
            //ColorTools.RGBToHSV(oldPixels[i], hsv); // #Ne fonctionne pas pour hsv[2], revoir le calcul.
            hsv[2] = modifyHSV(hsv[2]);
            //newPixels[i] = ColorTools.HSVToRGB(hsv);// #Ne fonctionne pas pour hsv[2], revoir le calcul.
            newPixels[i] = Color.HSVToColor(hsv);     //Pour test performance
        }
        super.imageOut.setPixels(newPixels);
    }

    private float modifyHSV(float value){
        //System.out.println("OLD value : " + value);
        //System.out.println("NEW value : " + value);
        value += (this.intensity / 100f - 0.5);
        if(value < 0.01f)
            value = 0.01f;
        if(value > 0.99f)
            value = 0.99f;

        return value;
    }

    /*private void applyJava(int[] newPixels){
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
    }*/

    private void applyRenderScript(int[] newPixels){
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

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
