package example.com.projet;

public class Equalize extends Filter {

    private Histogram histogramSrc;

    public Equalize(Image image) {
        super(image);
    }

    @Override
    public void apply() {

    }

    public Histogram getHistogramSrc() {
        return histogramSrc;
    }

    public void setHistogramSrc(Histogram histogramSrc) {
        this.histogramSrc = histogramSrc;
    }
}
