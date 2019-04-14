package example.com.projet.filter;

import android.graphics.Color;

import example.com.projet.Image;
import example.com.projet.MainActivity;
import example.com.projet.Matrix;
import example.com.projet.utils.ColorTools;

/**
 * Represent the cartoon filter
 */
public class Cartoon extends Filter {

    /**
     * The cartoon constructor
     *
     * @param main
     *      The main activity
     * @param image
     *      The source image
     */
    public Cartoon(MainActivity main, Image image) {
        super(main, image);
    }

    /**
     *
     *
     * @param nbColor
     * @return
     */
    public int[] createReducLUT(int nbColor) {
        int[] LUT = new int[256];
        int interval = 256 / nbColor;
        int threshold = interval;
        int newValue = interval / 2;
        for (int ind = 0; ind < 256; ind++) {
            LUT[ind] = newValue;
            if (ind == threshold) {
                threshold += interval;
                newValue += interval;
            }
        }
        return LUT;
    }

    @Override
    protected void applyJava() {
        Runtime.getRuntime().gc();
        int[] pixels = imageSrc.getPixels();
        int[] tabgrey = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        int[] out = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        // out : reduce nb of colors
        // greytab = grey scale
        int[] LUT = createReducLUT(5);
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {

            int red = LUT[Color.red(pixels[i])];
            int green = LUT[Color.green(pixels[i])];
            int blue = LUT[Color.blue((pixels[i]))];

            out[i] = Color.argb(Color.alpha(pixels[i]), red, green, blue);
            int grey = ColorTools.getGreyColor(pixels[i]);
            tabgrey[i] = Color.argb(Color.alpha(pixels[i]), grey, grey, grey);
        }
        // edge of the gray scale in greyTab
        imageOut.setPixels(tabgrey);
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        convo.apply(true);
        tabgrey = convo.getImageOut().getPixels();
        int[] newTab = new int[imageSrc.getWidth() * imageSrc.getHeight()];

        //epaississement des contours
        for (int y = 2; y < imageSrc.getHeight() - 2; y++) {
            for (int x = 2; x < imageSrc.getWidth() - 2; x++) {
                int i = x + y * imageSrc.getWidth();
                int red = Color.red(tabgrey[i]);
                int green = Color.green(tabgrey[i]);
                int blue = Color.blue((tabgrey[i]));
                if (red > 127 && green > 127 && blue > 127) {

                    newTab[i] = tabgrey[i];
                    newTab[i - 1] = tabgrey[i];
                    newTab[i + 1] = tabgrey[i];

                    newTab[i - 1 - imageSrc.getWidth()] = tabgrey[i];
                    newTab[i + 1 - imageSrc.getWidth()] = tabgrey[i];
                    newTab[i - imageSrc.getWidth()] = tabgrey[i];

                    newTab[i - 1 + imageSrc.getWidth()] = tabgrey[i];
                    newTab[i + 1 + imageSrc.getWidth()] = tabgrey[i];
                    newTab[i + imageSrc.getWidth()] = tabgrey[i];

                } else {
                    newTab[i] = tabgrey[i];
                }
            }
        }
        tabgrey = newTab;
        // threasold image to binary in greytab
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {
            int red = Color.red(tabgrey[i]);
            int green = Color.green(tabgrey[i]);
            int blue = Color.blue((tabgrey[i]));
            if (red < 127 && green < 127 && blue < 127) {
                red = Color.red(out[i]);
                green = Color.green(out[i]);
                blue = Color.blue(out[i]);
            } else {
                red = 0;
                green = 0;
                blue = 0;
            }
            out[i] = Color.argb(Color.alpha(out[i]), red, green, blue);
        }
        imageOut.setPixels(out);
    }

    @Override
    protected void applyRenderScript() {
        main.showMessage("Not avaible in RenderScript");
        /*Convolution convo = new Convolution(super.main, this.imageSrc);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        RenderScript rs = RenderScript.create(super.main);

        Allocation input = Allocation.createFromBitmap(rs, convo.imageSrc.getBitmap());
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Cartoon CartoonScript = new ScriptC_Cartoon(rs);

        CartoonScript.set_in(input);
        CartoonScript.set_height(super.getImageSrc().getHeight());
        CartoonScript.set_width(super.getImageSrc().getWidth());
        Allocation matrixAlloc = Allocation.createSized(rs, Element.F32(rs), convo.getLength() * convo.getLength());
        matrixAlloc.copyFrom(convo.getMatrix().getType().generate(convo.getLength()));
        CartoonScript.bind_matrix(matrixAlloc);
        CartoonScript.set_matrixSize(convo.getLength());

        CartoonScript.forEach_Cartoon(output);

        Bitmap out = super.imageOut.getBitmap();
        output.copyTo(out);
        super.imageOut.setBitmap(out);

        input.destroy();
        output.destroy();

        CartoonScript.destroy();
        rs.destroy();*/
    }
}
