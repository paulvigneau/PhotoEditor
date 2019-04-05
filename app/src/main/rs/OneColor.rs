#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)


float red;
float green;
float blue;
float dist;

uchar4  RS_KERNEL  OneColor(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float newR = pixelf.r - red;
    float newG = pixelf.g - green;
    float newB = pixelf.b -blue;
    float length = sqrt(newR*newR + newB*newB + newG*newG);
    if( length > dist){
        float grey = 0.3 * pixelf.r + 0.59 * pixelf.g + 0.11 * pixelf.b;
        pixelf.r = grey;
        pixelf.g = grey;
        pixelf.b = grey;
    }
    return  rsPackColorTo8888(pixelf.r, pixelf.g, pixelf.b, pixelf.a);
}