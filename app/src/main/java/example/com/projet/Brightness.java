package example.com.projet;

public class Brightness extends Filter {

    private int intensity;

    public Brightness(Image image, int intensity) {
        super(image);
        this.intensity = intensity;
    }

    @Override
    public void apply() {

    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
