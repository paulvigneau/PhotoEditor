#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)

uchar4  RS_KERNEL  Sepia(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);

     float newRed = 0.393*pixelf.r + 0.769*pixelf.g + 0.189*pixelf.b;
     float newGreen = 0.349*pixelf.r + 0.686*pixelf.g + 0.168*pixelf.b;
     float newBlue = 0.272*pixelf.r + 0.534*pixelf.g + 0.131*pixelf.b;
     if(newRed>255) newRed=255;
     if(newGreen>255) newGreen=255;
     if(newBlue>255) newBlue=255;

    return  rsPackColorTo8888(newRed, newGreen, newBlue, pixelf.a);
}