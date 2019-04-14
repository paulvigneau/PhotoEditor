package example.com.projet;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import example.com.projet.filter.Brightness;
import example.com.projet.filter.Cartoon;
import example.com.projet.filter.Colorize;
import example.com.projet.filter.Contrast;
import example.com.projet.filter.Convolution;
import example.com.projet.filter.Equalize;
import example.com.projet.filter.FaceDetection;
import example.com.projet.filter.Grey;
import example.com.projet.filter.Invert;
import example.com.projet.filter.Matrix;
import example.com.projet.filter.Mirror;
import example.com.projet.filter.OneColor;
import example.com.projet.filter.Pixelated;
import example.com.projet.filter.Replace;
import example.com.projet.filter.Saturation;
import example.com.projet.filter.Sepia;
import example.com.projet.filter.Sketch;

/**
 * Represent the enumerate filter
 */
public enum LayerType {
    BRIGHTNESS("Brightness", R.id.ButtonBrightness, new ILayerType() {
        @Override
        public void setInflactor(final MainActivity main) {
            main.InflateLayer(R.layout.brightness_layout, R.id.optionID);

            updateText(main, R.id.brightness_value, R.id.brightness_value_text, false, -50);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Brightness(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Brightness brightness = (Brightness) main.layerFilter;

            //option
            brightness.setIntensity(getSeekBarProgress(main, R.id.brightness_value, false));

            brightness.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    SATURATION("Saturation", R.id.ButtonSaturation, new ILayerType() {
        @Override
        public void setInflactor(final MainActivity main) {
            main.InflateLayer(R.layout.saturation_layout, R.id.optionID);

            updateText(main, R.id.saturation_value, R.id.saturation_value_text, false, -50);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Saturation(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {

            Saturation saturation = (Saturation) main.layerFilter;

            //option
            saturation.setIntensity(getSeekBarProgress(main, R.id.saturation_value, false));

            saturation.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    CONTRAST("Contrast", R.id.ButtonContrast, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Contrast(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Contrast contrast = (Contrast) main.layerFilter;

            //option
            contrast.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    EQUALIZE("Equalize", R.id.ButtonEqualize, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Equalize(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Equalize equalize = (Equalize) main.layerFilter;


            equalize.apply(inRenderScript);
            main.setApplyImage();
        }
    }),

    COLORIZE("Colorize", R.id.ButtonColorize, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.colorise_layout, R.id.optionID);

            main.setImage(R.drawable.hue, R.id.hue_imageID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Colorize(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Colorize colorize = (Colorize) main.layerFilter;

            //option
            if (getCheckBoxSelect(main, R.id.colorise_random)) {
                colorize.setHue((int) (Math.random() * 255));
            } else {
                colorize.setHue(getSeekBarProgress(main, R.id.hue_value, false));
            }

            colorize.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    ONE_COLOR("One Color", R.id.ButtonOneColor, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.one_color_layout, R.id.optionID);

            main.setImage(R.drawable.white, R.id.color_view);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new OneColor(main, main.applyImage);

            updateText(main, R.id.max_distance_value, R.id.distance_text, false, 0);

            updateColorView(main, R.id.color_view, R.id.contrast_value, R.id.from_green_value, R.id.from_blue_value);

        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            OneColor oneColor = (OneColor) main.layerFilter;

            if (getCheckBoxSelect(main, R.id.one_color_random)) {
                int red = (int) (Math.random() * 255);
                int green = (int) (Math.random() * 255);
                int blue = (int) (Math.random() * 255);
                oneColor.setColor(Color.argb(255, red, green, blue));
            } else {
                int red = getSeekBarProgress(main, R.id.contrast_value, false);
                int green = getSeekBarProgress(main, R.id.from_green_value, false);
                int blue = getSeekBarProgress(main, R.id.from_blue_value, false);
                oneColor.setColor(Color.argb(255, red, green, blue));
            }

            oneColor.setThreshold(getSeekBarProgress(main, R.id.max_distance_value, false));

            oneColor.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    REPLACE("Replace", R.id.ButtonReplace, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.replace_color_layout, R.id.optionID);

            main.setImage(R.drawable.white, R.id.iconFrom);
            main.setImage(R.drawable.white, R.id.iconTo);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Replace(main, main.applyImage);

            updateText(main, R.id.max_distance_value, R.id.distance_text, false, 0);

            updateColorView(main, R.id.iconFrom, R.id.from_red_value, R.id.from_green_value, R.id.from_blue_value);
            updateColorView(main, R.id.iconTo, R.id.to_red_value, R.id.to_green_value, R.id.to_blue_value);

            final View fromView = main.findViewById(R.id.fromLayout);
            final View toView = main.findViewById(R.id.toLayout);

            TabLayout tab = (TabLayout) main.findViewById(R.id.replaceTab);
            tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0){
                        fromView.setVisibility(View.VISIBLE);
                        toView.setVisibility(View.INVISIBLE);
                    }else{
                        fromView.setVisibility(View.INVISIBLE);
                        toView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}
                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Replace replace = (Replace) main.layerFilter;

            if (getCheckBoxSelect(main, R.id.replace_color_random)) {
                int red = (int) (Math.random() * 255);
                int green = (int) (Math.random() * 255);
                int blue = (int) (Math.random() * 255);
                replace.setReplaceColor(Color.argb(255, red, green, blue));
            } else {
                int red = getSeekBarProgress(main, R.id.to_red_value, false);
                int green = getSeekBarProgress(main, R.id.to_green_value, false);
                int blue = getSeekBarProgress(main, R.id.to_blue_value, false);
                replace.setReplaceColor(Color.argb(255, red, green, blue));
            }

            int red = getSeekBarProgress(main, R.id.from_red_value, false);
            int green = getSeekBarProgress(main, R.id.from_green_value, false);
            int blue = getSeekBarProgress(main, R.id.from_blue_value, false);
            replace.setColor(Color.argb(255, red, green, blue));

            replace.setThreshold(getSeekBarProgress(main, R.id.max_distance_value, false));

            replace.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    BLURRING("Blurring", R.id.ButtonBlur, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.blurring_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(final MainActivity main) {
            main.layerFilter = new Convolution(main, main.applyImage);

            updateText(main, R.id.blurring_size, R.id.blurring_text, true, 0);

            Spinner spinner = (Spinner)main.findViewById(R.id.blurring_menu);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    View sizeView = main.findViewById(R.id.sizeLayout);
                    if(i == 2){
                        sizeView.setVisibility(View.INVISIBLE);
                    }else{
                        sizeView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Convolution convolution = (Convolution) main.layerFilter;

            //option
            switch (getSpinnerIndex(main, R.id.blurring_menu)) {
                case 0:
                    convolution.setMatrix(Matrix.AVERAGING);
                    break;
                case 1:
                    convolution.setMatrix(Matrix.GAUSSIAN);
                    break;
                case 2:
                    convolution.setMatrix((Matrix.SHARPEN));
                    break;
                default:
                    break;
            }
            convolution.setLength(getSeekBarProgress(main, R.id.blurring_size, true));


            convolution.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    CONTOUR("Contour", R.id.ButtonContour, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.contour_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Convolution(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
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
                case 3:
                    convolution.setMatrix(Matrix.EMBOSS);
                    break;
                default:
                    break;
            }
            convolution.setLength(3);

            convolution.apply(inRenderScript);
            main.setApplyImage();
        }
    }),

    SKETCH("Sketch", R.id.ButtonSketch, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Sketch(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Sketch sketch = (Sketch) main.layerFilter;
            sketch.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    GREY("Grey", R.id.ButtonGrey, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Grey(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Grey grey = (Grey) main.layerFilter;
            grey.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    INVERT("Invert", R.id.ButtonInvert, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Invert(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Invert invert = (Invert) main.layerFilter;
            invert.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    SEPIA("Sepia", R.id.ButtonSepia, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Sepia(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Sepia sepia = (Sepia) main.layerFilter;
            sepia.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    PIXELATE("Pixelated", R.id.ButtonPixel, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.pixalate_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Pixelated(main, main.applyImage);

            updateText(main, R.id.blurring_size, R.id.blurring_text, true, 0);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Pixelated pixel = (Pixelated) main.layerFilter;

            pixel.setLength(getSeekBarProgress(main, R.id.blurring_size, true));

            pixel.apply(inRenderScript);
            main.setApplyImage();

        }
    }),

    MIRROR("Mirror", R.id.ButtonMirror, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(R.layout.miror_layout, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Mirror(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Mirror miror= (Mirror) main.layerFilter;

            ToggleButton toggle = (ToggleButton)main.findViewById(R.id.mirorToggle);
            miror.setOrientation(toggle.isChecked());

            miror.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    CARTOON("Cartoon", R.id.ButtonCartoon, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new Cartoon(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            Cartoon cartoon= (Cartoon) main.layerFilter;
            cartoon.apply(inRenderScript);
            main.setApplyImage();
        }
    }),
    FACE_DETECTION("Face Detector", R.id.ButtonFace, new ILayerType() {
        @Override
        public void setInflactor(MainActivity main) {
            main.InflateLayer(-1, R.id.optionID);
        }

        @Override
        public void generateLayer(MainActivity main) {
            main.layerFilter = new FaceDetection(main, main.applyImage);
        }

        @Override
        public void applyLayer(MainActivity main, boolean inRenderScript) {
            FaceDetection faceDetection = (FaceDetection) main.layerFilter;
            faceDetection.apply(inRenderScript);
            main.setApplyImage();
        }
    });

    private String name;
    private ILayerType type;
    private int buttonId;

    /**
     * The Filter constructor
     *
     * @param name
     *      The filter name
     * @param buttonId
     *      The filter button id
     * @param type
     *      The filter type interface
     */
    LayerType(String name, int buttonId, ILayerType type) {
        this.name = name;
        this.type = type;
        this.buttonId = buttonId;
    }

    /**
     * Return the filter type interface
     *
     * @return
     *      The filter type interface
     */
    public ILayerType getType() {
        return this.type;
    }

    /**
     * Generate the selected button
     *
     * @param main
     *      The main activity
     */
    public void generateSelectButton(final MainActivity main){
        ImageButton select = (ImageButton)main.findViewById(this.buttonId);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setLayerType(LayerType.this);
                type.setInflactor(main);
                type.generateLayer(main);

                for(LayerType t : LayerType.values()){
                    ImageButton button = (ImageButton)main.findViewById(t.buttonId);
                    if(button != view)
                        button.setBackgroundResource(R.color.transparent);
                    else
                        button.setBackgroundResource(R.color.colorPrimaryDark);

                }

                TextView filter = (TextView)main.findViewById(R.id.filterName);
                filter.setText(name);
            }
        });
    }

    /**
     * Return the spinner index selected
     *
     * @param main
     *      The main activity
     * @param id
     *      The spinner id
     * @return
     *      The spinner index selected
     */
    private static int getSpinnerIndex(MainActivity main, int id) {
        Spinner menu = (Spinner) main.findViewById(id);
        return menu.getSelectedItemPosition();
    }

    /**
     * Return checkbox value
     *
     * @param main
     *      The main activity
     * @param id
     *      The checkbox id
     * @return
     *      The checkbox value
     */
    private static boolean getCheckBoxSelect(MainActivity main, int id) {
        CheckBox button = (CheckBox) main.findViewById(id);
        return button.isChecked();
    }

    /**
     * Return the seekBar value
     *
     * @param main
     *      The main activity
     * @param id
     *      The seekBar id
     * @param onlyImpair
     *      TRUE if the value must be impair
     * @return
     *      The seekBar value
     */
    private static int getSeekBarProgress(MainActivity main, int id, boolean onlyImpair) {
        SeekBar bar = (SeekBar) main.findViewById(id);
        if (onlyImpair) {
            return bar.getProgress() * 2 + 1;
        } else {
            return bar.getProgress();
        }
    }

    /**
     * Update the link text of the seekBar
     *
     * @param main
     *      The main activity
     * @param seekBarID
     *      The seekBar id
     * @param textViewID
     *      The textView id
     * @param onlyImpair
     *      TRUE if the seekbBar value is only impair
     * @param add
     *      value to add
     */
    private static void updateText(final MainActivity main, int seekBarID, final int textViewID, final boolean onlyImpair, final int add) {
        SeekBar bar = (SeekBar) main.findViewById(seekBarID);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeValue(main,i, textViewID, onlyImpair, add);
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

        //Initialize
        changeValue(main, bar.getProgress(), textViewID, onlyImpair, add);
    }

    /**
     * Change the textView value
     *
     * @param main
     *      The main activity
     * @param value
     *      The new text value
     * @param textViewID
     *      The textVew id
     * @param onlyImpair
     *      TRUE if the value is only impair
     * @param add
     *      value to add
     */
    private static void changeValue(MainActivity main, int value, int textViewID, boolean onlyImpair, int add){
        TextView text = (TextView) main.findViewById(textViewID);

        if (onlyImpair)
            text.setText("" + ((value * 2 + 1) + add));
        else
            text.setText("" + (value + add));
    }


    /**
     * Update the color view value
     *
     * @param main
     *      The main activity
     * @param imgFrom
     *      The image view id
     * @param red
     *      The red seekBar id
     * @param green
     *      The green seekBar id
     * @param blue
     *      The blue seekBar id
     */
    private static void updateColorView(final MainActivity main, final int imgFrom, int red, int green, int blue) {
        final SeekBar redBar = (SeekBar) main.findViewById(red);
        final SeekBar blueBar = (SeekBar) main.findViewById(blue);
        final SeekBar greenBar = (SeekBar) main.findViewById(green);

        SeekBar.OnSeekBarChangeListener event = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeValue(main, imgFrom, redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
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

        changeValue(main, imgFrom, redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
    }

    /**
     * Change the color value
     *
     * @param main
     *      The main activity
     * @param imgFrom
     *      The image view id
     * @param red
     *      The red value
     * @param green
     *      The green value
     * @param blue
     *      The blue value
     */
    private static void changeValue(MainActivity main, int imgFrom, int red, int green, int blue){
        ImageView imgView = (ImageView) main.findViewById(imgFrom);

        Bitmap map = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
        int[] pixels = new int[map.getHeight() * map.getWidth()];

        for (int index = 0; index < pixels.length; index++) {
            pixels[index] = Color.argb(255, red, green, blue);
        }
        map.setPixels(pixels, 0, map.getWidth(), 0, 0, map.getWidth(), map.getHeight());
        imgView.setImageBitmap(map);
    }
}
