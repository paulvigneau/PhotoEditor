package example.com.projet;


import android.graphics.Color;

import example.com.projet.utils.*;


public class Histogram {


    private int[] values;

    public Histogram(Image image, ColorType color){
        values = createHistogram(image ,color);
    }

    public int[] createHistogram(Image image, ColorType color){
        int[] histogram = new int[256];
        int[] pixels = image.getPixels();
        int size = image.getHeight() * image.getWidth();
        for(int i=0; i<size; i++){
            switch(color){
                case RED:
                    histogram[Color.red(pixels[i])]++;
                    break;
                case GREEN :
                    histogram[Color.green(pixels[i])]++;
                    break;
                case BLUE :
                    histogram[Color.blue(pixels[i])]++;
                    break;
                case GREY :
                    histogram[ColorTools.getGreyColor(i)]++;
                    break;
                default :
                    break;
            }
        }
        return histogram;
    }

    public int[] getHistogram(){
        return values;
    }

  /*  public int[] getGS(){
        if(this.GS == null) this.GS = createHistogram(ColorType.GREY);
        return this.GS;
    }
    public int[] getRed (){
        if(this.R == null) this.R = createHistogram(ColorType.RED);
        return this.R;
    }
    public int[] getBlue (){
        if(this.B == null) this.B = createHistogram(ColorType.BLUE);
        return this.B;
    }
    public int[] getGreen (){
        if(this.G == null) this.G = createHistogram(ColorType.GREEN);
        return this.G;
    }*/


}
