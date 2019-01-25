package example.com.projet;

public enum Matrix {

    GAUSSIAN (new IMatrix() {
        @Override
        public void generate(int length) {

        }
    }),

    AVERAGING (new IMatrix() {
        @Override
        public void generate(int length) {

        }
    }),

    LAPLACIAN (new IMatrix() {
        @Override
        public void generate(int length) {

        }
    }),

    SOBEL (new IMatrix() {
        @Override
        public void generate(int length) {

        }
    }),

    PREWITT (new IMatrix() {
        @Override
        public void generate(int length) {

        }
    });

    IMatrix type;

    Matrix(IMatrix type){
        this.type = type;
    }
}
