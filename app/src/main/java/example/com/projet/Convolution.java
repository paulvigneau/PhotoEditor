package example.com.projet;

public class Convolution extends Filter {

    private Matrix matrix;

    public Convolution(MainActivity main, Image image) {
        super(main, image);
    }

    @Override
    public void apply() {

    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
