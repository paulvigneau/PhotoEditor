#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)

uchar4  RS_KERNEL  Grey(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float newColor = 0.3 * pixelf.r + 0.59 *pixelf.g + 0.11 * pixelf.b;

    return  rsPackColorTo8888(newColor, newColor, newColor, pixelf.a);
}