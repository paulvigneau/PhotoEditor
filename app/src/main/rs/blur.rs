# pragma version (1)
# pragma rs java_package_name(pvigneau.s5.projet)

rs_allocation in;
int width, height;

static float getValueColor(float4 pixel, int i) {
    if (i == 0)
        return pixel.r;
    else if (i == 1)
        return pixel.g;
    else
        return pixel.b;
}

uchar4 __attribute__((kernel)) gaussianBlur(uint32_t x, uint32_t y) {
    uchar4 u4 = rsGetElementAt_uchar4(in, x, y);
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
}