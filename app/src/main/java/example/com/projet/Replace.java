package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Replace;

public class Replace extends Filter {

    private int color;
    private int newColor;
    private int threshold;

    public Replace(MainActivity main, Image image) {
        super(main, image);
        this.color = 0;
        this.newColor =0;
        this.threshold = 50;
    }

    @Override
    protected void applyJava(){
        main.showMessage("Not avaible in Java");
        //showAlert();
    }

    @Override
    public void applyRenderScript(){
       RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Replace replaceScript = new ScriptC_Replace(rs);

        replaceScript.set_red(Color.red(this.color)/255.0f);
        replaceScript.set_green(Color.green(this.color)/255.0f);
        replaceScript.set_blue(Color.blue(this.color)/255.0f);
        replaceScript.set_replaceR(Color.red(this.newColor)/255.0f);
        replaceScript.set_replaceG(Color.green(this.newColor)/255.0f);
        replaceScript.set_replaceB(Color.blue(this.newColor)/255.0f);

        replaceScript.set_dist(this.threshold/255.0f);

        replaceScript.forEach_Replace(input, output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        replaceScript.destroy();
        rs.destroy();
    }

    public int getColor() {
        return color;
    }
    public int getNewColor(){return  newColor;}

    public void setNewColor(int color) {
        this.newColor = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
