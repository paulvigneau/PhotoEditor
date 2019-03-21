#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)

uchar4  RS_KERNEL  Invert(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float newR = 1- pixelf.r;
    float newG =1- pixelf.g;
    float newB =1- pixelf.b;

    return  rsPackColorTo8888(newR, newG, newB, pixelf.a);
}