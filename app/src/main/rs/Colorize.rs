#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)


float hue;

static float3 RGBToHSV(float4 color){
    float3 hsv;

    float r = color.r;
    float g = color.g;
    float b = color.b;

    float Cmax = fmax(r, fmax(g, b));
    float Cmin = fmin(r, fmin(g, b));

    float delta = Cmax - Cmin;

    //Define H
    if(delta == 0)
        hsv.x = 0;
    else if(Cmax == r)
        hsv.x = 60 * fmod((g-b)/delta, 6);
    else if(Cmax == g)
        hsv.x = 60 * ((b-r)/delta + 2);
    else if(Cmax == b)
        hsv.x = 60 * ((r-g)/delta + 4);

    //Define S
    if(Cmax == 0)
        hsv.y = 0;
    else
        hsv.y = delta / Cmax;

    //Define V
    hsv.z = Cmax;

    return hsv;
}

static float4 HSVToRGB(float3 hsv){
    float4 color;

    float c = hsv.z * hsv.y;
    float x = c * (1 - fabs(fmod(hsv.x / 60.0f, 2) - 1));
    float m = hsv.z - c;

    int t = fmod((hsv.x / 60), 60);


    switch(t){
        case 0:
            color.r = c + m;
            color.g = x + m;
            color.b = m;
            break;
        case 1:
            color.r = x + m;
            color.g = c + m;
            color.b = m;
            break;
        case 2:
            color.r = m;
            color.g = c + m;
            color.b = x + m;
            break;
        case 3:
            color.r = m;
            color.g = x + m;
            color.b = c + m;
            break;
        case 4:
            color.r = x + m;
            color.g = m;
            color.b = c + m;
            break;
        case 5:
            color.r = c + m;
            color.g = m;
            color.b = x + m;
            break;
        default:
            color.r = c + m;
            color.g = x + m;
            color.b = m;
            break;
    }

    color.a = 1;
    return color;
}


uchar4  RS_KERNEL  Colorize(uchar4 in, uint32_t x, uint32_t y) {
    float4  pixelf = rsUnpackColor8888(in);
    float3 hsv = RGBToHSV(pixelf);
    hsv.x = hue;
    pixelf = HSVToRGB(hsv);
    return  rsPackColorTo8888(pixelf.r, pixelf.g, pixelf.b, pixelf.a);
}