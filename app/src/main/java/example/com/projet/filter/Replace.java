package example.com.projet.filter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Replace;

import example.com.projet.Image;
import example.com.projet.MainActivity;

/**
 * Represent the Replace color filter
 */
public class Replace extends Filter {

    private int color;
    private int replaceColor;
    private int threshold;

    /**
     * The Represent constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Replace(MainActivity main, Image image) {
        super(main, image);
        this.color = 0;
        this.replaceColor =0;
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
        replaceScript.set_replaceR(Color.red(this.replaceColor)/255.0f);
        replaceScript.set_replaceG(Color.green(this.replaceColor)/255.0f);
        replaceScript.set_replaceB(Color.blue(this.replaceColor)/255.0f);

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

    /**
     * Return the selected color
     *
     * @return
     *      The selected color
     */
    public int getColor() {
        return color;
    }

    /**
     * Set the replace color
     *
     * @param color
     *      The new replace color
     */
    public void setReplaceColor(int color) {
        this.replaceColor = color;
    }

    /**
     * Set the selected color
     *
     * @param color
     *      The new selected color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Set the threshold
     *
     * @param threshold
     *      The new threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
