package example.com.projet;

public abstract class Filter {

    protected Image imageSrc;
    protected Image imageOut;

    public Filter(Image image) {
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
