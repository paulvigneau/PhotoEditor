package example.com.projet;

public class Convolution extends Filter {

    private Matrix matrix;

    public Convolution(Image image) {
        super(image);
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
