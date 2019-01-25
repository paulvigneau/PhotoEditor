package example.com.projet;

public abstract class Filter {

    Image imageSrc;
    Image imageOut;

    public Filter(Image image) {
        this.imageSrc = image;
    }

    public abstract void apply();
}
