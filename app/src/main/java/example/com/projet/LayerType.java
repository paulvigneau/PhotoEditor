package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public enum LayerType {
    BRIGHTNESS(new ILayerType() {
        @Override
        public void setInflacter(final MainActivity main) {
            main.InflateLayer(R.layout.brightness_layout, R.id.optionID);

            updateText(main, R.id.brightness_value, R.id.brightness_value_text);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Brightness(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Brightness brightness = (Brightness)main.layerFilter;

            //option
            brightness.setIntensity(getSeekBarProgress(main, R.id.brightness_value));

            brightness.apply();
        }
    }),
    CONTRAST(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.contrast_layout, R.id.optionID);

            updateText(main, R.id.contrast_value, R.id.contrast_text);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Contrast(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Contrast contrast = (Contrast)main.layerFilter;

            //option
            int level = getSeekBarProgress(main, R.id.contrast_value);
            contrast.setIntensity(level);

            contrast.apply();
        }
    }),
    EQUALIZE(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.equalize_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Equalize(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Equalize equalize = (Equalize)main.layerFilter;


            equalize.apply();
        }
    }),
    COLORISE(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.colorise_layout, R.id.optionID);

            main.setImage(R.drawable.hue, R.id.hue_imageID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Colorize(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Colorize colorize = (Colorize)main.layerFilter;

            //option
            colorize.setHue(getSeekBarProgress(main, R.id.hue_value));

            colorize.apply();
        }
    }),
    ONE_COLOR(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.one_color_layout, R.id.optionID);

            main.setImage(R.drawable.white, R.id.color_view);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new OneColor(main, main.image);

            updateText(main, R.id.max_distance_value, R.id.distance_text);

            updateColorView(main, R.id.color_view, R.id.contrast_value, R.id.green_value, R.id.blue_value);


        }

        @Override
        public void applyLayer(MainActivity main) {
            OneColor oneColor = (OneColor) main.layerFilter;

            int red = getSeekBarProgress(main, R.id.contrast_value);
            int green = getSeekBarProgress(main, R.id.green_value);
            int blue = getSeekBarProgress(main, R.id.blue_value);
            oneColor.setColor(Color.argb(255, red, green, blue));
            oneColor.setThreshold(getSeekBarProgress(main, R.id.max_distance_value));

            oneColor.apply();
        }
    }),
    BLURRING(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.blurring_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Convolution(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Convolution convolution = (Convolution)main.layerFilter;

            //option
            convolution.setMatrix(Matrix.PREWITT);
            convolution.setLength(3);

            convolution.apply();
        }
    }),
    CONTOUR(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.blurring_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Convolution(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Convolution convolution = (Convolution)main.layerFilter;

            //option
            convolution.setMatrix(Matrix.LAPLACIAN);

            convolution.apply();
        }
    });

    private ILayerType type;

    LayerType(ILayerType type){
        this.type = type;
    }

    public ILayerType getType(){
        return this.type;
    }


    private static int getSeekBarProgress(MainActivity main, int id){
        SeekBar bar = (SeekBar)main.findViewById(id);
        return bar.getProgress();
    }

    private static void updateText(final MainActivity main, int seekBarID, final int textViewID){
        SeekBar bar = (SeekBar)main.findViewById(seekBarID);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView text = (TextView)main.findViewById(textViewID);

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

    private static void updateColorView(final MainActivity main,final int img, int red, int green, int blue){
        final SeekBar redBar = (SeekBar)main.findViewById(red);
        final SeekBar blueBar = (SeekBar)main.findViewById(blue);
        final SeekBar greenBar = (SeekBar)main.findViewById(green);

        SeekBar.OnSeekBarChangeListener event = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ImageView imgView = (ImageView)main.findViewById(img);

                Bitmap map = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                int R = redBar.getProgress();
                int G = greenBar.getProgress();
                int B = blueBar.getProgress();

                int[] pixels = new int[map.getHeight() * map.getWidth()];

                for(int index = 0; index < pixels.length; index++){
                    pixels[index] = Color.argb(255, R, G, B);
                }
                map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());
                imgView.setImageBitmap(map);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        };

        redBar.setOnSeekBarChangeListener(event);
        greenBar.setOnSeekBarChangeListener(event);
        blueBar.setOnSeekBarChangeListener(event);
    }
}
