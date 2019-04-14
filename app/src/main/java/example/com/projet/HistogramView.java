package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import example.com.projet.utils.ColorType;

/**
 * Represent the histogram view
 */
public class HistogramView {
    private static final int NB_COLOR = 256;
    private static final int SIZE_X = 256;
    private static final int SIZE_Y = 200;

    private boolean showRed;
    private boolean showGreen;
    private boolean showBlue;
    private ImageView image;

    /**
     * The histogram constructor
     *
     * @param image
     *      The source image
     */
    public HistogramView(ImageView image){
        this.image = image;

        this.showRed = true;
        this.showGreen = true;
        this.showBlue = true;
    }

    /**
     * Generate the histogram image in the source image
     *
     * @param source
     *      The source image.
     */
    public void applyFilter(Image source) {
        int[] pixels = new int[SIZE_X * SIZE_Y];

        int[] redValues = Histogram.createHistogram(source, ColorType.RED);
        int[] greenValues = Histogram.createHistogram(source, ColorType.GREEN);
        int[] blueValues = Histogram.createHistogram(source, ColorType.BLUE);

        int maxValue = 0;
        for (int index = 1; index < NB_COLOR-1; index++) {
            maxValue = Math.max(maxValue, redValues[index]);
            maxValue = Math.max(maxValue, greenValues[index]);
            maxValue = Math.max(maxValue, blueValues[index]);
        }

        for (int index = 0; index < NB_COLOR; index++) {
            int redValue = (SIZE_Y * redValues[index]) / maxValue;
            int greenValue = (SIZE_Y * greenValues[index]) / maxValue;
            int blueValue = (SIZE_Y * blueValues[index]) / maxValue;

            for (int y = 0; y < SIZE_Y; y++) {
                int red = 50, green = 50, blue = 50;
                if (showRed && redValue > SIZE_Y-y)
                    red = 255;
                if (showGreen && greenValue > SIZE_Y-y)
                    green = 255;
                if (showBlue & blueValue > SIZE_Y-y)
                    blue = 255;

                pixels[index + y * SIZE_X] = Color.argb(255, red, green, blue);
            }
        }

        Bitmap map = Bitmap.createBitmap(pixels, SIZE_X, SIZE_Y, Bitmap.Config.ARGB_8888);
        this.image.setImageBitmap(map);
    }
}
