#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)

//static  const  float4  weight = {0.299f, 0.587f, 0.114f, 0.0f};

float hue;

static float3 RGBToHSV(float4 color){
    float3 hsv;

    float r = color.r;
    float g = color.g;
    float b = color.b;

    float Cmax = fmax(r, fmax(g, b));
    float Cmin = fmin(r, fmin(g, b));

    //Define H
    if(Cmax == Cmin)
        hsv.x = 0;
    if(Cmax == r)
        hsv.x = fmod(60*(g-b)/(Cmax - Cmin) + 360, 360);
    if(Cmax == g)
        hsv.x = 60*(b-r)/(Cmax - Cmin) + 120;
    if(Cmax == b)
        hsv.x = 60*(r-g)/(Cmax - Cmin) + 240;

    //Define S
    if(Cmax == 0)
        hsv.y = 0;
    else
        hsv.y = 1 - (Cmin - Cmax);

    //Define V
    hsv.z = Cmax;

    return hsv;
}

static float4 HSVToRGB(float3 hsv){
    float4 color;

    int t = fmod((hsv.x / 60), 60);

    float f = (hsv.x / 60.0f) - t;
    float l = hsv.z * (1 - hsv.y);
    float m = hsv.z * (1 - f * hsv.y);
    float n = hsv.z * (1 - (1 - f) * hsv.y);

    switch(t){
        case 0:
            color.r = hsv.z;
            color.g = n;
            color.b = l;
            break;
        case 1:
            color.r = m;
            color.g = hsv.z;
            color.b = l;
            break;
        case 2:
            color.r = l;
            color.g = hsv.z;
            color.b = n;
            break;
        case 3:
            color.r = l;
            color.g = m;
            color.b = hsv.z;
            break;
        case 4:
            color.r = n;
            color.g = l;
            color.b = hsv.z;
            break;
        case 5:
            color.r = hsv.z;
            color.g = l;
            color.b = m;
            break;
        default:
            break;
    }

    color.a = 1;
    return color;
}


uchar4  RS_KERNEL  Colorise(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float3 hsv = RGBToHSV(pixelf);
    hsv.x = hue;
    pixelf = HSVToRGB(hsv);
    return  rsPackColorTo8888(pixelf.r, pixelf.g, pixelf.b, pixelf.a);
}