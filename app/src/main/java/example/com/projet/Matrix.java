package example.com.projet;

public enum Matrix {

    GAUSSIAN(new IMatrix() {
        @Override
        public float[] generate(int length) {
            int n = length / 2;

            float[] matrix = new float[length * length];

            double sigma = length / 2.35;
            float sum = 0;

            for (int y = 0; y < length; y++) {
                for (int x = 0; x < length; x++) {
                    matrix[x + y * length] = (float) (Math.exp(-(((x - n) * (x - n) + (y - n) * (y - n)) / (2 * sigma * sigma))) / (Math.sqrt(2 * Math.PI * sigma * sigma)));
                    sum += matrix[x + y * length];
                    System.out.println(matrix[x+y*length]);
                }
            }
            for (int ind = 0; ind < length * length; ind++) {
                matrix[ind] /= sum;
            }
            return matrix;
        }
    }),

    AVERAGING(new IMatrix() {
        @Override
        public float[] generate(int length) {
            int size = length * length;
            float[] matrix = new float[size];

            for (int i = 0; i < size; i++) {
                matrix[i] = 1.0f / size;
            }

            return matrix;
        }
    }),

    LAPLACIAN(new IMatrix() {
        @Override
        public float[] generate(int length) {
            return new float[]{0.f, -1.f, 0.f, -1.f, 4.f, -1.f, 0.f, -1.f, 0.f};
        }
    }),

    SOBEL(new IMatrix() {
        @Override
        public float[] generate(int length) {
            return create(2, length);
        }
    }),

    PREWITT(new IMatrix() {
        @Override
        public float[] generate(int length) {
            return create(1, length);
        }
    }),
    SHARPEN(new IMatrix() {
        @Override
        public float[] generate(int length) {
            return new float[]{0.f, -1.f, 0.f, -1.f, 5.f, -1.f, 0.f, -1.f, 0.f};

        }
    }),
    EMBOSS(new IMatrix() {
        @Override
        public float[] generate(int length) {
            return new float[]{-1.f, -1.f, 0.f, -1.f, 0.f, 1.f, 0.f, 1.f, 1.f};

        }
    });

    private static float[] create(int k, int length) {
        float[] matrix = new float[length * length];
        float[] A = new float[]{-1, 0, 1};
        float[] B = new float[]{1, k, 1};
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                matrix[x + y * length] = A[y] * B[x];
            }
        }
        return matrix;
    }

    private IMatrix type;

    Matrix(IMatrix type) {
        this.type = type;
    }

    public IMatrix getType() {
        return this.type;
    }
}
