package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;

import com.android.rssample.ScriptC_Sketch;

import example.com.projet.utils.ColorTools;

public class Sketch extends Filter {


    public Sketch(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    protected void applyJava() {
        int[] img1 = new int[this.imageSrc.getWidth() * imageSrc.getHeight()];
        int ind;
        int[] pixels = imageSrc.getPixels();
        for (int y = 0; y < imageSrc.getHeight(); y++) {
            for (int x = 0; x < imageSrc.getWidth(); x++) {
                ind = x + y * imageSrc.getWidth();
                int grey = ColorTools.getGreyColor(pixels[ind]);
                img1[ind] = Color.argb(255, grey, grey, grey);
            }
        }
        imageOut.setPixels(img1);

        // gaussien (image a combiner dans convo.imageOut)
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        convo.apply(true);
        int[] img2 = convo.imageOut.getPixels();

        int[] out = super.imageOut.getPixels();

        for (int y = 0; y < imageOut.getHeight(); y++) {
            for (int x = 0; x < imageOut.getWidth(); x++) {
                ind = x + y * imageOut.getWidth();
                out[ind] = ColorTools.invert(img2[ind]);
            }
        }
        imageOut.setPixels(out);
    }

    @Override
    protected void applyRenderScript() {
        int[] img1 = new int[this.imageSrc.getWidth() * imageSrc.getHeight()];
        int ind;
        int[] pixels = imageSrc.getPixels();
        for (int y = 0; y < imageSrc.getHeight(); y++) {
            for (int x = 0; x < imageSrc.getWidth(); x++) {
                ind = x + y * imageSrc.getWidth();
                int grey = ColorTools.getGreyColor(pixels[ind]);
                img1[ind] = Color.argb(255, grey, grey, grey);
            }
        }
        imageOut.setPixels(img1);
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, convo.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Sketch SketchScript = new ScriptC_Sketch(rs);

        SketchScript.set_in(input);
        SketchScript.set_height(super.getImageSrc().getHeight());
        SketchScript.set_width(super.getImageSrc().getWidth());
        Allocation matrixAlloc = Allocation.createSized(rs, Element.F32(rs), convo.getLength() * convo.getLength());
        matrixAlloc.copyFrom(convo.getMatrix().getType().generate(convo.getLength()));
        SketchScript.bind_matrix(matrixAlloc);
        SketchScript.set_matrixSize(convo.getLength());

        SketchScript.forEach_Sketch(output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        SketchScript.destroy();
        rs.destroy();
    }

}
