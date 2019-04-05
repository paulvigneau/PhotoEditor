# pragma version (1)
# pragma rs java_package_name(com.android.rssample)

rs_allocation in;
int width;
int height;

float *matrix;
int matrixSize;

//float4 value[2];// = malloc(sizeof(float4) * 2);

static float4 *getValue(int indexX, int indexY){
    float4 value[2];//(float4 *) malloc(sizeof(float4) * 2);

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

uchar4 RS_KERNEL Sketch(uint32_t x, uint32_t y) {
    float4 *valeur = getValue(x, y);

    float4 color;
    color.r = 1 - sqrt(valeur[0].r * valeur[0].r + valeur[1].r * valeur[1].r);
    color.g =1- sqrt(valeur[0].g * valeur[0].g + valeur[1].g * valeur[1].g);
    color.b = 1-sqrt(valeur[0].b * valeur[0].b + valeur[1].b * valeur[1].b);
    color.a = 255;

    return rsPackColorTo8888(color.r, color.g, color.b, 1);
}