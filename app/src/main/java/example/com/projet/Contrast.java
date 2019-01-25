package example.com.projet;

public class Contrast extends Filter {

    private int interval;
    private Histogram histogramSrc;


    public Contrast(Image image, int interval) {
        super(image);
        this.interval = interval;
    }

    @Override
    public void apply() {

    }
}
