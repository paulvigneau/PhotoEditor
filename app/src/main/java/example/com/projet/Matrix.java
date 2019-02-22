package example.com.projet;

public enum Matrix {

    GAUSSIAN (new IMatrix() {
        @Override
        public float[] generate(int length) {
            int n =(length-1)/2;
            float [] matrix = new float[length * length];
            double sigma =0.25;
            for(int y=-n;y<=n;y++) {
                for (int x = -n; x <= n; x++) {
                    matrix[x +y * length] = (float) (Math.exp(-((x*x+y*y)/(2*sigma*sigma)))/(sigma * Math.sqrt(2*Math.PI)));
                }
            }
            return matrix;
        }
    }),

    AVERAGING (new IMatrix() {
        @Override
        public float[] generate(int length) {
            int size = length * length;
            float[] matrix = new float[size];
            for(int i=0; i< size;i++){
                matrix[i]= 1/size;
            }
            return matrix;
        }
    }),

    LAPLACIAN (new IMatrix() {
        @Override
        public float[] generate(int length) {
            return new float[]{0.f,1.f,0.f,1.f,-4.f,1.f,0.f,1.f,0.f};
      }
    }),

    SOBEL (new IMatrix() {
        @Override
        public float[] generate(int length) {
            return create(2,length);
        }
    }),

    PREWITT (new IMatrix() {
        @Override
        public float[] generate(int length) {
            return create(1,length);
        }
    });

    private static float[] create(int k, int length){
        float[] matrix = new float[length* length];
        float[]A = new float[]{-1,0,1};
        float[]B = new float[]{1,k,1};
        for(int y=0; y< length;y++){
            for(int x=0;x<length;x++){
                matrix[x+y*length]=A[y]*B[x];
            }
        }
        return matrix;
    }
    IMatrix type;

    Matrix(IMatrix type){
        this.type = type;
    }
}
