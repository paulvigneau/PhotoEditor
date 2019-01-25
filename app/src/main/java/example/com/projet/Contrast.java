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

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Histogram getHistogramSrc() {
        return histogramSrc;
    }

    public void setHistogramSrc(Histogram histogramSrc) {
        this.histogramSrc = histogramSrc;
    }
}
