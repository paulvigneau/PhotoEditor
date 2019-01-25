package example.com.projet.lut;

public enum LUT {
    CONTRAST_LINEAR(new ILUT() {
        @Override
        public int[] getLUT(int min, int max) {
            int[] lut = new int[256];

            for(int index=0; index<256; index++){
                lut[index] = (255 * (index - min))/(max - min);
            }
            return lut;
        }
    });

    private ILUT iLut;

    LUT(ILUT iLut){
        this.iLut = iLut;
    }

    public int[] getLUT(int min, int max){
        return this.iLut.getLUT(min, max);
    }
}
