package example.com.projet;

import android.graphics.Color;

public class OneColor extends Filter {

    private int color;
    private int threshold;

    public OneColor(MainActivity main, Image image) {
        super(main, image);
        this.color = 0;
        this.threshold = 0;
    }

    @Override
    public void apply() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
