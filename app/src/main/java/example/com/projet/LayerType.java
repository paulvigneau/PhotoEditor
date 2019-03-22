package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public enum LayerType {
    BRIGHTNESS(new ILayerType() {
        @Override
        public void setInflacter(final MainActivity main) {
            main.InflateLayer(R.layout.brightness_layout, R.id.optionID);

            updateText(main, R.id.brightness_value, R.id.brightness_value_text, false);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Brightness(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Brightness brightness = (Brightness) main.layerFilter;

            //option
            brightness.setIntensity(getSeekBarProgress(main, R.id.brightness_value, false));

            brightness.apply();
        }
    }),
    CONTRAST(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.contrast_layout, R.id.optionID);

            updateText(main, R.id.contrast_value, R.id.contrast_text, false);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Contrast(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Contrast contrast = (Contrast) main.layerFilter;

            //option
            int level = getSeekBarProgress(main, R.id.contrast_value, false);
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
            Equalize equalize = (Equalize) main.layerFilter;


            equalize.apply();
        }
    }),

    COLORIZE(new ILayerType() {
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
            Colorize colorize = (Colorize) main.layerFilter;

            //option
            if (getCheckBoxSelect(main, R.id.colorise_random)) {
                colorize.setHue((int) (Math.random() * 255));
            } else {
                colorize.setHue(getSeekBarProgress(main, R.id.hue_value, false));
            }

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

            updateText(main, R.id.max_distance_value, R.id.distance_text, false);

            updateColorView(main, R.id.color_view, R.id.contrast_value, R.id.green_value, R.id.blue_value);


        }

        @Override
        public void applyLayer(MainActivity main) {
            OneColor oneColor = (OneColor) main.layerFilter;

            if (getCheckBoxSelect(main, R.id.one_color_random)) {
                int red = (int) (Math.random() * 255);
                int green = (int) (Math.random() * 255);
                int blue = (int) (Math.random() * 255);
                oneColor.setColor(Color.argb(255, red, green, blue));
            } else {
                int red = getSeekBarProgress(main, R.id.contrast_value, false);
                int green = getSeekBarProgress(main, R.id.green_value, false);
                int blue = getSeekBarProgress(main, R.id.blue_value, false);
                oneColor.setColor(Color.argb(255, red, green, blue));
            }

            oneColor.setThreshold(getSeekBarProgress(main, R.id.max_distance_value, false));

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

            updateText(main, R.id.blurring_size, R.id.blurring_text, true);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Convolution convolution = (Convolution) main.layerFilter;

            //option
            switch (getSpinnerIndex(main, R.id.blurring_menu)) {
                case 0:
                    convolution.setMatrix(Matrix.AVERAGING);
                    break;
                case 1:
                    convolution.setMatrix(Matrix.GAUSSIAN);
                    break;
                default:
                    break;
            }
            convolution.setLength(getSeekBarProgress(main, R.id.blurring_size, true));


            convolution.apply();
        }
    }),
    CONTOUR(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.contour_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Convolution(main, main.image);

            updateText(main, R.id.contour_size, R.id.contour_text, true);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Convolution convolution = (Convolution) main.layerFilter;

            //option
            switch (getSpinnerIndex(main, R.id.contour_menu)) {
                case 0:
                    convolution.setMatrix(Matrix.SOBEL);
                    break;
                case 1:
                    convolution.setMatrix(Matrix.PREWITT);
                    break;
                case 2:
                    convolution.setMatrix(Matrix.LAPLACIAN);
                    break;
                default:
                    break;
            }
            convolution.setLength(3);

            convolution.apply();
        }
    }),

    SKETCH(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.sketch_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Sketch(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Sketch sketch = (Sketch) main.layerFilter;
            sketch.apply();
        }
    }),
    GREY(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.grey_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Grey(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Grey grey = (Grey) main.layerFilter;
            grey.apply();
        }
    }),
    INVERT(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(R.layout.grey_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Invert(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Invert invert = (Invert) main.layerFilter;
            invert.apply();
        }
    }),
    SEPIA(new ILayerType() {
        @Override
        public void setInflacter(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Sepia(main, main.image);
        }

        @Override
        public void applyLayer(MainActivity main) {
            Sepia sepia = (Sepia) main.layerFilter;
            sepia.apply();
        }
    });

    private ILayerType type;

    LayerType(ILayerType type) {
        this.type = type;
    }

    public ILayerType getType() {
        return this.type;
    }

    private static int getSpinnerIndex(MainActivity main, int id) {
        Spinner menu = (Spinner) main.findViewById(id);
        return menu.getSelectedItemPosition();
    }

    private static boolean getCheckBoxSelect(MainActivity main, int id) {
        CheckBox button = (CheckBox) main.findViewById(id);
        return button.isChecked();
    }

    private static int getSeekBarProgress(MainActivity main, int id, boolean onlyImpair) {
        SeekBar bar = (SeekBar) main.findViewById(id);
        if (onlyImpair) {
            return bar.getProgress() * 2 + 1;
        } else {
            return bar.getProgress();
        }
    }

    private static void updateText(final MainActivity main, int seekBarID, final int textViewID, final boolean onlyImpair) {
        SeekBar bar = (SeekBar) main.findViewById(seekBarID);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView text = (TextView) main.findViewById(textViewID);

                if (onlyImpair)
                    text.setText("" + (i * 2 + 1));
                else
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

    private static void updateColorView(final MainActivity main, final int img, int red, int green, int blue) {
        final SeekBar redBar = (SeekBar) main.findViewById(red);
        final SeekBar blueBar = (SeekBar) main.findViewById(blue);
        final SeekBar greenBar = (SeekBar) main.findViewById(green);

        SeekBar.OnSeekBarChangeListener event = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ImageView imgView = (ImageView) main.findViewById(img);

                Bitmap map = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                int R = redBar.getProgress();
                int G = greenBar.getProgress();
                int B = blueBar.getProgress();

                int[] pixels = new int[map.getHeight() * map.getWidth()];

                for (int index = 0; index < pixels.length; index++) {
                    pixels[index] = Color.argb(255, R, G, B);
                }
                map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());
                imgView.setImageBitmap(map);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        redBar.setOnSeekBarChangeListener(event);
        greenBar.setOnSeekBarChangeListener(event);
        blueBar.setOnSeekBarChangeListener(event);
    }
}
