package example.com.projet;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.Toast;

import com.android.rssample.*;

public class Colorize extends Filter {

    private int hue;

    public Colorize(MainActivity main, Image image) {
        super(main, image);
        this.hue = (int) (Math.random() * 360);
    }

    @Override
    public void applyJava(){

        main.showMessage("Not avaible in Java");
        //Toast.makeText(main.getApplicationContext(),"Impossible en Java",Toast.LENGTH_SHORT).show();
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

    public void setHue(int hue) {
        this.hue = hue;
    }

    public int getHue() {
        return this.hue;
    }

}
