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
     * Create a LUT that contain the value that is associate to each 256 existing values of a color in a reduce palette of colors.
     *
     * @param nbColor
     *      The number of value accept
     * @return the new LUT
     */
    private int[] createReduceLUT(int nbColor) {
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
        int[] greyTab = new int[imageSrc.getHeight() * imageSrc.getWidth()];
        int[] reduce = new int[imageSrc.getHeight() * imageSrc.getWidth()];

        // reduce : reduce color picture
        // greyTab = grey scale picture

        int[] LUT = createReduceLUT(5);
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {

            int red = LUT[Color.red(pixels[i])];
            int green = LUT[Color.green(pixels[i])];
            int blue = LUT[Color.blue((pixels[i]))];

            reduce[i] = Color.argb(Color.alpha(pixels[i]), red, green, blue);
            int grey = ColorTools.getGreyColor(pixels[i]);
            greyTab[i] = Color.argb(Color.alpha(pixels[i]), grey, grey, grey);
        }

        //apply a Sobel filter on the grey scale picture
        imageOut.setPixels(greyTab);
        Convolution convo = new Convolution(super.main, this.imageOut);
        convo.setMatrix(Matrix.SOBEL);
        convo.setLength(3);
        convo.apply(true);
        greyTab = convo.getImageOut().getPixels();
        int[] newTab = new int[imageSrc.getWidth() * imageSrc.getHeight()];

        //thickens the contours
        for (int y = 2; y < imageSrc.getHeight() - 2; y++) {
            for (int x = 2; x < imageSrc.getWidth() - 2; x++) {
                int i = x + y * imageSrc.getWidth();
                int red = Color.red(greyTab[i]);
                int green = Color.green(greyTab[i]);
                int blue = Color.blue((greyTab[i]));
                if (red > 127 && green > 127 && blue > 127) {

                    newTab[i] = greyTab[i];
                    newTab[i - 1] = greyTab[i];
                    newTab[i + 1] = greyTab[i];

                    newTab[i - 1 - imageSrc.getWidth()] = greyTab[i];
                    newTab[i + 1 - imageSrc.getWidth()] = greyTab[i];
                    newTab[i - imageSrc.getWidth()] = greyTab[i];

                    newTab[i - 1 + imageSrc.getWidth()] = greyTab[i];
                    newTab[i + 1 + imageSrc.getWidth()] = greyTab[i];
                    newTab[i + imageSrc.getWidth()] = greyTab[i];

                } else {
                    newTab[i] = greyTab[i];
                }
            }
        }
        greyTab = newTab;
        // determined if it's an edge or not
        for (int i = 0; i < imageSrc.getHeight() * imageSrc.getWidth(); i++) {
            int red = Color.red(greyTab[i]);
            int green = Color.green(greyTab[i]);
            int blue = Color.blue((greyTab[i]));
            //not an edge then put the color reduced picture
            if (red < 127 && green < 127 && blue < 127) {
                red = Color.red(reduce[i]);
                green = Color.green(reduce[i]);
                blue = Color.blue(reduce[i]);
             // it's an edge and put a black pixel
            } else {
                red = 0;
                green = 0;
                blue = 0;
            }
            reduce[i] = Color.argb(Color.alpha(reduce[i]), red, green, blue);
        }
        imageOut.setPixels(reduce);
    }

    @Override
    protected void applyRenderScript() {
        main.showMessage("Not available in RenderScript");

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
