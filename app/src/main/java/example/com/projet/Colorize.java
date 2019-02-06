package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import example.com.projet.utils.ColorTools;
import com.android.rssample.*;

public class Colorize extends Filter {

    private int hue;
    private boolean isRandom;

    public Colorize(MainActivity main, Image image) {
        super(main, image);
        this.hue = (int) (Math.random() * 360);
        this.isRandom = true;
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
            hsv[0] = this.hue;
            newPixels[index] = ColorTools.HSVToRGB(hsv);
            //newPixels[index] = Color.HSVToColor(hsv);     Pour test performance
        }
        super.imageOut.setPixels(newPixels);
    }

    private void applyRenderScript(int[] newPixels){
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Colorise coloriseScript = new ScriptC_Colorise(rs);

        coloriseScript.set_hue(this.hue);

        coloriseScript.forEach_Colorise(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        coloriseScript.destroy();
        rs.destroy();
    }

    public void setHue(int hue){
        this.hue = hue;
    }

    public void setRandom(boolean isRandom){
        this.isRandom = isRandom;
    }

    public int getHue(){
        return this.hue;
    }

    public boolean isRandom(){
        return this.isRandom;
    }
}
