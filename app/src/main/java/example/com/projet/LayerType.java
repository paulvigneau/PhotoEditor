package example.com.projet;

import android.widget.SeekBar;
import android.widget.TextView;

public enum LayerType {
    BRIGHTNESS(new ILayerType() {
        @Override
        public void setInflacter(final MainActivity main) {
            main.InflateLayer(R.layout.brightness_layout, R.id.optionID);

            SeekBar bar = (SeekBar) main.findViewById(R.id.brightness_value);
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    TextView text = (TextView)main.findViewById(R.id.brightness_value_text);
                    text.setText("" + i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    return;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    return;
                }
            });
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Brightness(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Brightness brightness = (Brightness)main.layerFilter;

            //option
            SeekBar value = (SeekBar) main.findViewById(R.id.brightness_value);
            brightness.setIntensity(value.getProgress());

            brightness.apply();
        }
    }),
    CONTRAST(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.contrast_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Contrast(main, main.image, new int[]{0,0});
        }

        @Override
        public void applyLayer(MainActivity main) {
            /*Contrast contrast = (Contrast)main.layerFilter;

            //option
            SeekBar value = (SeekBar) main.findViewById(R.id.brightness_value);
            contrast.setIntensity(value.getProgress());

            contrast.apply();*/
        }
    }),
    EQUALIZE(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.contrast_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {

        }

        @Override
        public void applyLayer(MainActivity main) {

        }
    }),
    COLORISE(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.colorise_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Colorize(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Colorize colorize = (Colorize)main.layerFilter;

            //option
            //SeekBar value = (SeekBar) main.findViewById(R.id.brightness_value);
            //colorize.setIntensity(value.getProgress());

            colorize.apply();
        }
    }),
    ONE_COLOR(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.one_color_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new OneColor(main, main.image,0,0);
        }

        @Override
        public void applyLayer(MainActivity main) {
            OneColor oneColor = (OneColor) main.layerFilter;


            oneColor.apply();
        }
    }),
    BLURRING(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {

        }

        @Override
        public void generateLayer(MainActivity main) {

        }

        @Override
        public void applyLayer(MainActivity main) {

        }
    }),
    CONTOUR(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {

        }

        @Override
        public void generateLayer(MainActivity main) {

        }

        @Override
        public void applyLayer(MainActivity main) {

        }
    });

    private ILayerType type;

    LayerType(ILayerType type){
        this.type = type;
    }

    public ILayerType getType(){
        return this.type;
    }
}
