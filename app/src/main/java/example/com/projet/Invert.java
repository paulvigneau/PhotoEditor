package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Invert;

import example.com.projet.utils.ColorTools;

public class Invert extends Filter {

    public Invert(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    public void apply(){
        JavaApply();
    }

    public void applyRS() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Invert invertScript = new ScriptC_Invert(rs);

        invertScript.forEach_Invert(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        invertScript.destroy();
        rs.destroy();
    }

    public void JavaApply(){
        int[] pixels = super.imageSrc.getPixels();
        int[] out = super.imageOut.getPixels();
        for (int y = 0; y < super.imageSrc.getHeight(); y++) {
            for (int x = 0; x < super.imageSrc.getWidth(); x++) {
                int ind = x + y * super.imageSrc.getWidth();
                int grey = ColorTools.invert(pixels[ind]);
                out[ind] = Color.argb(Color.alpha(pixels[ind]), grey, grey, grey);
            }
        }
        super.imageOut.setPixels(out);
    }


}