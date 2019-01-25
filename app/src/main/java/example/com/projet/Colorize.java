package example.com.projet;

public class Colorize extends Filter {

    private int hue;
    private boolean isRandom;

    public Colorize(Image image) {
        super(image);
        this.hue = 0;
        this.isRandom = true;
    }

    @Override
    public void apply() {

    }

    public void setHue(int hue){
        this.hue = hue;
    }

    public void setRandom(boolean isRandom){
        this.isRandom = isRandom;
    }

    public int getHue(){
        return this.hue;
    }

    public boolean isRandom(){
        return this.isRandom;
    }
}
