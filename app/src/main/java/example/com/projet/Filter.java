package example.com.projet;

public abstract class Filter {
    protected MainActivity main;
    protected Image imageSrc;
    protected Image imageOut;

    public Filter(MainActivity main, Image image) {
        this.main = main;

        this.imageSrc = image;
        this.imageOut = new Image(image);
    }

    public abstract void apply();

    public Image getImageSrc(){
        return this.imageSrc;
    }

    public Image getImageOut(){
        return this.imageOut;
    }
}
