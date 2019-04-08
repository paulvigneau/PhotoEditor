# pragma version (1)
# pragma rs java_package_name(com.android.rssample)

rs_allocation in;
int width;
int height;

float *matrix;
int matrixSize;


static float4 *getValue(int indexX, int indexY){
    float4 value[2];

    indexX -= (int)(matrixSize / 2);
    indexY -= (int)(matrixSize / 2);

    int size = 0;
    for(int y = 0; y < matrixSize; y++){
        for(int x = 0; x < matrixSize; x++){
            int localX = indexX + x;
            int localY = indexY + y;

            int finalX = abs((int)(localX - (localX / (width - 1)) * fmod((float)localX, (float)(width - 1)) * 2));
            int finalY = abs((int)(localY - (localY / (height - 1)) * fmod((float)localY, (float)(height - 1)) * 2));

            float4 color = rsUnpackColor8888(rsGetElementAt_uchar4(in, finalX, finalY));
            float mult = matrix[y + x * matrixSize];

            value[0].r += color.r * mult;
            value[0].g += color.g * mult;
            value[0].b += color.b * mult;

            mult = matrix[x + y * matrixSize];

            value[1].r += color.r * mult;
            value[1].g += color.g * mult;
            value[1].b += color.b * mult;

            size += 1;
        }
    }
    return value;
}


uchar4 RS_KERNEL Cartoon(uint32_t x, uint32_t y) {
    float4 originalColor = rsUnpackColor8888(rsGetElementAt_uchar4(in,x,y));

    float red;
    if(originalColor.r <0.2) red = 0.1;
    if(originalColor.r <0.4) red = 0.3;
    if(originalColor.r<0.6) red = 0.5;
    if(originalColor.r<0.8) red = 0.7;
    else red = 0.9;

    float green;
     if(originalColor.g <0.2) green = 0.1;
     if(originalColor.g <0.4) green = 0.3;
     if(originalColor.g<0.6) green = 0.5;
     if(originalColor.g<0.8) green = 0.7;
     else green = 0.9;

    float blue;
     if(originalColor.b <0.2) blue = 0.1;
     if(originalColor.b <0.4) blue = 0.3;
     if(originalColor.b<0.6) blue = 0.5;
     if(originalColor.b<0.8) blue = 0.7;
     else blue = 0.9;

    //sobel
    float4 *valeur = getValue(x, y);

    float4 color;
    color.r = sqrt(valeur[0].r * valeur[0].r + valeur[1].r * valeur[1].r);
    color.g = sqrt(valeur[0].g * valeur[0].g + valeur[1].g * valeur[1].g);
    color.b = sqrt(valeur[0].b * valeur[0].b + valeur[1].b * valeur[1].b);
    color.a = 255;


    //choose value depending if it's an edge or not
    if(color.r < 0.5 && color.g <0.5 && color.b <0.5) {
      color.r = red;
      color.g = green;
      color.b = blue;
    }else{
        color.r = 0.0;
        color.g = 0.0;
        color.b = 0.0;
    }

    return rsPackColorTo8888(color.r, color.g, color.b, 1);
}