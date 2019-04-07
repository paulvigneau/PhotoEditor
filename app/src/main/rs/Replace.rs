#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)


float red;
float green;
float blue;
float replaceR;
float replaceG;
float replaceB;
float dist;

uchar4  RS_KERNEL  Replace(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float newR = pixelf.r - red;
    float newG = pixelf.g - green;
    float newB = pixelf.b -blue;
    float length = sqrt(newR*newR + newB*newB + newG*newG);
    if( length <dist){
        pixelf.r =  replaceR;
        pixelf.g = replaceG;
        pixelf.b = replaceB;
    }
    return  rsPackColorTo8888(pixelf.r, pixelf.g, pixelf.b, pixelf.a);
}