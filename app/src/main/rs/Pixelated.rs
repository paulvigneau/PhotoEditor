# pragma version (1)
# pragma rs java_package_name(com.android.rssample)

rs_allocation in;
int width;
int height;

float *matrix;
int matrixSize;

static float4 getValue(int indexX, int indexY){
    float4 value;

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

            value.r += color.r * mult;
            value.g += color.g * mult;
            value.b += color.b * mult;

            size += 1;
        }
    }
    value.a = 255;
    return value;
}

uchar4 RS_KERNEL Pixelated(uint32_t x, uint32_t y) {
    float4 color = getValue(x, y);

    return rsPackColorTo8888(color.r, color.g, color.b, color.a);
}