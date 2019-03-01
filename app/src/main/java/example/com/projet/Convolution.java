package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Blur;

public class Convolution extends Filter {

    private Matrix matrix;
    private int lenght;

    public Convolution(MainActivity main, Image image) {
        super(main, image);
        this.matrix = Matrix.AVERAGING;
        this.lenght = 8;
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
        blurScript.set_matrix(this.matrix.getType().generate(this.lenght));
        blurScript.set_matrixSize(this.lenght);

        float[] matrix = blurScript.get_matrix();
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

    public int getLenght(){
        return this.lenght;
    }

    public void setLenght(int lenght){
        this.lenght = lenght;
    }
}
