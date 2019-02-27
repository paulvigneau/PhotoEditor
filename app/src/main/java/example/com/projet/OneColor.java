package example.com.projet;

import android.graphics.Color;

public class OneColor extends Filter {

    private Color color;
    private int threshold;

    public OneColor(MainActivity main, Image image, Color color, int threshold) {
        super(main, image);
        this.color = color;
        this.threshold = threshold;
    }

    @Override
    public void apply() {

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
