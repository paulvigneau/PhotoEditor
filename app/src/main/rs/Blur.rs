# pragma version (1)
# pragma rs java_package_name(com.android.rssample)

rs_allocation in;
int width;
int height;

int matrix[];
int matrixSizeX;
int matrixSizeY;

static float4 getValue(int indexX, int indexY){
    float4 value;
    value.a = rsUnpackColor8888(rsGetElementAt_uchar4(in, indexX, indexY)).a;

    indexX -= (int)(matrixSizeX / 2);
    indexY -= (int)(matrixSizeY / 2);

    int size = 0;
    for(int y = 0; y < matrixSizeY; y++){
        for(int x = 0; x < matrixSizeX; x++){
            int localX = indexX + x;
            int localY = indexY + y;

            int finalX = abs((int)(localX - (int)(localX/(width-1)) * (fmod((float)localX, (float)(width-1))) * 2));
            int finalY = abs((int)(localY - (int)(localY/(height-1)) * (fmod((float)localY, (float)(height-1))) * 2));

            float4 color = rsUnpackColor8888(rsGetElementAt_uchar4(in, finalX, finalY));
            int mult = matrix[size];
            value.r += (float)(color.r * mult);
            value.g += (float)(color.g * mult);
            value.b += (float)(color.b * mult);

            size += 1;
        }
    }

    if(size != 0){
        value.r = (float)(value.r / size);
        value.g = (float)(value.g / size);
        value.b = (float)(value.b / size);
    }
    return value;
}

/*static float getValueColor(float4 pixel, int i) {
    if (i == 0)
        return pixel.r;
    else if (i == 1)
        return pixel.g;
    else
        return pixel.b;
}*/

uchar4 RS_KERNEL Blur(uint32_t x, uint32_t y) {
    float4 color = getValue(x, y);

    return rsPackColorTo8888(color.r, color.g, color.b, color.a);
}

    /* uchar4 u4 = rsGetElementAt_uchar4(in, x, y);
    float4 pixelf = rsUnpackColor8888(u4);

    float pixel[] = {0, 0, 0};

    if (y >= 2 && y < height - 2 && x >= 2 && x < width - 2) {
    	for (int i = 0; i < 3; i++) {
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 2), y - 2)), i);          // Première ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 2), y - 1)), i)   * 2;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 2), y)), i)       * 3;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 2), y + 1)), i)   * 2;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 2), y + 2)), i);

    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y - 2)), i)   * 2;    // Deuxième ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y - 1)), i)   * 6;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y)), i)       * 8;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y + 1)), i)   * 6;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y + 2)), i)   * 2;

    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y - 2)), i)         * 3;    // Troisième ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y - 1)), i)         * 8;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)), i)             * 10;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y + 1)), i)         * 8;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y + 2)), i)         * 3;

    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y - 2)), i)   * 2;    // Quatrième ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y - 1)), i)   * 6;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y)), i)       * 8;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y + 1)), i)   * 6;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y + 2)), i)   * 2;

    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 2), y - 2)), i);          // Cinquième ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 2), y - 1)), i)   * 2;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 2), y)), i)       * 3;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 2), y + 1)), i)   * 2;
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 2), y + 2)), i);

    		pixel[i] /= 98;
    	}
    }
    else {
    	pixel[0] = pixelf.r;
    	pixel[1] = pixelf.g;
    	pixel[2] = pixelf.b;
    }

    return rsPackColorTo8888(pixel[0], pixel[1], pixel[2], pixelf.a);
}

uchar4 __attribute__((kernel)) boxBlur(uint32_t x, uint32_t y) {
    uchar4 u4 = rsGetElementAt_uchar4(in, x, y);
    float4 pixelf = rsUnpackColor8888(u4);

    float pixel[] = {0, 0, 0};

    if (y >= 1 && y < height - 1 && x >= 1 && x < width - 1) {
    	for (int i = 0; i < 3; i++) {
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y - 1)), i);     // Première ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y)), i);
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x - 1), y + 1)), i);

    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y - 1)), i);           // Deuxième ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)), i);
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y + 1)), i);

    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y - 1)), i);     // Troisième ligne
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y)), i);
    		pixel[i] += getValueColor(rsUnpackColor8888(rsGetElementAt_uchar4(in, (x + 1), y + 1)), i);

    		pixel[i] /= 9;
    	}
    }
    else {
    	pixel[0] = pixelf.r;
    	pixel[1] = pixelf.g;
    	pixel[2] = pixelf.b;
    }

    return rsPackColorTo8888(pixel[0], pixel[1], pixel[2], pixelf.a);
}*/