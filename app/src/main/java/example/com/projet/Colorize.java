package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import example.com.projet.utils.ColorTools;
import com.android.rssample.*;

public class Colorize extends Filter {

    private int hue;

    public Colorize(MainActivity main, Image image) {
        super(main, image);
        this.hue = (int) (Math.random() * 360);
    }

    @Override
    public void apply() {
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

    public int getHue(){
        return this.hue;
    }

}
