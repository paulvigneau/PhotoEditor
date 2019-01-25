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
}
