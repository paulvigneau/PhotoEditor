package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Blur;

public class Convolution extends Filter {

    private Matrix matrix;
    private int length;

    private int[] oldpixels;
    private int width;
    private int height;
    private float[] M;

    public Convolution(MainActivity main, Image image) {
        super(main, image);
        this.matrix = Matrix.PREWITT;
        this.length = 15;
    }

    @Override
    public void apply() {
        JavaApply();
    }

    private void JavaApply(){
        this.oldpixels = super.imageSrc.getPixels();
        this.width = super.imageSrc.getWidth();
        this.height = super.imageSrc.getHeight();
        this.M = this.matrix.getType().generate(this.length);

        int[] pixels = super.imageOut.getPixels();

        for(int y=0; y<this.height; y++){
            for(int x=0; x<this.width; x++){
                float value[] = getValue(x, y);
                pixels[x + y * this.width] = Color.argb(255, (int)(value[0]), (int)(value[1]), (int)(value[2]));
            }
        }

        super.imageOut.setPixels(pixels);
    }

    public float[] getValue(int indexX, int indexY){


        float value[] = new float[3];

        indexX -= this.length /2;
        indexY -= this.length /2;

        int size = 0;
        for(int x = 0; x < this.length; x++){
            for(int y = 0; y < this.length; y++) {
                int localX = indexX + x;
                int localY = indexY + y;
                int X = Math.abs(localX - (localX/(width-1)) * (localX%(width-1))*2);
                int Y = Math.abs(localY - (localY/(height-1)) * (localY%(height-1))*2);
                int index = X + Y * width;

                float mult = M[size];
                int color = oldpixels[index];
                value[0] += Color.red(color) * mult;
                value[1] += Color.green(color) * mult;
                value[2] += Color.blue(color) * mult;

                size++;
            }
        }
        return value;
    }

    private void RenderScriptApply(){
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Blur blurScript = new ScriptC_Blur(rs);

        blurScript.set_in(input);
        blurScript.set_height(super.getImageSrc().getHeight());
        blurScript.set_width(super.getImageSrc().getWidth());
        blurScript.set_matrix(this.matrix.getType().generate(this.length));
        blurScript.set_matrixSize(this.length);

        float[] matrix = blurScript.get_matrix();
        for(int index = 0; index < matrix.length; index++){
            System.out.println(matrix[index]);
        }
        System.out.println("size :" + blurScript.get_matrixSize());

        blurScript.forEach_Blur(output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        blurScript.destroy();
        rs.destroy();
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getLength(){
        return this.length;
    }

    public void setLength(int length){
        this.length = length;
    }
}
