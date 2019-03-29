package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Blur;
import com.android.rssample.ScriptC_Contour;

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
        this.length = 3;
    }

    @Override
    public void apply() {
        //RenderScriptApply();
        RenderScriptApply();
    }

    private void JavaApply() {
        this.oldpixels = super.imageSrc.getPixels();
        this.width = super.imageSrc.getWidth();
        this.height = super.imageSrc.getHeight();
        this.M = this.matrix.getType().generate(this.length);

        int[] pixels = super.imageOut.getPixels();
        if (matrix == Matrix.PREWITT || matrix == Matrix.SOBEL) {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    int value[] = getValue(x, y, true);
                    value[0] = (int) Math.sqrt(value[0] * value[0] + value[3] * value[3]);
                    value[1] = (int) Math.sqrt(value[1] * value[1] + value[4] * value[4]);
                    value[2] = (int) Math.sqrt(value[2] * value[2] + value[5] * value[5]);
                    pixels[x + y * this.width] = Color.argb(255, (value[0]), (value[1]), (value[2]));
                }
            }

        } else {
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    int value[] = getValue(x, y, false);
                    pixels[x + y * this.width] = Color.argb(255, (value[0]), (value[1]), (value[2]));
                }
            }
        }
        super.imageOut.setPixels(pixels);
    }

    public int[] getValue(int indexX, int indexY, boolean ps) {
        float value[];
        if (ps) value = new float[6];
        else value = new float[3];
        indexX -= this.length / 2;
        indexY -= this.length / 2;

        for (int x = 0; x < this.length; x++) {
            for (int y = 0; y < this.length; y++) {
                int localX = indexX + x;
                int localY = indexY + y;
                int X = Math.abs(localX - (localX / (width - 1)) * (localX % (width - 1)) * 2);
                int Y = Math.abs(localY - (localY / (height - 1)) * (localY % (height - 1)) * 2);
                int index = X + Y * width;
                int size = x + y * this.length;
                float mult = M[size];
                int color = oldpixels[index];
                value[0] += Color.red(color) * mult;
                value[1] += Color.green(color) * mult;
                value[2] += Color.blue(color) * mult;
                if (ps) {
                    size = y + x * this.length;
                    mult = M[size];
                    value[3] += Color.red(color) * mult;
                    value[4] += Color.green(color) * mult;
                    value[5] += Color.blue(color) * mult;
                }
            }
        }
        int[] val;
        if (ps)
            val = new int[]{(int) value[0], (int) value[1], (int) value[2], (int) value[3], (int) value[4], (int) value[5]};
        else val = new int[]{(int) value[0], (int) value[1], (int) value[2]};
        return val;
    }

    private void RenderScriptApply() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        if (matrix == Matrix.PREWITT || matrix == Matrix.SOBEL) {
            ScriptC_Contour contourScript = new ScriptC_Contour(rs);

            contourScript.set_in(input);
            contourScript.set_height(super.getImageSrc().getHeight());
            contourScript.set_width(super.getImageSrc().getWidth());
            Allocation matrixAlloc = Allocation.createSized(rs, Element.F32(rs), this.length * this.length);
            matrixAlloc.copyFrom(this.matrix.getType().generate(this.length));
            contourScript.bind_matrix(matrixAlloc);
            contourScript.set_matrixSize(this.length);

            contourScript.forEach_Blur(output);

            Bitmap out = super.imageOut.getBitmap();
            output.copyTo(out);
            super.imageOut.setBitmap(out);

            input.destroy();
            output.destroy();

            contourScript.destroy();
            rs.destroy();
        } else {
            ScriptC_Blur blurScript = new ScriptC_Blur(rs);

            blurScript.set_in(input);
            blurScript.set_height(super.getImageSrc().getHeight());
            blurScript.set_width(super.getImageSrc().getWidth());
            Allocation matrixAlloc = Allocation.createSized(rs, Element.F32(rs), this.length * this.length);
            matrixAlloc.copyFrom(this.matrix.getType().generate(this.length));
            blurScript.bind_matrix(matrixAlloc);
            blurScript.set_matrixSize(this.length);

            blurScript.forEach_Blur(output);

            Bitmap out = super.imageOut.getBitmap();
            output.copyTo(out);
            super.imageOut.setBitmap(out);

            input.destroy();
            output.destroy();

            blurScript.destroy();
            rs.destroy();
        }
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
