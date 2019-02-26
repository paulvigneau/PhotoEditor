package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Blur;

public class Convolution extends Filter {

    private Matrix matrix;

    public Convolution(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    public void apply() {
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, super.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Blur blurScript = new ScriptC_Blur(rs);

        blurScript.set_in(input);
        blurScript.set_height(super.getImageSrc().getHeight());
        blurScript.set_width(super.getImageSrc().getWidth());
        blurScript.set_matrix(new int[]{
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
        });
        blurScript.set_matrixSizeX(8);
        blurScript.set_matrixSizeY(8);

        int[] matrix = blurScript.get_matrix();
        for(int index = 0; index < matrix.length; index++){
            System.out.println(matrix[index]);
        }

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
}
